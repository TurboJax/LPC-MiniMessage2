plugins {
    alias(libs.plugins.moddevgradle)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

neoForge {
    version = libs.versions.neoforge.get()
    validateAccessTransformers = true
}

dependencies {
    compileOnly(project(":api"))
}
