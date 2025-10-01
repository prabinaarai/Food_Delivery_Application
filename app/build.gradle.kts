plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.foodapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.foodapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary=true

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    // This is the correct fileTree declaration
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))


    implementation ("com.hbb20:ccp:2.5.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation ("com.squareup.retrofit2:retrofit:2.6.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.1")
    implementation ("com.google.android.material:material:1.1.0")
    implementation ("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation ("androidx.navigation:navigation-fragment:2.3.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation ("com.cepheuen.elegant-number-button:lib:1.0.2")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
//    implementation ("com.firebaseui:firebase-ui-database:6.2.1")
    implementation ("com.firebaseui:firebase-ui-database:8.0.1")

    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation ("androidx.navigation:navigation-ui:2.3.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")



    implementation ("com.google.firebase:firebase-analytics:17.4.4")
    implementation ("androidx.appcompat:appcompat:1.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation ("com.google.firebase:firebase-auth:19.3.2")
//    implementation ("com.github.mancj:MaterialSearchBar:0.7.1")

    implementation ("com.google.firebase:firebase-database:19.3.1")
    implementation ("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation ("androidx.navigation:navigation-fragment:2.3.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation ("com.cepheuen.elegant-number-button:lib:1.0.1")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation ("com.firebaseui:firebase-ui-database:6.2.1")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
//    implementation ("com.theartofdev.edmodo:android-image-cropper:2.3.+")
    implementation ("androidx.navigation:navigation-ui:2.3.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("com.hbb20:ccp:2.3.1")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.firebase:firebase-storage:19.1.1")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0")
    implementation ("com.google.android.material:material:1.1.0")
    implementation ("com.google.firebase:firebase-messaging:20.2.3")
    //implementation ("com.google.firebase:firebase-bom:30.1.0")
    implementation ("androidx.browser:browser:1.2.0")

    implementation ("com.android.support:multidex:1.0.3")

//
//    implementation 'com.squareup.retrofit2:retrofit:2.6.1'
//    implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
}
