plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.souhoola.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.souhoola.newsapp"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    secrets {
        propertiesFileName = "secrets.properties"
        ignoreList.add("keyToIgnore")
        ignoreList.add("sdk.*")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "NEWS_API_KEY", "\"${project.findProperty("RELEASE_NEWS_API_KEY") ?: project.findProperty("NEWS_API_KEY") ?: ""}\"")
            buildConfigField("String", "NEWS_API_BASE_URL", "\"${project.findProperty("RELEASE_NEWS_API_BASE_URL") ?: project.findProperty("NEWS_API_BASE_URL") ?: "https://newsapi.org/v2/"}\"")

            isDebuggable = false
        }

        debug {
            isMinifyEnabled = false
            isDebuggable = true

            // API key configuration for debug
            buildConfigField("String", "NEWS_API_KEY", "\"${project.findProperty("DEBUG_NEWS_API_KEY") ?: project.findProperty("NEWS_API_KEY") ?: ""}\"")
            buildConfigField("String", "NEWS_API_BASE_URL", "\"${project.findProperty("DEBUG_NEWS_API_BASE_URL") ?: project.findProperty("NEWS_API_BASE_URL") ?: "https://newsapi.org/v2/"}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Your existing dependencies...
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Ktor Client dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Chucker for network inspection
    debugImplementation(libs.chucker.library)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation (libs.androidx.hilt.navigation.compose)


    // Paging3
    implementation (libs.androidx.paging.compose)
    implementation (libs.androidx.paging.runtime)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation (libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.compose.material3.pullrefresh)
    implementation (libs.androidx.material.icons.extended)


    implementation(libs.kotlinx.serialization.core)
}

kapt {
    correctErrorTypes = true
}