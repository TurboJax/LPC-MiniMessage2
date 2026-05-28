plugins {
    alias(libs.plugins.loom)
}

// Getting the minecraft version from the version catalog
var mcVersion = libs.versions.minecraft.get()
mcVersion = mcVersion.substring(0, mcVersion.length - 1)

dependencies {
    minecraft("com.mojang:minecraft:${mcVersion}")
    compileOnly(libs.fabric)
    compileOnly(project(":api"))
}
