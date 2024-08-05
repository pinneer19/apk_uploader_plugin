dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.version.toml"))
        }
    }
}
rootProject.name = "apk_uploader_plugin"