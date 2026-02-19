package com.github.twistidev.surfideaplugin.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.github.twistidev.surfideaplugin.generator.GeneratorParams
import com.github.twistidev.surfideaplugin.generator.RedisServiceGenerator
import com.github.twistidev.surfideaplugin.actions.ui.GeneratorDialog

/**
 * Action to generate a Redis service class
 */
class GenerateRedisServiceAction : SurfGeneratorAction() {
    
    private val generator = RedisServiceGenerator()
    
    override fun getRequiredFeatures(): List<String> = listOf("surf-redis")
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val targetDir = getTargetDirectory(e) ?: return
        
        // Show dialog to get class name and package
        val dialog = GeneratorDialog(
            project,
            "Generate Redis Service",
            "RedisService",
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
                    "Failed to generate Redis service. File might already exist.",
                    "Generation Failed"
                )
            }
        }
    }
}
