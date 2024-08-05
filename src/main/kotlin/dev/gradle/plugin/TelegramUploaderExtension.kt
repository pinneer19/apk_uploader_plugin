package dev.gradle.plugin

import org.gradle.api.provider.Property

interface TelegramUploaderExtension {
    val chatId: Property<String>
    val token: Property<String>
}