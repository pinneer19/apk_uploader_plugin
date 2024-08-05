package dev.gradle.plugin

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class UploadTask @Inject constructor(
    private val service: UploaderService
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDirectory: DirectoryProperty

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val token: Property<String>

    @TaskAction
    fun upload() {
        apkDirectory.get().asFile.listFiles()
            ?.filter { it.name.endsWith(APK_EXT) }
            ?.forEach { apk ->
                runBlocking {
                    service.sendReportMessage(BUILD_FINISHED, chatId.get(), token.get()).apply {
                        println("$PLUGIN_REPORT_STATUS $status")
                    }
                }

                runBlocking {
                    service.uploadAPK(apk, chatId.get(), token.get()).apply {
                        println("$PLUGIN_REPORT_STATUS $status")
                    }
                }
            }
    }

    companion object {
        private const val APK_EXT = ".apk"
        private const val BUILD_FINISHED = "Build finished"
        private const val PLUGIN_REPORT_STATUS = "Plugin report status:"
    }
}