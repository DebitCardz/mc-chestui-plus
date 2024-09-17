plugins {
    alias(libs.plugins.kotlin)
    java

    `maven-publish`
}

group = "me.tech"
version = "2.0.0"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        compileOnly(rootProject.libs.adventureApi)
    }

    kotlin {
        jvmToolchain(21)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }


    publishing {
        publications {
            register<MavenPublication>("maven") {
                groupId = "${project.group}"
                artifactId = "mc-chestui-plus-${project.name}"
                version = "${project.version}"

                from(components["java"])
            }
        }
    }
}
