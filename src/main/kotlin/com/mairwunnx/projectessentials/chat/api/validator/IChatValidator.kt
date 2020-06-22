package com.mairwunnx.projectessentials.chat.api.validator

/**
 * Base interface for chat validation class.
 *
 * @since 2.0.0.
 */
interface IChatValidator {
    /**
     * @param message message to check.
     * @return true if message contains restricted word.
     * @since 2.0.0.
     */
    fun isContainsBlockedWord(message: String): Boolean

    /**
     * @param message message to check.
     * @return true if message contains blocked chars.
     * @since 2.0.0.
     */
    fun isContainsBlockedChars(message: String): Boolean

    /**
     * @param message message to check.
     * @return true if message exceeds max message lenght.
     * @since 2.0.0.
     */
    fun isMessageExceeds(message: String): Boolean

    /**
     * @param message message to check.
     * @return true if message contains advertising.
     * @since 2.0.0.
     */
    fun isContainsAdvertising(message: String): Boolean

    /**
     * @param message message to check.
     * @return true if message targets to global chat.
     * @since 2.0.0.
     */
    fun isGlobalChat(message: String): Boolean
}
