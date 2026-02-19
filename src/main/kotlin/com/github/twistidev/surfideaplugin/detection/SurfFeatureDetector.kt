package com.github.twistidev.surfideaplugin.detection

import com.intellij.openapi.module.Module
import com.github.twistidev.surfideaplugin.model.SurfFeature

/**
 * Interface for detecting enabled Surf features in a module
 */
interface SurfFeatureDetector {
    
    /**
     * Detects which Surf features are enabled in the given module
     * @param module The module to analyze
     * @return Set of feature IDs that are detected
     */
    fun detectFeatures(module: Module): Set<String>
    
    /**
     * Checks if a specific feature is enabled in the module
     * @param module The module to check
     * @param featureId The feature ID to check for
     * @return True if the feature is detected
     */
    fun hasFeature(module: Module, featureId: String): Boolean
}
