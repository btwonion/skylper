@file:Suppress("SpellCheckingInspection")

import masecla.modrinth4j.model.version.ProjectVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("fabric-loom") version "1.6-SNAPSHOT"

    id("com.modrinth.minotaur") version "2.8.7"
    id("com.github.breadmoirai.github-release") version "2.5.2"

    `maven-publish`
    signing
}

group = "dev.nyon"
val beta: Int? = 13
val majorVersion = "1.0.0${if (beta != null) "-beta$beta" else ""}"
val mcVersion = "1.20.6"
version = "$majorVersion-$mcVersion"
val authors = listOf("btwonion")
val githubRepo = "btwonion/skylper"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://maven.parchmentmc.org")
    maven("https://repo.nyon.dev/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.isxander.dev/snapshots")
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

val runtimeTestMods =
    mapOf(
        "auth-me" to "8.0.0+1.20.5", // AuthMe by axieum for authentication in dev environment
        "cloth-config" to "14.0.126+fabric", // ClothConfig by shedaniel as dependency for AuthMe
        "sodium" to "mc1.20.6-0.5.8" // Sodium by jellyquid3 for performance
    )

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
    exclude(group = "com.mojang")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(
        loom.layered {
            parchment("org.parchmentmc.data:parchment-1.20.6:2024.05.01@zip")
            officialMojangMappings()
        }
    )

    implementation("org.vineflower:vineflower:1.10.1")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.97.8+$mcVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.20+kotlin.1.9.24")

    modImplementation("dev.isxander:yet-another-config-lib:3.4.2+1.20.5-fabric")
    modImplementation("com.terraformersmc:modmenu:10.0.0-beta.1")

    runtimeTestMods.forEach { (projectId, versionId) ->
        modRuntimeOnly("maven.modrinth:$projectId:$versionId")
    }

    include(modImplementation("dev.nyon:konfig:2.0.1-1.20.4")!!)

    val ktorVersion = "2.3.11"
    include(implementation("io.ktor:ktor-client-core:$ktorVersion")!!)
    transitiveInclude(implementation("io.ktor:ktor-client-cio:$ktorVersion")!!)
    transitiveInclude(implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")!!)
    transitiveInclude(implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")!!)

    testImplementation(kotlin("test"))

    transitiveInclude.resolvedConfiguration.resolvedArtifacts.forEach {
        include(it.moduleVersion.id.toString())
    }
}

tasks {
    processResources {
        val modId = rootProject.name
        val modName = rootProject.name
        val modDescription = "Utility mod for Hypixel Skyblock"

        inputs.property("id", modId)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubRepo)

        filesMatching("fabric.mod.json") {
            expand(
                "id" to modId,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubRepo
            )
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("modrinthSyncBody")
        dependsOn("modrinth")
        dependsOn("githubRelease")
        dependsOn("publish")
    }

    withType<JavaCompile> {
        options.release.set(21)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }

    withType<Javadoc> {
        options {
            (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
        }
    }

    loom {
        accessWidenerPath = file("src/main/resources/skylper.accesswidener")
    }
}

val changelogText =
    buildString {
        append("# v${project.version}\n")
        file("${if (beta != null) "beta-" else ""}changelog.md").readText().also { append(it) }
    }

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("MXwU9ODv")
    versionNumber.set("${project.version}")
    versionType.set(if (beta != null) ProjectVersion.VersionType.BETA.name else ProjectVersion.VersionType.BETA.name)
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(mcVersion))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
        required.project("yacl")
        optional.project("modmenu")
    }
    changelog.set(changelogText)
    syncBodyFrom.set(file("readme.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner = split[0]
    repo = split[1]
    tagName = "v${project.version}"
    body = changelogText
    overwrite = true
    releaseAssets(tasks["remapJar"].outputs.files)
    targetCommitish = "master"
    prerelease = beta != null
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.nyon"
            artifactId = "skylper"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    sign(publishing.publications)
}
