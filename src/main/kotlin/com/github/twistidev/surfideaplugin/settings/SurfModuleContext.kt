package com.github.twistidev.surfideaplugin.settings

import com.intellij.openapi.module.Module
import com.github.twistidev.surfideaplugin.detection.CompositeFeatureDetector
import com.github.twistidev.surfideaplugin.model.SurfFeature
import com.github.twistidev.surfideaplugin.model.SurfFeatureRegistry
import com.github.twistidev.surfideaplugin.model.SurfPlatform

/**
 * Provides context about Surf features enabled in a module
 */
class SurfModuleContext(
    private val module: Module
) {
    private val settingsService = SurfProjectSettingsService.getInstance(module.project)
    private val detector = CompositeFeatureDetector()
    
    /**
     * Get enabled features for this module (from settings)
     */
    fun getEnabledFeatures(): Set<String> {
        return settingsService.getEnabledFeatures(module.name)
    }
    
    /**
     * Get detected features for this module (from build files)
     */
    fun getDetectedFeatures(): Set<String> {
        return detector.detectFeatures(module)
    }
    
    /**
     * Get all features (enabled or detected)
     */
    fun getAllFeatures(): Set<String> {
        return getEnabledFeatures() + getDetectedFeatures()
    }
    
    /**
     * Check if a feature is enabled or detected
     */
    fun hasFeature(featureId: String): Boolean {
        return getAllFeatures().contains(featureId)
    }
    
    /**
     * Get SurfFeature objects for all enabled/detected features
     */
    fun getFeatureObjects(): List<SurfFeature> {
        return getAllFeatures().mapNotNull { SurfFeatureRegistry.getFeature(it) }
    }
    
    /**
     * Get supported platforms based on enabled features
     */
    fun getSupportedPlatforms(): Set<SurfPlatform> {
        val platforms = mutableSetOf<SurfPlatform>()
        for (feature in getFeatureObjects()) {
            platforms.addAll(feature.supportedPlatforms)
        }
        return platforms
    }
    
    companion object {
        fun forModule(module: Module): SurfModuleContext {
            return SurfModuleContext(module)
        }
    }
}
