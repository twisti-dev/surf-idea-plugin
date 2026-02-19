package com.github.twistidev.surfideaplugin.detection

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.psi.KtFile

/**
 * Detects Surf features by analyzing Gradle build files (Kotlin DSL)
 */
class GradleKotlinDslDetector : SurfFeatureDetector {
    
    override fun detectFeatures(module: Module): Set<String> {
        val detectedFeatures = mutableSetOf<String>()
        
        // Look for build.gradle.kts files
        val buildFile = findBuildFile(module)
        if (buildFile != null) {
            detectedFeatures.addAll(analyzeBuildFile(module, buildFile))
        }
        
        return detectedFeatures
    }
    
    override fun hasFeature(module: Module, featureId: String): Boolean {
        return detectFeatures(module).contains(featureId)
    }
    
    private fun findBuildFile(module: Module): VirtualFile? {
        val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull()
            ?: return null
        
        // Look for build.gradle.kts
        return moduleRoot.findChild("build.gradle.kts")
            ?: moduleRoot.findChild("build.gradle") // fallback to Groovy
    }
    
    private fun analyzeBuildFile(module: Module, buildFile: VirtualFile): Set<String> {
        val detectedFeatures = mutableSetOf<String>()
        
        // TODO: Parse build file to detect dependencies
        // For now, we'll do simple text matching
        val content = String(buildFile.contentsToByteArray(), Charsets.UTF_8)
        
        // Check for surf-api
        if (content.contains("surf-api")) {
            detectedFeatures.add("surf-api")
            
            // Check for platform-specific additions
            if (content.contains("surf-api-paper")) {
                detectedFeatures.add("surf-api-paper")
            }
            if (content.contains("surf-api-velocity")) {
                detectedFeatures.add("surf-api-velocity")
            }
        }
        
        // Check for surf-redis
        if (content.contains("surf-redis")) {
            detectedFeatures.add("surf-redis")
        }
        
        // Check for surf-database
        if (content.contains("surf-database")) {
            detectedFeatures.add("surf-database")
        }
        
        return detectedFeatures
    }
}
