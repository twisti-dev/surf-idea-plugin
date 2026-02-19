package com.github.twistidev.surfideaplugin.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Persistent state for Surf project settings
 */
@State(
    name = "SurfProjectSettings",
    storages = [Storage("surfSettings.xml")]
)
@Service(Service.Level.PROJECT)
class SurfProjectSettingsService : PersistentStateComponent<SurfProjectSettings> {
    
    private var state = SurfProjectSettings()
    
    override fun getState(): SurfProjectSettings = state
    
    override fun loadState(state: SurfProjectSettings) {
        XmlSerializerUtil.copyBean(state, this.state)
    }
    
    /**
     * Get enabled features for a specific module
     */
    fun getEnabledFeatures(moduleName: String): Set<String> {
        return state.moduleFeatures[moduleName]?.toSet() ?: emptySet()
    }
    
    /**
     * Set enabled features for a specific module
     */
    fun setEnabledFeatures(moduleName: String, features: Set<String>) {
        state.moduleFeatures[moduleName] = features.toMutableSet()
    }
    
    /**
     * Add a feature to a module
     */
    fun addFeature(moduleName: String, featureId: String) {
        val features = state.moduleFeatures.getOrPut(moduleName) { mutableSetOf() }
        features.add(featureId)
    }
    
    /**
     * Remove a feature from a module
     */
    fun removeFeature(moduleName: String, featureId: String) {
        state.moduleFeatures[moduleName]?.remove(featureId)
    }
    
    /**
     * Check if a feature is enabled for a module
     */
    fun hasFeature(moduleName: String, featureId: String): Boolean {
        return state.moduleFeatures[moduleName]?.contains(featureId) ?: false
    }
    
    /**
     * Get entry point annotations
     */
    fun getEntryPointAnnotations(): List<String> {
        return state.entryPointAnnotations
    }
    
    /**
     * Set entry point annotations
     */
    fun setEntryPointAnnotations(annotations: List<String>) {
        state.entryPointAnnotations = annotations.toMutableList()
    }
    
    companion object {
        fun getInstance(project: Project): SurfProjectSettingsService {
            return project.service<SurfProjectSettingsService>()
        }
    }
}

/**
 * State class for Surf project settings
 */
data class SurfProjectSettings(
    /**
     * Map of module name to enabled feature IDs
     */
    var moduleFeatures: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    
    /**
     * List of annotation FQNs that mark elements as entry points
     */
    var entryPointAnnotations: MutableList<String> = mutableListOf(
        "com.surf.annotations.EntryPoint",
        "com.surf.redis.RedisHandler",
        "com.surf.api.ApiEndpoint"
    )
)
