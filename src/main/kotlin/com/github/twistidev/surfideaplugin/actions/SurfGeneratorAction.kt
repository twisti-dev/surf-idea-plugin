package com.github.twistidev.surfideaplugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiDirectory
import com.github.twistidev.surfideaplugin.settings.SurfModuleContext

/**
 * Base class for Surf code generation actions
 */
abstract class SurfGeneratorAction : AnAction() {
    
    /**
     * Get required Surf features for this action
     */
    protected abstract fun getRequiredFeatures(): List<String>
    
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
    
    override fun update(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val module = psiFile?.let { ModuleUtilCore.findModuleForPsiElement(it) }
        
        val enabled = if (module != null) {
            val context = SurfModuleContext.forModule(module)
            // Check if all required features are present
            getRequiredFeatures().all { context.hasFeature(it) }
        } else {
            false
        }
        
        e.presentation.isEnabledAndVisible = enabled
    }
    
    /**
     * Get the target directory for file generation
     */
    protected fun getTargetDirectory(e: AnActionEvent): PsiDirectory? {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        return psiFile?.containingDirectory
    }
}
