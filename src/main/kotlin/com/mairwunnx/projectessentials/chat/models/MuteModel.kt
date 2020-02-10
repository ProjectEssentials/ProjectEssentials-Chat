package com.mairwunnx.projectessentials.chat.models

import com.mairwunnx.projectessentials.core.extensions.empty
import kotlinx.serialization.Serializable

@Serializable
data class MuteModel(
    var players: MutableList<Player> = mutableListOf()
) {
    @Serializable
    data class Player(
        var name: String = String.empty,
        var mutedBy: String = String.empty,
        var reason: String = String.empty
    )
}
