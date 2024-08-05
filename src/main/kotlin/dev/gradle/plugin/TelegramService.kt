package dev.gradle.plugin

import io.ktor.client.statement.HttpResponse
import java.io.File

interface UploaderService {
    suspend fun uploadAPK(apk: File, chatId: String, token: String): HttpResponse
    suspend fun sendReportMessage(message: String, chatId: String, token: String): HttpResponse
}