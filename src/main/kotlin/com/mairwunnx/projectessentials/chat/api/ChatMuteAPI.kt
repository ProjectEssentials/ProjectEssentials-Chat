package com.mairwunnx.projectessentials.chat.api

import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration
import com.mairwunnx.projectessentials.chat.impl.configurations.MutedPlayersConfigurationModel.Entry
import com.mairwunnx.projectessentials.chat.mutedPlayersConfiguration
import java.time.Duration
import java.time.ZonedDateTime

/**
 * This class contains API for working with
 * chat player mutes.
 * @since 2.0.0.
 */
object ChatMuteAPI {
    /**
     * @return all mute players sequence.
     * @since 2.0.0.
     */
    fun getMutePlayers() = mutedPlayersConfiguration.entries.asSequence()

    /**
     * @param name player name to search.
     * @param uuid player uuid to search.
     * @return mute player or null if player not muted.
     * @since 2.0.0.
     */
    fun getMutePlayer(name: String, uuid: String) = getMutePlayers().find {
        it.name == name || it.uuid == uuid
    }

    /**
     * @param name player name to check.
     * @param uuid player name to check.
     * @return true if player is muted.
     * @since 2.0.0.
     */
    fun isInMute(name: String, uuid: String) = getMutePlayer(name, uuid)?.let {
        (Duration.between(
            ZonedDateTime.parse(it.timeEnd), ZonedDateTime.now()
        ).toMillis() >= 1).let { isExpired ->
            if (isExpired) {
                getMutePlayers().toMutableList().removeIf { entry ->
                    entry.name == name || entry.uuid == uuid
                }.let { false }
            } else true
        }
    } ?: false


    /**
     * @param name player name to check.
     * @param uuid player uuid to check.
     * @return true if player is temporary muted.
     * @since 2.0.0.
     */
    fun isPlayerTempMuted(name: String, uuid: String) = getMutePlayer(name, uuid)?.isTemp ?: false

    /**
     * @param name player name to unmute.
     * @param uuid player uuid to unmute.
     * @return true if player removed.
     * @since 2.0.0.
     */
    fun unmute(name: String, uuid: String) =
        getMutePlayers().toMutableList().removeIf { it.name == name || it.uuid == uuid }

    /**
     * @param name player name to mute.
     * @param uuid player uuid to mute.
     * @param mutedBy muted by name. (can be null)
     * @param reason mute reason. (can be null)
     * @return true if player muted successfully.
     * @since 2.0.0.
     */
    fun mute(name: String, uuid: String, mutedBy: String?, reason: String?): Boolean {
        if (isInMute(name, uuid)) return false
        mutedPlayersConfiguration.entries.add(
            Entry(
                name, uuid,
                mutedBy?.let { it } ?: "#server",
                reason?.let { it } ?: chatSettingsConfiguration.mute.defaultReason,
                false,
                null, null
            )
        ).let { return true }
    }

    /**
     * @param name player name to mute.
     * @param uuid player uuid to mute.
     * @param mutedBy muted by name. (can be null)
     * @param reason mute reason. (can be null)
     * @param duration temp mute duration in minecraft ticks.
     * @return true if player temporary muted successfully.
     * @since 2.0.0.
     */
    fun tempMute(
        name: String, uuid: String, mutedBy: String?, reason: String?, duration: Long
    ): Boolean {
        if (isInMute(name, uuid)) return false
        mutedPlayersConfiguration.entries.add(
            Entry(
                name, uuid,
                mutedBy?.let { it } ?: "#server",
                reason?.let { it } ?: chatSettingsConfiguration.mute.defaultReason,
                true,
                ZonedDateTime.now().toString(),
                ZonedDateTime.now().plusSeconds(duration / 20).toString()
            )
        ).let { return true }
    }
}
