import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.20-1.0.24")
}

tasks {
    withType<JavaCompile> {
        options.release = 17
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
}