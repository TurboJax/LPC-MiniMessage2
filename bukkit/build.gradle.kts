// Getting the minecraft version from the version catalog
var mcVersion = libs.versions.minecraft.get()
mcVersion = mcVersion.substring(0, mcVersion.length - 1)

dependencies {
    compileOnly(libs.spigot)
    compileOnly(project(":api"))
    compileOnly(project(":bukkit:spigot"))
    compileOnly(project(":bukkit:paper"))
}

// Configuring tasks
tasks {
    processResources {
        val props = mapOf("version" to version, "mcVersion" to mcVersion)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
