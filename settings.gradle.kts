plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "mc-chestui-plus"

include("development")

if(System.getenv("JITPACK") != null) {
    listOf("build", "compileJava", "compileKotlin").forEach {
        startParameter.excludedTaskNames += ":development:$it"
    }
}