package com.github.twistidev.surfideaplugin.model

/**
 * Types of Surf modules
 */
enum class SurfModuleKind(val displayName: String) {
    API("API Module"),
    PLATFORM_SPECIFIC("Platform-Specific Module"),
    INTEGRATION("Integration Module"),
    PLUGIN("Plugin Module"),
    COMMON("Common/Shared Module");
    
    companion object {
        fun fromString(value: String): SurfModuleKind? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
