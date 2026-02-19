package com.github.twistidev.surfideaplugin.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.github.twistidev.surfideaplugin.generator.EventHandlerGenerator
import com.github.twistidev.surfideaplugin.generator.GeneratorParams
import com.github.twistidev.surfideaplugin.actions.ui.GeneratorDialog

/**
 * Action to generate an event handler class
 */
class GenerateEventHandlerAction : SurfGeneratorAction() {
    
    private val generator = EventHandlerGenerator()
    
    override fun getRequiredFeatures(): List<String> = listOf("surf-api")
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val targetDir = getTargetDirectory(e) ?: return
        
        // Show dialog to get class name and package
        val dialog = GeneratorDialog(
            project,
            "Generate Event Handler",
            "EventHandler",
            targetDir
        )
        
        if (dialog.showAndGet()) {
            val params = GeneratorParams(
                className = dialog.className,
                packageName = dialog.packageName
            )
            
            val generatedFile = generator.generate(project, targetDir, params)
            if (generatedFile != null) {
                // Open the generated file
                generatedFile.navigate(true)
            } else {
                Messages.showErrorDialog(
                    project,
                    "Failed to generate event handler. File might already exist.",
                    "Generation Failed"
                )
            }
        }
    }
}
