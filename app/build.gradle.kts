import java.io.FileInputStream
import java.sql.Timestamp
import java.time.ZonedDateTime
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    //hilt plugin
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gms)
    //Apply the App Distribution Gradle plugin
//    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.firebase.crashlytics)
//    id("com.google.firebase.appdistribution")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.segrets.gradle)
}

//release version
val major = 2
val minor = 2
val patch = 0
val timestamp: Int =
    Timestamp.from(ZonedDateTime.now().toInstant())
        .toInstant()
        .toEpochMilli().toInt()

// KeyStore
val keystoreProperties = Properties().apply {
    load(FileInputStream(rootProject.file("keystore.properties")))
}

android {
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig {
        applicationId = "com.application.material.bookmarkswallet.app"

        minSdk = 30
        versionCode = (major * 10000 + minor * 100 + patch)
        versionName = "$major.$minor.$patch"

        buildConfigField(
            "String",
            "API_URLMETA_BASE_URL",
            "\"https://api.urlmeta.org\""
        )
        buildConfigField(
            "String",
            "API_URLMETA_USER",
            "\"da-doz@hotmail.it\""
        )
        buildConfigField(
            "String",
            "API_URLMETA_PWD",
            "\"INuNPW7T5eZdYe6EFyw8\""
        )
        buildConfigField(
            "String",
            "JSONLINK_BASE_URL",
            "\"https://jsonlink.io/api/\""
        )
        buildConfigField(
            "String",
            "JSONLINK_API_KEY",
            "\"pk_d1a3c2c5051ccc8e5970e37a1284962f7236632c\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("releaseConfig") {
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders.let {
                it["appIcon"] = "@mipmap/ic_launcher_debug"
                it["appIconRound"] = "@mipmap/ic_launcher_debug_round"
            }
        }

        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("releaseConfig")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompiler.get()
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
        buildConfig = true
    }
    namespace = "com.application.material.bookmarkswallet.app"
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

secrets {
    // Change the properties file from the default "local.properties" in your root project
    // to another properties file in your root project.
    propertiesFileName = "secrets.properties"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //android
    implementation(libs.core.ktx)

    //lifecycle
    implementation(libs.lifecycle.runtime.ktx)

    //playservices
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    //compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    runtimeOnly(libs.work.runtime.ktx)
    //maps
    implementation(libs.maps.compose)
    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation(libs.maps.compose.utils)
    implementation("androidx.compose.material3:material3-android:1.4.0-alpha14")
    implementation(
         "androidx.compose.material:material-icons-extended:1.7.8"
    )

    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation(libs.maps.compose.widgets)

    //material
    implementation(libs.material)
    implementation(libs.viewpager2)

    //navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.fragment.ui)
    // Jetpack Compose Integration
    implementation(libs.navigation.compose)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

//    implementation(libs.rxjava)
//    implementation(libs.rxandroid)

    //timber log
    implementation(libs.timber)

    //retrofit
    implementation(libs.bundles.retrofit)
//    implementation(libs.adapter.rxjava3)
    implementation(libs.converter.gson)

    //moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    //okHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    //hilt
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    //room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    //Destinations Library
    implementation(libs.destinations.animations.core)
    ksp(libs.destinations.ksp)

    //Jetpack Datastore
    implementation(libs.datastore.preferences)

    //GOOGLE ACCOMPANIST
    implementation(libs.bundles.accompanist)

    //KOTLINX DATETIME
    implementation(libs.kotlinxDatetime)

    //SERIALIZATION
    implementation(libs.kotlinxSerialization)
    //SERIALIZATION
    implementation(libs.gson)

    //LOTTIE
    implementation(libs.lottie)

    //firebase auth
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)

    //coil
    implementation(libs.coil.compose)
    //glide
//    implementation(libs.glide)

    implementation(libs.generativeai)

    //test
    testImplementation(libs.junit)
//    testImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.bundles.androidTest)
}
