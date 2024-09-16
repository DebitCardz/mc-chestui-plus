//import xyz.jpenilla.runpaper.task.RunServer

plugins {
    kotlin("jvm") version "2.0.10"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
//
//    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
//    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "me.tech"
version = rootProject.version

repositories {
    mavenCentral()
    mavenLocal()

//    maven("https://papermc.io/repo/repository/maven-public/")
}


dependencies {
    implementation(libs.minestom)
//    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation(project(":minestom"))
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

//tasks.jar {
//    enabled = false
//}
//
//tasks.withType<RunServer> {
//    minecraftVersion("1.20.4")
//}
//
//bukkit {
//    name = "ui-development"
//    apiVersion = "1.20"
//    version = "${project.version}"
//    authors = listOf("Tech")
//    main = "DevelopmentPlugin"
//
//    commands {
//        register("openui")
//    }
//}