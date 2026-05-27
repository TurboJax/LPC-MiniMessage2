plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LPC-Minimessage2"

include("api", "bukkit", "paper")
