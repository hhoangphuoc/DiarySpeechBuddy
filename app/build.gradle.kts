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

    //android core and lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //compose for app ui
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // Material Design
    implementation(libs.androidx.material3)

    //Glance for app widgets
    implementation(libs.androidx.glance.appwidget)

//    // Wearable
//    implementation(libs.androidx.compose.material)
//    implementation(libs.androidx.compose.foundation)
//    implementation(libs.androidx.core.splashscreen)
//
//    //tile and horologist
//    implementation(libs.androidx.tiles)
//    implementation(libs.androidx.tiles.material)
//    implementation(libs.horologist.compose.tools)
//    implementation(libs.horologist.tiles)
//    implementation(libs.androidx.watchface.complications.data.source.ktx)


    // Firebase

    // Import the Firebase BoM
    //noinspection UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")

    //wearable dependencies
    implementation(libs.play.services.wearable)

    //speech dependencies
    implementation(libs.google.cloud.speech)


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    //testing packages
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}