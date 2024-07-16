plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

//    // add google service plugin for firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hhoangphuoc.diarybuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hhoangphuoc.diarybuddy"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    dependencies {
        implementation(libs.feature.delivery)
    }

    //android core and lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //viewmodel
    implementation(libs.androidx.runtime.livedata)

    //compose for app ui
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    //navigation-------------------------------------------------------------
    // for Java
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    // for Kotlin
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Feature module Support
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    // Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)


    // Material Design
    implementation(libs.androidx.material3)
    //icons
    implementation(libs.androidx.material.icons.extended)

    //Glance for app widgets
    implementation(libs.androidx.glance.appwidget)

    // Firebase
    // TODO: Add the dependencies for Firebase products you want to use
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
//    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.storage)

    //wearable dependencies
    implementation(libs.play.services.wearable)

    //speech dependencies
    implementation(libs.google.cloud.speech)

    //testing packages
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}