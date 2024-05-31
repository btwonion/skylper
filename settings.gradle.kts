import dev.kikugie.stonecutter.gradle.StonecutterSettings

rootProject.name = "skylper"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.parchmentmc.org/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.3.8"
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
        versions("1.20.4", "1.20.6")
        vcsVersion = "1.20.6"
    }
    create(rootProject)
}