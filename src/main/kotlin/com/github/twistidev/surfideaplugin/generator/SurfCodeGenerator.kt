package com.github.twistidev.surfideaplugin.generator

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.psi.KtPsiFactory

/**
 * Interface for code generators
 */
interface SurfCodeGenerator {
    /**
     * Generate code based on the given parameters
     * @param project The current project
     * @param targetDirectory The directory where the file should be created
     * @param params Generator-specific parameters
     * @return The generated file
     */
    fun generate(project: Project, targetDirectory: PsiDirectory, params: GeneratorParams): PsiFile?
}

/**
 * Base class for generator parameters
 */
open class GeneratorParams(
    val className: String,
    val packageName: String
)

/**
 * Base implementation of SurfCodeGenerator
 */
abstract class BaseSurfCodeGenerator : SurfCodeGenerator {
    
    override fun generate(project: Project, targetDirectory: PsiDirectory, params: GeneratorParams): PsiFile? {
        return WriteCommandAction.writeCommandAction(project).compute<PsiFile?, Throwable> {
            val fileName = "${params.className}.kt"
            
            // Check if file already exists
            if (targetDirectory.findFile(fileName) != null) {
                // TODO: Show error dialog
                return@compute null
            }
            
            val fileContent = generateFileContent(params)
            val psiFactory = KtPsiFactory(project)
            val ktFile = psiFactory.createFile(fileName, fileContent)
            
            val addedFile = targetDirectory.add(ktFile) as? PsiFile
            
            // Reformat the file
            addedFile?.let {
                CodeStyleManager.getInstance(project).reformat(it)
            }
            
            addedFile
        }
    }
    
    /**
     * Generate the file content as a string
     */
    protected abstract fun generateFileContent(params: GeneratorParams): String
}
