package dev.gradle.plugin

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.escapeIfNeeded
import java.io.File
import javax.inject.Inject

class TelegramServiceImpl @Inject constructor(
    private val httpClient: HttpClient
) : UploaderService {
    override suspend fun uploadAPK(apk: File, chatId: String, token: String): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendDocument") {
            parameter("chat_id", chatId)

            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "document",
                            value = apk.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentDisposition, "filename=${apk.name.escapeIfNeeded()}")
                            }
                        )
                    }
                )
            )
        }
    }

    override suspend fun sendReportMessage(message: String, chatId: String, token: String): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendMessage") {
            parameter(TEXT, message)
            parameter(CHAT_ID, chatId)
        }
    }

    companion object {
        private const val BASE_URL = "https://api.telegram.org"
        private const val TEXT = "text"
        private const val CHAT_ID = "chat_id"
    }
}