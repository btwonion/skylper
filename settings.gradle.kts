import dev.kikugie.stonecutter.StonecutterSettings

rootProject.name = "skylper"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
    }

    plugins {
        kotlin("jvm") version "2.0.20"
        id("com.google.devtools.ksp") version "2.0.20-1.0.24"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.4+"
}

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0-RC")
    }
}

extensions.configure<StonecutterSettings> {
    kotlinController = true
    centralScript = "build.gradle.kts"
    shared {
        versions("1.21", "1.20.4", "1.20.6")
        vcsVersion = "1.21"
    }
    create(rootProject)
}

include(":annotation-processor")