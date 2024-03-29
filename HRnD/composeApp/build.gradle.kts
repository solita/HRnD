plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.mockmp)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.compose.bom))
                implementation(libs.compose.ui)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.ktor.client.okhttp)

                implementation(libs.zxing)

                implementation(libs.profiler)
            }
        }

        androidNativeTest{
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.profiler)
            }
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)

            implementation(libs.kotlin.coroutines)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kamel)
            implementation(libs.koin.core)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screen.model)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.transitions)

            implementation(libs.napier)

            implementation(libs.orbit.compose)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlin.immutable.collections)

            implementation(libs.shimmer)
        }
        
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotest.assert)
                implementation(libs.kotlin.coroutines.test)
            }
        }
    }
}

mockmp {
    usesHelper = true
    installWorkaround()
}

android {
    namespace = "fi.solita.hrnd"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "fi.solita.hrnd"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
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
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            resValue("string", "app_name", "Health Rundown")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug") // Uncomment this line to enable signing
        }

        getByName("debug"){
            isDebuggable = true
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Health Debug")
        }

        create("demo") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            isDebuggable = false
            isDefault = false

            signingConfig = signingConfigs.getByName("debug")

            applicationIdSuffix = ".demo"
            resValue("string", "app_name", "Health Rundown Demo")
        }

        create("benchmark"){
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true

            applicationIdSuffix = ".benchmark"

            resValue("string", "app_name", "Health Benchmark")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}