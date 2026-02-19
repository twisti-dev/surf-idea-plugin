package com.github.twistidev.surfideaplugin.framework

import com.intellij.framework.FrameworkTypeEx
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableModelsProvider
import com.intellij.openapi.roots.ModifiableRootModel
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * Provides framework support for Surf in modules
 */
class SurfFrameworkSupportProvider : FrameworkSupportInModuleProvider() {
    
    override fun getFrameworkType(): FrameworkTypeEx = SurfFrameworkType()
    
    override fun createConfigurable(model: FrameworkSupportModel): FrameworkSupportInModuleConfigurable {
        return SurfFrameworkConfigurable()
    }
    
    override fun isEnabledForModuleType(moduleType: ModuleType<*>): Boolean {
        // Enable for all module types, but check for Kotlin support would be better
        return true
    }
}

/**
 * Configurable for Surf framework support
 */
class SurfFrameworkConfigurable : FrameworkSupportInModuleConfigurable() {
    
    override fun createComponent(): JComponent {
        // TODO: Create a proper UI for selecting Surf features
        return JLabel("Surf Framework Support - Feature selection UI coming soon")
    }
    
    override fun addSupport(
        module: Module,
        rootModel: ModifiableRootModel,
        modifiableModelsProvider: ModifiableModelsProvider
    ) {
        // TODO: Add selected features to module settings
        // For now, just mark that Surf is enabled
    }
}
