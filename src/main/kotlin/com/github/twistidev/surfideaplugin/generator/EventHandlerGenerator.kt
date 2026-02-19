package com.github.twistidev.surfideaplugin.generator

/**
 * Generator for event handler classes
 */
class EventHandlerGenerator : BaseSurfCodeGenerator() {
    
    override fun generateFileContent(params: GeneratorParams): String {
        return buildString {
            appendLine("package ${params.packageName}")
            appendLine()
            appendLine("import com.surf.api.ApiEndpoint")
            appendLine()
            appendLine("/**")
            appendLine(" * Event handler: ${params.className}")
            appendLine(" * TODO: Implement event handling logic")
            appendLine(" */")
            appendLine("@ApiEndpoint")
            appendLine("class ${params.className} {")
            appendLine("    ")
            appendLine("    fun handleEvent(event: Any) {")
            appendLine("        // TODO: Implement event handling logic")
            appendLine("    }")
            appendLine("}")
        }
    }
}
