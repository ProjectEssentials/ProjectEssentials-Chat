package com.mairwunnx.projectessentials.chat.impl.configurations

import kotlinx.serialization.Serializable

@Serializable
data class MutedPlayersConfigurationModel(
    var entries: MutableList<Entry> = mutableListOf()
) {
    @Serializable
    data class Entry(
        val name: String,
        val uuid: String,
        val mutedBy: String,
        val reason: String,
        val isTemp: Boolean,
        val timeStart: String?,
        val timeEnd: String?
    )
}
