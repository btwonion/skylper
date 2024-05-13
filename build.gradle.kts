@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("fabric-loom") version "1.6-SNAPSHOT"

    id("me.modmuss50.mod-publish-plugin") version "0.5.+"

    `maven-publish`
    signing
}

val beta: Int? = 17
val featureVersion = "1.0.0${if (beta != null) "-beta$beta" else ""}"
val mcVersion = property("mcVersion")!!.toString()
val mcVersionRange = property("mcVersionRange")!!.toString()
version = "$featureVersion-$mcVersion"

group = "dev.nyon"
val authors = listOf("btwonion")
val githubRepo = "btwonion/skylper"

base {
    archivesName.set("skylper")
}

loom {
    if (stonecutter.current.isActive) {
        runConfigs.all {
            ideConfigGenerated(true)
            runDir("../../run")
        }

        rootProject.tasks.register("runActive") {
            group = "mod"

            dependsOn(tasks.named("runClient"))
        }
    }

    mixin { useLegacyMixinAp = false }

    accessWidenerPath = file("../../src/main/resources/skylper.accesswidener")
}

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

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
    exclude(group = "com.mojang")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
        officialMojangMappings()
    })

    implementation("org.vineflower:vineflower:1.10.1")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fapi")!!}")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.20+kotlin.1.9.24")

    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")!!}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modMenu")!!}")

    val runtimeTestMods = property("runtimeTestMods")!!.toString().split(',').map { it.split(':') }
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

val javaVersion = property("javaVer")!!.toString()
tasks {
    processResources {
        val modId = "skylper"
        val modName = "skylper"
        val modDescription = "Utility mod for Hypixel Skyblock"

        val props = mapOf(
            "id" to modId,
            "name" to modName,
            "description" to modDescription,
            "version" to project.version,
            "github" to githubRepo,
            "mc" to mcVersionRange
        )

        props.forEach(inputs::property)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }


    register("releaseMod") {
        group = "publishing"

        dependsOn("publishMods")
        dependsOn("publish")
    }

    withType<JavaCompile> {
        options.release = javaVersion.toInt()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }
}

val changelogText = buildString {
    append("# v${project.version}\n")
    file("../../${if (beta != null) "beta-" else ""}changelog.md").readText().also { append(it) }
}

val supportedMcVersions: List<String> =
    property("supportedMcVersions")!!.toString().split(',').map(String::trim).filter(String::isNotEmpty)
publishMods {
    displayName = "v${project.version}"
    file = tasks.remapJar.get().archiveFile
    changelog = changelogText
    type = if (beta != null) BETA else STABLE
    modLoaders.addAll("fabric", "quilt")

    modrinth {
        projectId = "MXwU9ODv"
        accessToken = providers.environmentVariable("MODRINTH_API_KEY")
        minecraftVersions.addAll(supportedMcVersions)

        requires { slug = "fabric-api" }
        requires { slug = "yacl" }
        requires { slug = "fabric-language-kotlin" }
        optional { slug = "modmenu" }
    }

    github {
        repository = githubRepo
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        commitish = "master"
    }

    discord {
        webhookUrl = providers.environmentVariable("DISCORD_WEBHOOK")
        username = "Release Notifier"
        content = "# A new version of Skylper released!\n$changelogText"
    }
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials {
                username = providers.environmentVariable("NYON_USERNAME").orNull
                password = providers.environmentVariable("NYON_PASSWORD").orNull
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

    javaVersion.toInt().let { JavaVersion.values()[it - 1] }.let {
            sourceCompatibility = it
            targetCompatibility = it
        }
}

/*
signing {
    sign(publishing.publications)
}
 */
