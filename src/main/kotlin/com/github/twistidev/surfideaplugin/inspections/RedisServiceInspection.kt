package com.github.twistidev.surfideaplugin.inspections

import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtVisitorVoid
import com.github.twistidev.surfideaplugin.settings.SurfModuleContext

/**
 * Sample inspection for Redis service classes
 * Only runs in modules with surf-redis feature enabled
 */
class RedisServiceInspection : SurfInspection() {
    
    override fun getRequiredFeatures(): List<String> = listOf("surf-redis")
    
    override fun buildKotlinVisitor(holder: ProblemsHolder, context: SurfModuleContext): KtVisitorVoid {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                super.visitClass(klass)
                
                // TODO: Add actual inspection logic
                // Example: Check if Redis service classes follow naming conventions
                // Example: Check if they implement required interfaces
                // Example: Check for proper annotation usage
                
                // For now, this is just a placeholder
            }
        }
    }
    
    override fun getDisplayName(): String = "Redis Service Conventions"
    
    override fun getGroupDisplayName(): String = "Surf Framework"
    
    override fun getShortName(): String = "SurfRedisServiceConventions"
}
