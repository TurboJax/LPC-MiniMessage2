pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LPC-Minimessage2"

include("api", "bungeecord", "bukkit", "bukkit:paper", "bukkit:spigot", "fabric", "forge", "neoforge", "sponge", "velocity")
