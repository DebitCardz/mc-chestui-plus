plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":common"))
    compileOnly(libs.minestom)
}