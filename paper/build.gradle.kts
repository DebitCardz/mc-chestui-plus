plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":common"))
    compileOnly(libs.paper)
}