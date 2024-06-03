import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val RELEASE_KEY_ALIAS: String by project
val RELEASE_STORE_PASSWORD: String by project
val RELEASE_KEY_PASSWORD: String by project
val PLAY_KEY_ALIAS: String by project
val PLAY_STORE_PASSWORD: String by project
val PLAY_KEY_PASSWORD: String by project

android {
    namespace = "uz.mahmudxon.currency"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.mahmudxon.currency"
        minSdk = 24
        targetSdk = 34
        versionCode = 2005
        versionName = "2.0.5"
        archivesName.set("Valyuta Kurslari V$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug")
        {
            storeFile = file("config/release.keystore")
            keyAlias = RELEASE_KEY_ALIAS
            storePassword = RELEASE_STORE_PASSWORD
            keyPassword = RELEASE_KEY_PASSWORD
        }

        create("release")
        {
            storeFile = file("config/release.keystore")
            keyAlias = RELEASE_KEY_ALIAS
            storePassword = RELEASE_STORE_PASSWORD
            keyPassword = RELEASE_KEY_PASSWORD
        }
        create("playStore")
        {
            storeFile = file("config/playstore.keystore")
            keyAlias = PLAY_KEY_ALIAS
            storePassword = PLAY_STORE_PASSWORD
            keyPassword = PLAY_KEY_PASSWORD
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            buildConfigField(
                "String",
                "AppDownloadUrl",
                "\"https://mahmudxon.uz/\""
            )
        }
        release {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "AppDownloadUrl",
                "\"https://play.google.com/store/apps/details?id=uz.mahmudxon.currency\""
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("playStoreRelease") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("playStore")
        }

        create("huaweiRelease") {
            initWith(getByName("release"))
            buildConfigField(
                "String",
                "AppDownloadUrl",
                "\"https://appgallery.huawei.com/app/C110379329\""
            )
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    android.sourceSets.configureEach {
        kotlin.srcDir("src/$name/kotlin")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
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

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // navigation
    implementation(libs.androidx.hilt.hilt.navigation)
    implementation(libs.androidx.navigation.compose)

    // hilt
    implementation(libs.google.dagger.hilt.android)
    ksp(libs.google.dagger.hilt.compiler)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.content.serialization)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // Chucker
    debugImplementation(libs.chuckerteam.chucker.debug)
    releaseImplementation(libs.chuckerteam.chucker.release)
    "huaweiReleaseImplementation"(libs.chuckerteam.chucker.release)
    "playStoreReleaseImplementation"(libs.chuckerteam.chucker.release)

    // Coil
    implementation(libs.io.coil.compose)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}