// Top-level Gradle build file

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Navigation Safe Args plugin classpath
        val navVersion = "2.7.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

plugins {
    // Plugin versions come from libs.versions.toml
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}
