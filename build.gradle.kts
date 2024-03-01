@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("fabric-loom") version "1.5-SNAPSHOT"

    id("com.modrinth.minotaur") version "2.8.7"
    id("com.github.breadmoirai.github-release") version "2.5.2"

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "1.0.0"
val mcVersion = "1.20.4"
version = "$majorVersion-$mcVersion"
val authors = listOf("btwonion")
val githubRepo = "btwonion/skylper"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://maven.parchmentmc.org")
    maven("https://repo.nyon.dev/releases")
    maven("https://maven.isxander.dev/releases")
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

val runtimeTestMods = mapOf(
    "auth-me" to "8.0.0+1.20.4", // AuthMe by axieum for authentication in dev environment
    "cloth-config" to "13.0.121+fabric", // ClothConfig by shedaniel as dependency for AuthMe
    "sodium" to "mc1.20.4-0.5.8" // Sodium by jellyquid3 for performance
)

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.20.3:2023.12.31@zip")
        officialMojangMappings()
    })

    implementation("org.vineflower:vineflower:1.9.3")
    modImplementation("net.fabricmc:fabric-loader:0.15.6")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.93.1+$mcVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")

    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.3.2+1.20.4")
    modImplementation("com.terraformersmc:modmenu:9.0.0-pre.1")
    modCompileOnly("maven.modrinth:y6DuFGwJ:bKu6Hdms") // Skyblocker by Wohlhabend - place the mod for testing in the mods folder (they have weird includes)

    runtimeTestMods.forEach { (projectId, versionId) ->
        modRuntimeOnly("maven.modrinth:$projectId:$versionId")
    }

    include(modImplementation("dev.nyon:konfig:2.0.0-1.20.4")!!)

    val ktorVersion = "2.3.7"
    include(implementation("io.ktor:ktor-client-core:$ktorVersion")!!)
    
    include(implementation("io.ktor:ktor-client-cio:$ktorVersion")!!)
    include(implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")!!)
    include(implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")!!)

    testImplementation(kotlin("test"))
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
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

val changelogText = buildString {
    append("v${project.version}")
    file("changelog.md").readText().also { append(it) }
}

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("")
    versionNumber.set("${project.version}")
    versionType.set("release")
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
    syncBodyFrom.set(file("README.md").readText())
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
    targetCommitish = "main"
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
}

signing {
    sign(publishing.publications)
}