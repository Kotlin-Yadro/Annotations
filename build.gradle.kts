plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.google.ksp).apply(false)
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradlePlugin)
    }
}
