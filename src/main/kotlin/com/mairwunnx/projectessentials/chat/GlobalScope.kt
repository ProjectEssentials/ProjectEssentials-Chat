package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.impl.configurations.ChatSettingsConfiguration
import com.mairwunnx.projectessentials.chat.impl.configurations.MutedPlayersConfiguration
import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName

internal val chatSettingsConfiguration by lazy {
    getConfigurationByName<ChatSettingsConfiguration>("chat-settings").take()
}

internal val mutedPlayersConfiguration by lazy {
    getConfigurationByName<MutedPlayersConfiguration>("muted-players").take()
}

