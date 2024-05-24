plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.hyperether.getgoing_kmp.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.hyperether.getgoing"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["mapsApiKey"] = project.findProperty("MAPS_API_KEY") ?: ""
    }
    dataBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.appcompat)
    implementation(libs.coreKtx)
    implementation(libs.maps)
    implementation(libs.location)
    implementation(libs.mpAndroidChart)
    implementation(libs.navigationFragmentKtx)
    implementation(libs.navigationUiKtx)
    implementation(libs.androidToolbox) {
        isTransitive = true
    }
    testImplementation(libs.junit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.espressoCore)
    implementation(libs.lifecycleExtensions)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.circleProgressBar)
    implementation(libs.lifecycleCompiler)
    implementation(libs.lifecycleLivedataKtx)
    implementation(libs.mpAndroidChart)
    implementation(libs.google.maps)
}