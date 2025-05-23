// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies{
        classpath (libs.secrets.gradle.plugin)

    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.library) apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version ("1.9.23")
}