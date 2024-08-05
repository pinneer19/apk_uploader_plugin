package dev.gradle.plugin

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register

class ApkUploaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class)
            ?: throw GradleException(ANDROID_NOT_FOUND)

        val extension = project.extensions.create(PLUGIN_EXTENSION, TelegramUploaderExtension::class)
        val uploaderService: UploaderService = TelegramServiceImpl(httpClient = HttpClient(OkHttp))

        androidComponents.onVariants { variant: Variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)

            project.tasks.register("${TASK_NAME}_${variant.name}", UploadTask::class, uploaderService)
                .configure {
                    apkDirectory.set(artifacts)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
        }
    }

    companion object {
        private const val ANDROID_NOT_FOUND = "Android files not found"
        private const val PLUGIN_EXTENSION = "apkUploader"
        private const val TASK_NAME = "tgUploadAPK_forVariant"
    }
}