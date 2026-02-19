# surf-idea-plugin

![Build](https://github.com/twisti-dev/surf-idea-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Adjust the [pluginGroup](./gradle.properties) and [pluginName](./gradle.properties), as well as the [id](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [ ] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `MARKETPLACE_ID` in the above README badges. You can obtain it once the plugin is published to JetBrains Marketplace.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.
- [ ] Configure the [CODECOV_TOKEN](https://docs.codecov.com/docs/quick-start) secret for automated test coverage reports on PRs

<!-- Plugin description -->
IntelliJ IDEA plugin for Surf framework - a Kotlin-based Minecraft server framework.

Provides intelligent code assistance, inspections, and code generation for Surf-based projects targeting Paper, Velocity, and other Minecraft server platforms.
<!-- Plugin description end -->

## Features

- **Feature Detection**: Automatically detects Surf dependencies in your Gradle/Maven projects
- **Framework Support**: Integrated framework support for Surf modules
- **Code Generation**: Generate Redis services, event handlers, and more with template-based wizards
- **Smart Inspections**: Context-aware inspections for Surf-specific code patterns
- **Entry Points**: Marks Surf-annotated elements as used to prevent false "unused" warnings

## Architecture

### Core Components

#### 1. Feature Model (`model` package)
- **SurfFeature**: Represents Surf framework features (surf-api, surf-redis, surf-database, etc.)
- **SurfPlatform**: Enum of supported Minecraft server platforms (Paper, Velocity, Spigot, etc.)
- **SurfModuleKind**: Types of Surf modules (API, Integration, Platform-Specific, etc.)
- **SurfDependencyGraph**: Manages feature dependencies and validates configurations
- **SurfFeatureRegistry**: Central registry of all known Surf features

#### 2. Detection Layer (`detection` package)
- **SurfFeatureDetector**: Interface for detecting enabled features
- **GradleKotlinDslDetector**: Detects features from build.gradle.kts files
- **ClasspathDetector**: Detects features from classpath/PSI analysis
- **CompositeFeatureDetector**: Combines multiple detection strategies

#### 3. Settings & Context (`settings` package)
- **SurfProjectSettingsService**: Persistent storage of enabled features per project/module
- **SurfModuleContext**: Provides feature context for a specific module

#### 4. Framework Support (`framework` package)
- **SurfFrameworkType**: Defines the Surf framework type for IntelliJ
- **SurfFrameworkSupportProvider**: Provides framework support configuration UI

#### 5. Inspections (`inspections` package)
- **SurfInspection**: Base class for all Surf inspections
- **RedisServiceInspection**: Example inspection for Redis service conventions
- Context-aware inspections that only run when relevant features are enabled

#### 6. Code Generation (`generator` and `actions` packages)
- **SurfCodeGenerator**: Interface for code generators
- **BaseSurfCodeGenerator**: Base implementation with file creation and formatting
- **RedisServiceGenerator**: Generates Redis service classes
- **EventHandlerGenerator**: Generates event handler classes
- **SurfGeneratorAction**: Base action class with feature-based visibility
- **GeneratorDialog**: UI for collecting generation parameters

#### 7. Entry Points (`entrypoints` package)
- **SurfImplicitUsageProvider**: Marks annotated elements as entry points
- Prevents false "unused" warnings for framework-managed code

#### 8. Project Wizard (`wizard` package)
- **SurfProjectTemplateModel**: Model for project template configuration
- **SurfProjectWizardProvider**: Interface for project wizard (stub implementation)
- **GradleBuildFileGenerator**: Generates Gradle build files for new projects

### Package Structure

```
com.github.twistidev.surfideaplugin/
├── model/              # Domain models and feature registry
├── detection/          # Feature detection from build files
├── settings/           # Project settings and module context
├── framework/          # Framework support integration
├── inspections/        # Code inspections
├── actions/            # IDE actions
│   └── ui/            # Action dialogs and UI
├── generator/          # Code generators
├── entrypoints/        # Entry point providers
└── wizard/             # Project wizard (stubs)
```

## Developer Guide

### How to Add a New Surf Feature

1. Register the feature in `SurfFeatureRegistry`:
```kotlin
register(SurfFeatureImpl(
    id = "surf-messaging",
    displayName = "Surf Messaging",
    description = "Messaging integration for Surf",
    dependsOn = listOf("surf-api"),
    moduleKind = SurfModuleKind.INTEGRATION,
    gradleCoordinates = "com.surf:surf-messaging:+"
))
```

2. Update detectors to recognize the feature:
   - Add detection logic in `GradleKotlinDslDetector`
   - Add classpath detection in `ClasspathDetector`

3. Add feature-specific functionality:
   - Create inspections that require the feature
   - Add code generators for the feature
   - Add actions that depend on the feature

### How to Add a New Inspection

1. Create a new inspection class extending `SurfInspection`:
```kotlin
class MyCustomInspection : SurfInspection() {
    override fun getRequiredFeatures(): List<String> = listOf("surf-api")
    
    override fun buildKotlinVisitor(holder: ProblemsHolder, context: SurfModuleContext): KtVisitorVoid {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                // Inspection logic here
            }
        }
    }
    
    override fun getDisplayName(): String = "My Custom Check"
    override fun getGroupDisplayName(): String = "Surf Framework"
    override fun getShortName(): String = "SurfMyCustomCheck"
}
```

2. Register in `plugin.xml`:
```xml
<localInspection 
    language="kotlin" 
    groupName="Surf Framework"
    displayName="My Custom Check"
    enabledByDefault="true"
    level="WARNING"
    implementationClass="com.github.twistidev.surfideaplugin.inspections.MyCustomInspection"/>
```

### How to Add a New Code Generator

1. Create a generator class extending `BaseSurfCodeGenerator`:
```kotlin
class MyServiceGenerator : BaseSurfCodeGenerator() {
    override fun generateFileContent(params: GeneratorParams): String {
        return """
            package ${params.packageName}
            
            class ${params.className} {
                // Generated code here
            }
        """.trimIndent()
    }
}
```

2. Create an action class:
```kotlin
class GenerateMyServiceAction : SurfGeneratorAction() {
    private val generator = MyServiceGenerator()
    
    override fun getRequiredFeatures(): List<String> = listOf("surf-api")
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val targetDir = getTargetDirectory(e) ?: return
        
        val dialog = GeneratorDialog(project, "Generate My Service", "MyService", targetDir)
        if (dialog.showAndGet()) {
            val params = GeneratorParams(dialog.className, dialog.packageName)
            generator.generate(project, targetDir, params)?.navigate(true)
        }
    }
}
```

3. Register in `plugin.xml`:
```xml
<action id="Surf.GenerateMyService" 
        class="com.github.twistidev.surfideaplugin.actions.GenerateMyServiceAction"
        text="My Service"
        description="Generate a My Service class">
    <add-to-group group-id="Surf.GenerateGroup" anchor="last"/>
</action>
```

### Running Tests

```bash
./gradlew test
```

### Building the Plugin

```bash
./gradlew buildPlugin
```

The plugin will be built to `build/distributions/`.

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "surf-idea-plugin"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/twisti-dev/surf-idea-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
