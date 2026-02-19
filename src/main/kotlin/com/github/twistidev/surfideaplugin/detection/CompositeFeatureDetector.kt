package com.github.twistidev.surfideaplugin.detection

import com.intellij.openapi.module.Module

/**
 * Composite detector that combines multiple detection strategies
 */
class CompositeFeatureDetector(
    private val detectors: List<SurfFeatureDetector> = listOf(
        GradleKotlinDslDetector(),
        ClasspathDetector()
    )
) : SurfFeatureDetector {
    
    override fun detectFeatures(module: Module): Set<String> {
        val allFeatures = mutableSetOf<String>()
        for (detector in detectors) {
            allFeatures.addAll(detector.detectFeatures(module))
        }
        return allFeatures
    }
    
    override fun hasFeature(module: Module, featureId: String): Boolean {
        return detectors.any { it.hasFeature(module, featureId) }
    }
}
