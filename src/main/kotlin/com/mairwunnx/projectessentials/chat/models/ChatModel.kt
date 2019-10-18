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
        var messageGlobalPattern: String = "&8[&2GLOBAL&8] &a┃ &8[&6%player&8]&7: &f%message",
        var messageLocalPattern: String = "&8[&cLOCAL&8] &4┃ &8[&7%player&8]&7: &7§o%message",
        var messageCommonPattern: String = "&8[&7%player&8]&7: &f%message",
        var enableRangedChat: Boolean = true,
        var localChatRange: Int = 100
    )
}
