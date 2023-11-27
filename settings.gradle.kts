plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "mc-chestui-plus"

if(System.getenv("JITPACK") != null) {
    include("development")

    startParameter.excludedTaskNames += ":development:build"
    startParameter.excludedTaskNames += ":development:compileJava"
    startParameter.excludedTaskNames += ":development:compileKotlin"
}