package com.mairwunnx.projectessentials.chat.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    var moderation: Moderation = Moderation()
) {
    @Serializable
    data class Moderation(
        var blockedWords: MutableList<String> = mutableListOf("fuck", "shit"),
        var blockedChars: List<String> = mutableListOf(),
        var modifyBlockedWords: Boolean = true,
        var blockedWordsMask: String = "**beep**",
        var maxMessageLength: Int = 128
    )
}
