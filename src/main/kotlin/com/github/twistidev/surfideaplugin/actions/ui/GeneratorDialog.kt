package com.github.twistidev.surfideaplugin.actions.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

/**
 * Dialog for collecting generator parameters
 * Uses Swing with Kotlin UI DSL (preparing for future Compose migration)
 */
class GeneratorDialog(
    project: Project,
    title: String,
    private val defaultClassName: String,
    private val targetDirectory: PsiDirectory
) : DialogWrapper(project) {
    
    var className: String = defaultClassName
        private set
    var packageName: String = extractPackageName()
        private set
    
    init {
        this.title = title
        init()
    }
    
    override fun createCenterPanel(): JComponent {
        return panel {
            row("Class Name:") {
                textField()
                    .text(defaultClassName)
                    .focused()
                    .onChanged { className = it.text }
            }
            row("Package:") {
                textField()
                    .text(packageName)
                    .onChanged { packageName = it.text }
            }
        }
    }
    
    private fun extractPackageName(): String {
        // Try to extract package from directory structure
        var current: PsiDirectory? = targetDirectory
        val parts = mutableListOf<String>()
        
        while (current != null) {
            val name = current.name
            if (name == "kotlin" || name == "java") break
            parts.add(0, name)
            current = current.parent
        }
        
        return parts.joinToString(".")
    }
}
