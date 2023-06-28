plugins {
    kotlin("jvm") version "1.8.21"

    id("maven-publish")
}

val githubActor = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
val githubToken = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")

group = "me.tech"
version = "0.0.7"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    withJavadocJar()
    withSourcesJar()
}

// Allow for publishing to Maven local.
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DebitCardz/mc-chestui-plus")
            credentials {
                username = githubActor
                password = githubToken
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
