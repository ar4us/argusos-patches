android.namespace = "app.argusos.extension"

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "app.argusos.extension"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
}
