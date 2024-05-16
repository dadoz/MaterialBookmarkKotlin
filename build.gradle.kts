// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
//        classpath(Dependencies.ANDROID_PLUGIN)
//        classpath(Dependencies.KOTLIN_GRADLE_PLUGIN)
//        classpath(Dependencies.GOOGLE_SERVICES)
//        classpath(Dependencies.FIREBASE_PLUGIN)
//        //hilt
//        classpath(Dependencies.HILT_PLUGIN)
    }
}

plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.gms).apply(false)
    alias(libs.plugins.firebase.crashlytics).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.kotlin.kapt).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.android.library).apply(false)
}
