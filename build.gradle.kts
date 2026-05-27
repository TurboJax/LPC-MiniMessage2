plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
}

dependencies {
    implementation(project(":api"))
    implementation(project(":bukkit"))
    implementation(project(":paper"))
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    group = "de.ayont"
    version = "3.8.0"

    repositories {
        mavenCentral()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")

        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

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
}

subprojects {
    apply(plugin = "java-library")

    dependencies {
        compileOnly(rootProject.libs.luckperms)
        compileOnly(rootProject.libs.papi)
        implementation(rootProject.libs.adventure)
        implementation(rootProject.libs.adventure.bukkit)
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

    tasks {
        withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.release.set(targetJavaVersion)
        }
    }
}
