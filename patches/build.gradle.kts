group = "app.argusos"

patches {
    about {
        name = "ArgusOS Patches"
        description = "Patches for ArgusOS"
        source = "git@github.com:argusos/argusos-patches.git"
        author = "ArgusOS"
        contact = "patches@argusos.app"
        website = "https://argusos.app"
        license = "GNU General Public License v3.0"
    }
}

dependencies {
    // Required due to smali, or build fails. Can be removed once smali is bumped.
    implementation(libs.guava)

    implementation(libs.apksig)

    // Android API stubs defined here.
    compileOnly(project(":patches:stub"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexplicit-backing-fields",
            "-Xcontext-parameters"
        )
    }
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/argusos/argusos-patches")
            credentials(PasswordCredentials::class)
        }
    }
}

apply(from = "strings-processing.gradle.kts")
