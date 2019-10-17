package com.mairwunnx.projectessentials.chat.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    var moderation: Moderation = Moderation(),
    var messaging: Messaging = Messaging()
) {
    @Serializable
    data class Moderation(
        var blockedWords: MutableList<String> = mutableListOf("fuck", "shit"),
        var blockedChars: List<String> = mutableListOf(),
        var modifyBlockedWords: Boolean = true,
        var blockedWordsMask: String = "**beep**",
        var maxMessageLength: Int = 128

    )

    @Serializable
    data class Messaging(
        var messagePattern: String = "&7[&6%type&7] &7[&6%player&7]: &f%message"
    )
}
