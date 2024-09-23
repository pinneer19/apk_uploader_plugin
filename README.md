**Gradle plugin which allows publish Android APK build files to your Telegram bot**

1. Update path in *build.gradle.kts* to publish plugin locally:
    
```
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("YOUR_PATH")
        }
    }
}
  ```

2. Execute gradle task: **publishing** -> **publishToMavenLocal**
3. Apply plugin in your Android project
```
// settings.gradle.kts
pluginManagement {
    repositories {
        mavenLocal() // add this line
    }
}
```
```
// root build.gradle.kts
id("dev.gradle.plugin.apk_uploader_plugin") version "1.0.0" apply false
```
4. After syncing you need specify your telegram bot API token and [chat_id](https://stackoverflow.com/questions/32423837/telegram-bot-how-to-get-a-group-chat-id) like this
```
// build.gradle.kts of plugin applying module
apkUploader {
    chatId.set(YOUR_CHAT_ID)
    token.set(YOUR_API_TOKEN)
}
```
5. Now you can execute gradle tasks to publish apk: tgUploadAPK_forVariant_[some variant]