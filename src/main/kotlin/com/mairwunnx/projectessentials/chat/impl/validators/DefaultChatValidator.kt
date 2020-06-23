package com.mairwunnx.projectessentials.chat.impl.validators

import com.mairwunnx.projectessentials.chat.api.validator.IChatValidator
import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration

object DefaultChatValidator : IChatValidator {
    override fun isContainsBlockedWord(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterWords) return false
        // @formatter:off
        return chatSettingsConfiguration.moderation.blockedWords.filter {
            message.contains(Regex(it, RegexOption.IGNORE_CASE)) ||
            message.matches(Regex(it, RegexOption.IGNORE_CASE)) ||
            message.contains(it, true)
        }.count() > 0
        // @formatter:on
    }

    override fun isContainsBlockedChars(message: String): Boolean {
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

    override fun isContainsAllowedChars(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterChars) return true
        // @formatter:off
        with(chatSettingsConfiguration.moderation.allowedCharsRegex) {
            return (
                message.contains(Regex(this, RegexOption.IGNORE_CASE)) ||
                message.matches(Regex(this, RegexOption.IGNORE_CASE))
            )
        }
        // @formatter:on
    }

    override fun isMessageExceeds(message: String): Boolean {
        if (!chatSettingsConfiguration.filters.filterExceeds) return false
        return message.count() > chatSettingsConfiguration.moderation.maxMessageLength
    }

    override fun isContainsAdvertising(message: String): Boolean {
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

    override fun isGlobalChat(message: String): Boolean {
        if (!chatSettingsConfiguration.messaging.enableRangedChat) return true
        return message.startsWith("!")
    }
}
