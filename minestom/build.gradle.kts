plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":common"))
    compileOnly(libs.minestom)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}