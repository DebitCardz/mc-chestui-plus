plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.6.10"
}

group = "me.tech"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

// Allow for publishing to Maven local.
publishing {
    publications {
        create<MavenPublication>(project.name.toLowerCase()) {
            groupId = "me.tech"
            artifactId = "chestuiplus"
            version = "1.0.0"

            from(components["java"])
        }
    }
}