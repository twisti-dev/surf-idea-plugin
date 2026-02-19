package com.github.twistidev.surfideaplugin.model

/**
 * Supported Minecraft server platforms for Surf features
 */
enum class SurfPlatform(val displayName: String) {
    PAPER("Paper"),
    VELOCITY("Velocity"),
    SPIGOT("Spigot"),
    BUNGEECORD("BungeeCord"),
    SPONGE("Sponge"),
    FABRIC("Fabric"),
    FORGE("Forge");
    
    companion object {
        fun fromString(value: String): SurfPlatform? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
