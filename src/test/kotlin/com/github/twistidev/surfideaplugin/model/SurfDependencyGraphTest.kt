package com.github.twistidev.surfideaplugin.model

import org.junit.Test
import org.junit.Assert.*

/**
 * Tests for SurfDependencyGraph
 */
class SurfDependencyGraphTest {
    
    private val graph = SurfDependencyGraph()
    
    @Test
    fun testResolveDependencies_basicFeature() {
        // surf-api has no dependencies
        val resolved = graph.resolveDependencies(setOf("surf-api"))
        assertEquals(listOf("surf-api"), resolved)
    }
    
    @Test
    fun testResolveDependencies_withDependency() {
        // surf-redis depends on surf-api
        val resolved = graph.resolveDependencies(setOf("surf-redis"))
        assertTrue(resolved.contains("surf-api"))
        assertTrue(resolved.contains("surf-redis"))
        // surf-api should come before surf-redis (topological order)
        assertTrue(resolved.indexOf("surf-api") < resolved.indexOf("surf-redis"))
    }
    
    @Test
    fun testResolveDependencies_multipleFeatures() {
        // Both surf-redis and surf-database depend on surf-api
        val resolved = graph.resolveDependencies(setOf("surf-redis", "surf-database"))
        assertTrue(resolved.contains("surf-api"))
        assertTrue(resolved.contains("surf-redis"))
        assertTrue(resolved.contains("surf-database"))
        // surf-api should come first
        assertEquals("surf-api", resolved[0])
    }
    
    @Test
    fun testValidate_validConfiguration() {
        // All dependencies present
        val errors = graph.validate(setOf("surf-api", "surf-redis"))
        assertTrue(errors.isEmpty())
    }
    
    @Test
    fun testValidate_missingDependency() {
        // surf-redis requires surf-api, but it's not present
        val errors = graph.validate(setOf("surf-redis"))
        assertFalse(errors.isEmpty())
        assertTrue(errors.any { it.contains("surf-api") })
    }
    
    @Test(expected = FeatureNotFoundException::class)
    fun testResolveDependencies_unknownFeature() {
        graph.resolveDependencies(setOf("unknown-feature"))
    }
}
