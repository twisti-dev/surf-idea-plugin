package com.github.twistidev.surfideaplugin.model

/**
 * Represents a Surf framework feature/dependency that can be enabled in a module.
 */
interface SurfFeature {
    val id: String
    val displayName: String
    val description: String
    val dependsOn: List<String> // IDs of required features
    val supportedPlatforms: Set<SurfPlatform>
    val moduleKind: SurfModuleKind
    
    /**
     * Gradle coordinates for this feature (e.g., "com.example:surf-api:1.0.0")
     */
    val gradleCoordinates: String?
    
    /**
     * Parent feature ID if this is a platform-specific addition (e.g., surf-api-paper is child of surf-api)
     */
    val parentFeature: String?
}

/**
 * Base implementation of SurfFeature
 */
data class SurfFeatureImpl(
    override val id: String,
    override val displayName: String,
    override val description: String,
    override val dependsOn: List<String> = emptyList(),
    override val supportedPlatforms: Set<SurfPlatform> = SurfPlatform.entries.toSet(),
    override val moduleKind: SurfModuleKind = SurfModuleKind.API,
    override val gradleCoordinates: String? = null,
    override val parentFeature: String? = null
) : SurfFeature

/**
 * Registry of all known Surf features
 */
object SurfFeatureRegistry {
    private val features = mutableMapOf<String, SurfFeature>()
    
    init {
        // Core features
        register(SurfFeatureImpl(
            id = "surf-api",
            displayName = "Surf API",
            description = "Core Surf API framework",
            moduleKind = SurfModuleKind.API,
            gradleCoordinates = "com.surf:surf-api:+" // TODO: Use actual version
        ))
        
        register(SurfFeatureImpl(
            id = "surf-api-paper",
            displayName = "Surf API - Paper Platform",
            description = "Paper-specific additions to Surf API",
            dependsOn = listOf("surf-api"),
            supportedPlatforms = setOf(SurfPlatform.PAPER),
            moduleKind = SurfModuleKind.PLATFORM_SPECIFIC,
            parentFeature = "surf-api",
            gradleCoordinates = "com.surf:surf-api-paper:+" // TODO: Use actual version
        ))
        
        register(SurfFeatureImpl(
            id = "surf-api-velocity",
            displayName = "Surf API - Velocity Platform",
            description = "Velocity-specific additions to Surf API",
            dependsOn = listOf("surf-api"),
            supportedPlatforms = setOf(SurfPlatform.VELOCITY),
            moduleKind = SurfModuleKind.PLATFORM_SPECIFIC,
            parentFeature = "surf-api",
            gradleCoordinates = "com.surf:surf-api-velocity:+" // TODO: Use actual version
        ))
        
        register(SurfFeatureImpl(
            id = "surf-redis",
            displayName = "Surf Redis",
            description = "Redis integration for Surf",
            dependsOn = listOf("surf-api"),
            moduleKind = SurfModuleKind.INTEGRATION,
            gradleCoordinates = "com.surf:surf-redis:+" // TODO: Use actual version
        ))
        
        register(SurfFeatureImpl(
            id = "surf-database",
            displayName = "Surf Database",
            description = "Database integration for Surf",
            dependsOn = listOf("surf-api"),
            moduleKind = SurfModuleKind.INTEGRATION,
            gradleCoordinates = "com.surf:surf-database:+" // TODO: Use actual version
        ))
    }
    
    fun register(feature: SurfFeature) {
        features[feature.id] = feature
    }
    
    fun getFeature(id: String): SurfFeature? = features[id]
    
    fun getAllFeatures(): List<SurfFeature> = features.values.toList()
    
    fun getCoreFeatures(): List<SurfFeature> = 
        features.values.filter { it.parentFeature == null }
    
    fun getPlatformAdditions(baseFeatureId: String): List<SurfFeature> =
        features.values.filter { it.parentFeature == baseFeatureId }
}
