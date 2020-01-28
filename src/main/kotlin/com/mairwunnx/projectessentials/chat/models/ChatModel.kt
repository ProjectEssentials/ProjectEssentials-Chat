package com.mairwunnx.projectessentials.chat.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    var moderation: Moderation = Moderation(),
    var messaging: Messaging = Messaging(),
    var mentions: Mentions = Mentions(),
    var events: Events = Events()
) {
    @Serializable
    data class Moderation(
        var blockedWords: MutableList<String> = mutableListOf("fuck", "shit"),
        var blockedChars: List<String> = mutableListOf(),
        var modifyBlockedWords: Boolean = true,
        var blockedWordsMask: String = "**beep**",
        var maxMessageLength: Int = 128,
        var messagingCooldownEnabled: Boolean = true,
        var messagingCooldown: Int = 5,
        var advertisingAllowed: Boolean = false,
        var advertisingRegex: String = "((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00A7 \n]|$))"
    )

    @Serializable
    data class Messaging(
        var messageGlobalPattern: String = "&8[&2GLOBAL&8] &a┃ &8[&6%player&8]&7: &f%message",
        var messageLocalPattern: String = "&8[&cLOCAL&8] &4┃ &8[&7%player&8]&7: &7§o%message",
        var messageCommonPattern: String = "&8[&7%player&8]&7: &f%message",
        var enableRangedChat: Boolean = true,
        var localChatRange: Int = 100,
        var chatEnabled: Boolean = true
    )

    @Serializable
    data class Mentions(
        var mentionsEnabled: Boolean = true,
        var mentionInActionBar: Boolean = true,
        var mentionMessage: String = "&7you are mentioned by &l&7%player&7 player, in the chat.",
        var mentionAtFormat: String = "&c",
        var mentionNameFormat: String = "&b"
    )

    @Serializable
    data class Events(
        var joinMessageEnabled: Boolean = true,
        var leftMessageEnabled: Boolean = true,
        var advancementsEnabled: Boolean = true
    )
}
