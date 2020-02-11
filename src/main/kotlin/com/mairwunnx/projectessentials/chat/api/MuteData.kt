package com.mairwunnx.projectessentials.chat.api

data class MuteData(val mutedBy: String, val mutedPlayer: String, val reason: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MuteData

        if (mutedBy != other.mutedBy) return false
        if (mutedPlayer != other.mutedPlayer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mutedBy.hashCode()
        result = 31 * result + mutedPlayer.hashCode()
        return result
    }
}
