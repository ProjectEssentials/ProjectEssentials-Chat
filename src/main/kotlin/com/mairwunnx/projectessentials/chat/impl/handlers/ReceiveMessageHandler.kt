package com.mairwunnx.projectessentials.chat.impl.handlers

import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration
import net.minecraftforge.client.event.ClientChatReceivedEvent

internal object ReceiveMessageHandler {
    fun handle(event: ClientChatReceivedEvent) {
        fun cancelIf(key: String) {
            if ("key='$key" !in event.message.toString()) return
            { event.isCanceled = true }.let { return }
        }
        if (!chatSettingsConfiguration.events.joinMessageEnabled) cancelIf("multiplayer.player.joined'")
        if (!chatSettingsConfiguration.events.leftMessageEnabled) cancelIf("multiplayer.player.left'")
        if (!chatSettingsConfiguration.events.advancementsEnabled) cancelIf("chat.type.advancement'")
        if (!chatSettingsConfiguration.events.deathMessagesEnabled) cancelIf("death.")
    }
}
