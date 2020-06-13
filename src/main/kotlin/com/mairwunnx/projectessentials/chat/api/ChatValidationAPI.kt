package com.mairwunnx.projectessentials.chat.api

import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration

/**
 * This class contains API for working with
 * chat validations.
 * @since 2.0.0.
 */
object ChatValidationAPI {
    /**
     * @param message message to check.
     * @return true if message contains restricted word.
     * @since 2.0.0.
     */
    fun isContainsBlockedWord(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterWords) return false
        // @formatter:off
        return chatSettingsConfiguration.moderation.blockedWords.filter {
            message.contains(Regex(it, RegexOption.IGNORE_CASE)) ||
            message.matches(Regex(it, RegexOption.IGNORE_CASE)) ||
            message.contains(it, true)
        }.count() > 0
        // @formatter:on
    }

    /**
     * @param message message to check.
     * @return true if message contains blocked chars.
     * @since 2.0.0.
     */
    fun isContainsBlockedChars(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterChars) return false
        // @formatter:off
        with(chatSettingsConfiguration.moderation.blockedCharsRegex) {
            return (
                message.contains(Regex(this, RegexOption.IGNORE_CASE)) ||
                message.matches(Regex(this, RegexOption.IGNORE_CASE))
            )
        }
        // @formatter:on
    }

    /**
     * @param message message to check.
     * @return true if message exceeds max message lenght.
     * @since 2.0.0.
     */
    fun isMessageExceeds(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterExceeds) return false
        return message.count() > chatSettingsConfiguration.moderation.maxMessageLength
    }

    /**
     * @param message message to check.
     * @return true if message contains advertising.
     * @since 2.0.0.
     */
    fun isContainsAdvertising(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterAdvertising) return false
        // @formatter:off
        with(chatSettingsConfiguration.moderation.advertisingRegex) {
            return (
                message.contains(Regex(this, RegexOption.IGNORE_CASE)) ||
                message.matches(Regex(this, RegexOption.IGNORE_CASE))
            )
        }
        // @formatter:on
    }

    /**
     * @param message message to check.
     * @return true if message targets to global chat.
     * @since 2.0.0.
     */
    fun isGlobalChat(message: String): Boolean {
        if (!chatSettingsConfiguration.messaging.enableRangedChat) return true
        return message.startsWith("!")
    }
}
