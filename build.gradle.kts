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
        withJavadocJar()
        withSourcesJar()
    }


    publishing {
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}

//buildscript {
//    dependencies {
//        classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
//    }
//}

//plugins {
//    kotlin("jvm") version "1.9.20"
//
//    `maven-publish`
//}
//
//val githubActor = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
//val githubToken = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
//
//group = "me.tech"
//version = "1.5.6"
//
//repositories {
//    mavenCentral()
//
//    maven("https://repo.papermc.io/repository/maven-public/")
//}
//
//dependencies {
//    implementation(kotlin("stdlib"))
//
//    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
//}
//
//kotlin {
//    jvmToolchain(17)
//}
//
//java {
//    withJavadocJar()
//    withSourcesJar()
//}
//
//// Allow for publishing to Maven local.
//publishing {
//    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/DebitCardz/mc-chestui-plus")
//            credentials {
//                username = githubActor
//                password = githubToken
//            }
//        }
//    }
//
//    publications {
//        register<MavenPublication>("gpr") {
//            from(components["java"])
//        }
//    }
//}
