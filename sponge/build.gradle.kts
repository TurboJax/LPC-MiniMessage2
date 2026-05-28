repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:12.0.0") {
        exclude(module="configurate-core")
        exclude(module="configurate-core")
        exclude(module="configurate-hocon")
        exclude(module="configurate-gson")
        exclude(module="configurate-yaml")
    }
    compileOnly(project(":api"))
}
