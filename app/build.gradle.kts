import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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
    val localProperties = gradleLocalProperties(rootDir, providers)
    val apiKey: String = localProperties.getProperty("GEMINI_API_KEY") ?: ""

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }


        getByName("debug") {
            versionNameSuffix = "-debug"
            applicationIdSuffix= ".debug"
            buildConfigField("String", "GEMINI_API_KEY", "\"${apiKey}\"")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "GEMINI_API_KEY", "\"${apiKey}\"")
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
        buildConfig = true //FIXME: Adding to use Generative AI
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

    //Life Cycles and Coroutines
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Kotlin Coroutines (for asynchronous operations)
    implementation(libs.kotlinx.coroutines.android)


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
//    implementation(libs.androidx.glance.appwidget)

    // Firebase
    // TODO: Add the dependencies for Firebase products you want to use
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.vertexai) //TODO: USING VERTEXAI INSTEAD OF GENERATIVEAI

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)


    //speech dependencies
    implementation(libs.google.cloud.speech)


    // Google AI Studio
    implementation(libs.generativeai.android)

    implementation(libs.support.annotations)
    implementation(libs.generativeai) // For Compose navigation
    
    // Security for encrypted shared preferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")


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