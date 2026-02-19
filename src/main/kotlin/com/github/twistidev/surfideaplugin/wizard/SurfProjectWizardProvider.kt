package com.github.twistidev.surfideaplugin.wizard

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Provider for Surf project wizard
 * TODO: Implement full wizard UI and integration with IntelliJ Platform
 */
interface SurfProjectWizardProvider {
    
    /**
     * Show the wizard and collect user choices
     * @return Model with selected options, or null if cancelled
     */
    fun showWizard(): SurfProjectTemplateModel?
    
    /**
     * Generate a multi-module Gradle Kotlin project based on the template model
     * @param model The template model with user selections
     * @param targetDirectory The directory where the project should be created
     * @return The created project, or null if generation failed
     */
    fun generateProject(model: SurfProjectTemplateModel, targetDirectory: VirtualFile): Project?
}

/**
 * Stub implementation of SurfProjectWizardProvider
 * TODO: Implement actual wizard UI and project generation
 */
class SurfProjectWizardProviderImpl : SurfProjectWizardProvider {
    
    override fun showWizard(): SurfProjectTemplateModel? {
        // TODO: Show wizard dialog
        // - Step 1: Select target platform (Paper, Velocity, etc.)
        // - Step 2: Select Surf features (surf-api, surf-redis, surf-database)
        // - Step 3: Configure project structure (multi-module vs single)
        // - Step 4: Configure base package and project name
        return null
    }
    
    override fun generateProject(model: SurfProjectTemplateModel, targetDirectory: VirtualFile): Project? {
        // TODO: Generate Gradle Kotlin multi-module project
        // - Create root build.gradle.kts with common configuration
        // - Create settings.gradle.kts with module includes
        // - Create module directories and their build.gradle.kts files
        // - Generate sample Kotlin source files
        // - Add appropriate dependencies based on selected features
        return null
    }
}

/**
 * Generator for Gradle build files
 */
object GradleBuildFileGenerator {
    
    fun generateRootBuildFile(model: SurfProjectTemplateModel): String {
        // TODO: Generate root build.gradle.kts with common configuration
        return """
            plugins {
                kotlin("jvm") version "2.3.0"
            }
            
            allprojects {
                group = "${model.basePackage}"
                version = "1.0-SNAPSHOT"
                
                repositories {
                    mavenCentral()
                }
            }
            
            subprojects {
                apply(plugin = "kotlin")
                
                dependencies {
                    implementation(kotlin("stdlib"))
                }
            }
        """.trimIndent()
    }
    
    fun generateSettingsFile(model: SurfProjectTemplateModel): String {
        val modules = model.getModuleStructure()
        val includes = modules.joinToString("\n") { "include(\"${it.name}\")" }
        
        return """
            rootProject.name = "${model.projectName}"
            
            $includes
        """.trimIndent()
    }
    
    fun generateModuleBuildFile(module: ModuleDescriptor, model: SurfProjectTemplateModel): String {
        // TODO: Generate module-specific build.gradle.kts
        val dependencies = mutableListOf<String>()
        
        if ("surf-api" in model.selectedFeatures) {
            dependencies.add("    implementation(\"com.surf:surf-api:+\") // TODO: Use actual version")
        }
        if ("surf-redis" in model.selectedFeatures && module.name == "redis") {
            dependencies.add("    implementation(\"com.surf:surf-redis:+\") // TODO: Use actual version")
        }
        
        return """
            dependencies {
            ${dependencies.joinToString("\n")}
            }
        """.trimIndent()
    }
}
