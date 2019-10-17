package com.mairwunnx.projectessentials.chat.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    var moderation: Moderation
) {
    @Serializable
    data class Moderation(
        var blockedWords: List<String>,
        var blockedChars: List<String>,
        var modifyBlockedWords: Boolean,
        var blockedWordsMask: String
    )
}
