rootProject.name = "mc-chestui-plus"

if(System.getenv("JITPACK") != null) {
    include("development")
}

startParameter.excludedTaskNames += ":development:build"