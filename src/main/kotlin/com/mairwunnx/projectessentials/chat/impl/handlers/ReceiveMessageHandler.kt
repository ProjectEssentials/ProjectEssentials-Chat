package com.mairwunnx.projectessentials.chat.impl.handlers

import net.minecraftforge.client.event.ClientChatReceivedEvent
import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration as config

internal object ReceiveMessageHandler {
    private inline fun ClientChatReceivedEvent.cancelIf(key: String, action: () -> Unit) {
        if ("key='$key" !in this.message.toString()) return
        run { this.isCanceled = true }.also { action() }
    }

    fun handle(event: ClientChatReceivedEvent) {
        if (!config.events.joinMessageEnabled) event.cancelIf("multiplayer.player.joined'") { return }
        if (!config.events.leftMessageEnabled) event.cancelIf("multiplayer.player.left'") { return }
        if (!config.events.advancementsEnabled) event.cancelIf("chat.type.advancement'") { return }
        if (!config.events.deathMessagesEnabled) event.cancelIf("death.") { return }
    }
}
