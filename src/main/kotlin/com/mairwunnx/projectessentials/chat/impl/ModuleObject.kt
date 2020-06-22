@file:Suppress("unused")

package com.mairwunnx.projectessentials.chat.impl

import com.mairwunnx.projectessentials.chat.impl.handlers.ChatMessageHandler
import com.mairwunnx.projectessentials.chat.impl.handlers.ReceiveMessageHandler
import com.mairwunnx.projectessentials.chat.localizations
import com.mairwunnx.projectessentials.chat.providers
import com.mairwunnx.projectessentials.core.api.v1.localization.LocalizationAPI
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.core.api.v1.providers.ProviderAPI
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("project_essentials_chat")
internal class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 7
    override fun init() = Unit

    private val logger = LogManager.getLogger()

    init {
        EVENT_BUS.register(this)
        providers.forEach(ProviderAPI::addProvider)
        LocalizationAPI.apply(this.javaClass) { localizations }
    }

    @SubscribeEvent
    fun onReceivedMessage(event: ClientChatReceivedEvent) = ReceiveMessageHandler.handle(event)

    @SubscribeEvent
    fun onChatMessage(event: ServerChatEvent) = ChatMessageHandler.handle(event)
}
