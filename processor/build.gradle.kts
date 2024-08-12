plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "ru.otus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":annotation"))
    implementation(libs.google.ksp.api)
    implementation(libs.kotlinpoet)
}
