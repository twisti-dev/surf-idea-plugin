package com.github.twistidev.surfideaplugin.model

/**
 * Manages dependencies between Surf features
 */
class SurfDependencyGraph {
    
    /**
     * Resolves all dependencies for a set of feature IDs, including transitive dependencies
     * @param featureIds Initial set of feature IDs
     * @return Set of all feature IDs including dependencies, in topological order
     */
    fun resolveDependencies(featureIds: Set<String>): List<String> {
        val resolved = mutableSetOf<String>()
        val visiting = mutableSetOf<String>()
        val result = mutableListOf<String>()
        
        fun visit(featureId: String) {
            if (featureId in resolved) return
            if (featureId in visiting) {
                throw CyclicDependencyException("Cyclic dependency detected involving: $featureId")
            }
            
            val feature = SurfFeatureRegistry.getFeature(featureId)
                ?: throw FeatureNotFoundException("Feature not found: $featureId")
            
            visiting.add(featureId)
            
            // Visit dependencies first
            for (depId in feature.dependsOn) {
                visit(depId)
            }
            
            visiting.remove(featureId)
            resolved.add(featureId)
            result.add(featureId)
        }
        
        for (featureId in featureIds) {
            visit(featureId)
        }
        
        return result
    }
    
    /**
     * Validates that a set of features can be enabled together
     * @param featureIds Set of feature IDs to validate
     * @return List of validation errors, empty if valid
     */
    fun validate(featureIds: Set<String>): List<String> {
        val errors = mutableListOf<String>()
        
        for (featureId in featureIds) {
            val feature = SurfFeatureRegistry.getFeature(featureId)
            if (feature == null) {
                errors.add("Unknown feature: $featureId")
                continue
            }
            
            // Check if dependencies are present
            for (depId in feature.dependsOn) {
                if (depId !in featureIds) {
                    val depFeature = SurfFeatureRegistry.getFeature(depId)
                    errors.add("Feature '${feature.displayName}' requires '${depFeature?.displayName ?: depId}'")
                }
            }
        }
        
        return errors
    }
    
    /**
     * Gets all features that depend on a given feature
     * @param featureId The feature ID to check
     * @return Set of feature IDs that depend on the given feature
     */
    fun getDependents(featureId: String): Set<String> {
        return SurfFeatureRegistry.getAllFeatures()
            .filter { featureId in it.dependsOn }
            .map { it.id }
            .toSet()
    }
}

class CyclicDependencyException(message: String) : Exception(message)
class FeatureNotFoundException(message: String) : Exception(message)
