plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "app.argusos.extension"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}
