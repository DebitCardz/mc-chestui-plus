plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":common"))
    compileOnly(libs.minestom)
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}