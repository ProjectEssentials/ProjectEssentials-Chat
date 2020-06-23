package com.mairwunnx.projectessentials.chat.impl.configurations

import kotlinx.serialization.Serializable

@Serializable
data class ChatSettingsConfigurationModel(
    val filters: Filters = Filters(),
    var moderation: Moderation = Moderation(),
    var messaging: Messaging = Messaging(),
    var mentions: Mentions = Mentions(),
    var events: Events = Events(),
    var mute: Mute = Mute(),
    val prefixes: Prefixes = Prefixes(),
    val suffixes: Suffixes = Suffixes()
) {
    @Serializable
    data class Filters(
        var filterWords: Boolean = false,
        var filterChars: Boolean = false,
        var filterExceeds: Boolean = false,
        var filterAdvertising: Boolean = false
    )

    @Serializable
    data class Moderation(
        var colorsAllowed: Boolean = false,
        var blockedWords: MutableList<String> = mutableListOf("fuck", "ass"),
        var blockedCharsRegex: String = "",
        var allowedCharsRegex: String = ".*",
        var modifyBlockedWords: Boolean = true,
        var blockedWordsMask: String = "*beep*",
        var maxMessageLength: Int = 128,
        var messagingSlowMode: Int = -1,
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
        var advancementsEnabled: Boolean = true,
        var deathMessagesEnabled: Boolean = true
    )

    @Serializable
    data class Mute(
        var defaultReason: String = "Reason was not provided.",
        var ignoredPlayers: List<String> = listOf(),
        var notifyAllAboutMute: Boolean = true,
        var notifyAllAboutUnmute: Boolean = true,
        var notifyAllAboutUnmuteAll: Boolean = true,
        var replaceNativeMeCommand: Boolean = false
    )

    @Serializable
    data class Prefixes(val map: MutableMap<String, String> = mutableMapOf())

    @Serializable
    data class Suffixes(val map: MutableMap<String, String> = mutableMapOf())
}
