package com.github.twistidev.surfideaplugin.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.psi.KtVisitorVoid
import com.github.twistidev.surfideaplugin.settings.SurfModuleContext

/**
 * Base class for Surf-related Kotlin inspections
 */
abstract class SurfInspection : LocalInspectionTool() {
    
    /**
     * Check if the inspection should run for the given element's module
     * Override to restrict inspection to modules with specific Surf features
     */
    protected open fun isApplicableToModule(context: SurfModuleContext): Boolean = true
    
    /**
     * Get required Surf features for this inspection
     * Override to restrict inspection to modules with specific features
     */
    protected open fun getRequiredFeatures(): List<String> = emptyList()
    
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val element = holder.file
        val module = ModuleUtilCore.findModuleForPsiElement(element)
        
        // Only run inspection if module has Surf support
        if (module != null) {
            val context = SurfModuleContext.forModule(module)
            
            // Check if required features are present
            val hasRequiredFeatures = getRequiredFeatures().all { context.hasFeature(it) }
            
            if (isApplicableToModule(context) && hasRequiredFeatures) {
                return buildKotlinVisitor(holder, context)
            }
        }
        
        return PsiElementVisitor.EMPTY_VISITOR
    }
    
    /**
     * Build the Kotlin-specific visitor for this inspection
     */
    protected abstract fun buildKotlinVisitor(holder: ProblemsHolder, context: SurfModuleContext): KtVisitorVoid
}
