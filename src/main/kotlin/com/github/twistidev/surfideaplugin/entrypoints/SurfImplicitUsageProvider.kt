package com.github.twistidev.surfideaplugin.entrypoints

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.psiUtil.getAnnotationEntries
import com.github.twistidev.surfideaplugin.settings.SurfProjectSettingsService

/**
 * Marks Surf-annotated elements as implicitly used (not "unused")
 */
class SurfImplicitUsageProvider : ImplicitUsageProvider {
    
    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (element !is KtNamedDeclaration) return false
        
        val project = element.project
        val settings = SurfProjectSettingsService.getInstance(project)
        val entryPointAnnotations = settings.getEntryPointAnnotations()
        
        return hasAnyAnnotation(element, entryPointAnnotations)
    }
    
    override fun isImplicitRead(element: PsiElement): Boolean = false
    
    override fun isImplicitWrite(element: PsiElement): Boolean = false
    
    private fun hasAnyAnnotation(element: KtNamedDeclaration, annotationFqns: List<String>): Boolean {
        val annotations = element.getAnnotationEntries()
        
        for (annotation in annotations) {
            val annotationName = getAnnotationFqn(annotation)
            if (annotationName in annotationFqns) {
                return true
            }
        }
        
        return false
    }
    
    private fun getAnnotationFqn(annotation: KtAnnotationEntry): String? {
        // Try to get fully qualified name
        // This is a simplified version - full implementation would resolve the reference
        val shortName = annotation.shortName?.asString() ?: return null
        
        // For now, we'll check if any configured annotation ends with this short name
        // A more robust implementation would use proper PSI resolution
        return shortName
    }
}
