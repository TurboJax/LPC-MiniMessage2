plugins {
    alias(libs.plugins.forgegradle)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${libs.versions.forge.get()}"))
    annotationProcessor("net.minecraftforge:eventbus-validator:7.0.1")

    compileOnly(project(":api"))
}
