package com.github.twistidev.surfideaplugin.wizard

import com.github.twistidev.surfideaplugin.model.SurfPlatform

/**
 * Model for Surf project wizard template
 * Stores selected features and platforms for project generation
 */
data class SurfProjectTemplateModel(
    val projectName: String,
    val basePackage: String,
    val selectedFeatures: Set<String>,
    val targetPlatform: SurfPlatform,
    val useMultiModule: Boolean = true,
    val includeExamples: Boolean = false
) {
    /**
     * Get module structure based on selected features
     */
    fun getModuleStructure(): List<ModuleDescriptor> {
        val modules = mutableListOf<ModuleDescriptor>()
        
        // TODO: Generate module structure based on selected features
        // Example structure:
        // - api module (if surf-api selected)
        // - platform module (paper/velocity specific)
        // - plugin module (main implementation)
        
        if (useMultiModule) {
            modules.add(ModuleDescriptor("api", "API module"))
            modules.add(ModuleDescriptor("plugin", "Plugin module"))
            
            if ("surf-redis" in selectedFeatures) {
                modules.add(ModuleDescriptor("redis", "Redis integration module"))
            }
        } else {
            modules.add(ModuleDescriptor(projectName, "Main module"))
        }
        
        return modules
    }
}

/**
 * Describes a module in the generated project
 */
data class ModuleDescriptor(
    val name: String,
    val description: String,
    val dependencies: List<String> = emptyList()
)
