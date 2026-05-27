plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    alias(libs.plugins.run.paper)
}

group = "de.ayont"
version = "3.8.0"

// Getting the minecraft version from the version catalog
var mcVersion = libs.versions.minecraft.get()
mcVersion = mcVersion.substring(0, mcVersion.length - 1)

// Setting up repositories
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

// Adding dependencies
dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.luckperms)
    compileOnly(libs.papi)
    implementation(libs.adventure)
    implementation(libs.adventure.bukkit)
}

// Configuring spotless formatter
spotless {
    format("misc") {
        target("*.gradle.kts", ".gitattributes", ".gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }

    yaml {
        target("**/*.yml", "**/*.yaml")
        prettier()
    }

    java {
        googleJavaFormat("1.35.0")
            .aosp()
            .reflowLongStrings()
            .formatJavadoc(false)
            .reorderImports(false)
            .groupArtifact("com.google.googlejavaformat:google-java-format")
    }
}

val targetJavaVersion = 25
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

// Configuring tasks
tasks {
    runServer {
        minecraftVersion(mcVersion)
    }

    shadowJar {
        archiveFileName.set("LPC-Minimessage2.jar")
        mergeServiceFiles {
            exclude("META-INF/*.DSA", "META-INF/*.RSA")
        }
    }

    processResources {
        val props = mapOf("version" to version, "mcVersion" to mcVersion)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }
}
