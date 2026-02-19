package com.github.twistidev.surfideaplugin.detection

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope

/**
 * Detects Surf features by checking classpath for known classes/packages
 */
class ClasspathDetector : SurfFeatureDetector {
    
    override fun detectFeatures(module: Module): Set<String> {
        val detectedFeatures = mutableSetOf<String>()
        val javaPsiFacade = JavaPsiFacade.getInstance(module.project)
        val scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)
        
        // Check for surf-api
        if (hasClass(javaPsiFacade, scope, "com.surf.api.SurfApi")) {
            detectedFeatures.add("surf-api")
        }
        
        // Check for surf-redis
        if (hasClass(javaPsiFacade, scope, "com.surf.redis.RedisService")) {
            detectedFeatures.add("surf-redis")
        }
        
        // Check for surf-database
        if (hasClass(javaPsiFacade, scope, "com.surf.database.DatabaseService")) {
            detectedFeatures.add("surf-database")
        }
        
        // TODO: Add more heuristics for platform-specific features
        
        return detectedFeatures
    }
    
    override fun hasFeature(module: Module, featureId: String): Boolean {
        return detectFeatures(module).contains(featureId)
    }
    
    private fun hasClass(facade: JavaPsiFacade, scope: GlobalSearchScope, fqn: String): Boolean {
        return facade.findClass(fqn, scope) != null
    }
}
