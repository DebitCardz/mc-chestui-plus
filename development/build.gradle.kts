import xyz.jpenilla.runpaper.task.RunServer

plugins {
    kotlin("jvm") version "1.9.20"

    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "me.tech"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    implementation(rootProject)
}

tasks.withType<RunServer> {
    minecraftVersion("1.20.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

bukkit {
    name = "ui-development"
    apiVersion = "1.20"
    version = "${project.version}"
    authors = listOf("Tech")
    main = "me.tech.development.DevelopmentPlugin"

    commands {
        register("openui")
    }
}