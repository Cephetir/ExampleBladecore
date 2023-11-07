import dev.architectury.pack200.java.Pack200Adapter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    java
    idea
}

version = "1.0"
group = "me.cephetir"

base {
    archivesName.set("ExampleBladecore")
}

loom {
    silentMojangMappingsLicense()
    launchConfigs {
        getByName("client") {
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            property("legacy.debugClassLoading", "true")
            property("legacy.debugClassLoadingSave", "true")
            property("legacy.debugClassLoadingFiner", "true")
            arg("--tweakClass", "me.cephetir.bladecore.loader.BladeCoreTweaker")
            arg("--mixin", "mixins.examplebladecore.json")
        }
    }
    runConfigs {
        getByName("client") {
            isIdeConfigGenerated = true
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.examplebladecore.json")
    }
    mixin {
        defaultRefmapName.set("mixins.examplebladecore.refmap.json")
    }
}

val include: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.ilarea.ru/snapshots")
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    include("me.cephetir:bladecore-loader-1.8.9-forge:1.2")
    implementation("me.cephetir:bladecore-1.8.9-forge:0.0.2-h")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

sourceSets {
    main {
        output.setResourcesDir(file("${layout.buildDirectory.asFile.get().path}/classes/kotlin/main"))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", "1.8.9")

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to "1.8.9"))
        }
        dependsOn(compileJava)
    }
    jar {
        manifest {
            attributes(
                mapOf(
                    "ForceLoadAsMod" to true,
                    "ModSide" to "CLIENT",
                    "ModType" to "FML",
                    "TweakClass" to "me.cephetir.bladecore.loader.BladeCoreTweaker",
                    "TweakOrder" to "0",
                    "MixinConfigs" to "mixins.examplebladecore.json",
                )
            )
        }
        dependsOn(shadowJar)
        enabled = false
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
    }
    shadowJar {
        archiveClassifier.set("dev")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(include)

        exclude(
            "**/LICENSE.md",
            "**/LICENSE.txt",
            "**/LICENSE",
            "**/NOTICE",
            "**/NOTICE.txt",
            "pack.mcmeta",
            "dummyThing",
            "**/module-info.class",
            "META-INF/proguard/**",
            "META-INF/maven/**",
            "META-INF/versions/**",
            "META-INF/com.android.tools/**",
            "fabric.mod.json"
        )
        mergeServiceFiles()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.7"
        }
        kotlinDaemonJvmArguments.set(
            listOf(
                "-Xmx4G",
                "-Dkotlin.enableCacheBuilding=true",
                "-Dkotlin.useParallelTasks=true",
                "-Dkotlin.enableFastIncremental=true"
            )
        )
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}