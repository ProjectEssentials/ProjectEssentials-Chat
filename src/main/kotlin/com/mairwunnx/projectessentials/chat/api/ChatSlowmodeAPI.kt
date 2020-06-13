package com.mairwunnx.projectessentials.chat.api

import java.time.Duration
import java.time.ZonedDateTime

/**
 * Class for interacting with slow mode in chat.
 * @since 2.0.0.
 */
object ChatSlowmodeAPI {
    private val slowmodeMap = hashMapOf<String, ZonedDateTime>()

    /**
     * Default cooldown, -1 means slow mode in chat
     * are disabled.
     *
     * @since 2.0.0.
     */
    const val defaultCooldown = -1

    /**
     * @return immutable slowmode map.
     * @since 2.0.0.
     */
    fun getMap() = slowmodeMap.toMap()

    /**
     * Adds target player name to slowmode map
     * with current date time.
     *
     * @param name player name to add.
     * @since 2.0.0.
     */
    fun action(name: String) = dispose(name).also {
        synchronized(slowmodeMap) { slowmodeMap[name] = ZonedDateTime.now() }
    }

    /**
     * Disposes player from slowmode map.
     *
     * @param name player to dispose.
     * @since 2.0.0.
     */
    fun dispose(name: String) = synchronized(slowmodeMap) { slowmodeMap.remove(name) }

    /**
     * @param name player to check.
     * @param minTime time for checking.
     * @return true if slowmode time for specified player expired.
     * @since 2.0.0.
     */
    fun isExpired(name: String, minTime: Int = defaultCooldown) = passed(name) >= minTime

    /**
     * @param name player name to check.
     * @return long number of duration between
     * adding player and now.
     * @since 2.0.0.
     */
    fun passed(name: String) = slowmodeMap[name]?.let {
        Duration.between(it, ZonedDateTime.now()).seconds
    } ?: let { Long.MAX_VALUE }
}
