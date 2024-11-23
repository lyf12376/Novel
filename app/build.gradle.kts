plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.test.novel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.novel"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.jsoup) // Jsoup
    implementation (libs.okhttp)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.sharp.retrofit)
    implementation(libs.logging.okhttp)

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //di
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //serialization
    implementation(libs.kotlinx.serialization.json)
    //navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    //glide
    implementation(libs.glide)
    //splashScreen
    implementation(libs.androidx.core.splashscreen)

}