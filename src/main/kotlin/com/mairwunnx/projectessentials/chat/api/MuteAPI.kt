package com.mairwunnx.projectessentials.chat.api

object MuteAPI {
    private val muteHashMap = hashMapOf<String, MuteData>()

    fun mutePlayer(
        playerName: String,
        mutedBy: String,
        reason: String,
        override: Boolean
    ): Boolean {
        if (isInMute(playerName)) {
            if (override) {
                unmutePlayer(playerName)
                muteHashMap[playerName] = MuteData(mutedBy, playerName, reason)
                return true
            }
            return false
        } else {
            muteHashMap[playerName] = MuteData(mutedBy, playerName, reason)
            return true
        }
    }

    fun unmutePlayer(playerName: String): Boolean =
        if (isInMute(playerName)) {
            muteHashMap.remove(playerName)
            true
        } else {
            false
        }

    fun isInMute(playerName: String): Boolean = playerName in muteHashMap.keys

    fun getMuteInitiator(mutedPlayer: String): String? =
        if (isInMute(mutedPlayer)) muteHashMap[mutedPlayer]!!.mutedBy else null

    fun getMuteReason(mutedPlayer: String): String? =
        if (isInMute(mutedPlayer)) muteHashMap[mutedPlayer]!!.reason else null

    fun getMutedPlayers(): List<String> = muteHashMap.keys.toList()

    fun unmuteAll() = getMutedPlayers().forEach {
        unmutePlayer(it)
    }
}

