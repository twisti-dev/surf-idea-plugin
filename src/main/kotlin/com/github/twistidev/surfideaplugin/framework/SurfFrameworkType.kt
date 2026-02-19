package com.github.twistidev.surfideaplugin.framework

import com.intellij.framework.FrameworkTypeEx
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider
import com.intellij.icons.AllIcons
import javax.swing.Icon

/**
 * Framework type for Surf framework
 */
class SurfFrameworkType : FrameworkTypeEx("SURF_FRAMEWORK") {
    
    override fun getPresentableName(): String = "Surf Framework"
    
    override fun getIcon(): Icon = AllIcons.Nodes.Module // TODO: Use custom icon
    
    override fun createProvider(): FrameworkSupportInModuleProvider {
        return SurfFrameworkSupportProvider()
    }
}
