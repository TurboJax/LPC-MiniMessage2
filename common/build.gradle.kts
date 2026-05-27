plugins {
    alias(libs.plugins.run.paper)
}

// Getting the minecraft version from the version catalog
var mcVersion = libs.versions.minecraft.get()
mcVersion = mcVersion.substring(0, mcVersion.length - 1)

// Adding dependencies
dependencies {
    compileOnly(libs.spigot)
    compileOnly(":api")
}

// Configuring tasks
tasks {
    runServer {
        minecraftVersion(mcVersion)
    }

    processResources {
        val props = mapOf("version" to version, "mcVersion" to mcVersion)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
