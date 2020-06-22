@file:Suppress("unused")

package com.mairwunnx.projectessentials.chat.impl

import com.mairwunnx.projectessentials.chat.commands.*
import com.mairwunnx.projectessentials.chat.impl.configurations.ChatSettingsConfiguration
import com.mairwunnx.projectessentials.chat.impl.configurations.MutedPlayersConfiguration
import com.mairwunnx.projectessentials.chat.impl.handlers.ReceiveMessageHandler
import com.mairwunnx.projectessentials.core.api.v1.localization.LocalizationAPI
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.core.api.v1.providers.ProviderAPI
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("project_essentials_chat")
class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 7
    override fun init() = Unit

    private val logger = LogManager.getLogger()

    private val providers = listOf(
        ChatSettingsConfiguration::class.java,
        MutedPlayersConfiguration::class.java,
        ModuleObject::class.java,
        ClearChatCommand::class.java,
        MuteCommand::class.java,
        UnmuteCommand::class.java,
        UnmuteAllCommand::class.java,
        MutedPlayersCommand::class.java
        // PrefixCommand::class.java,
        // SuffixCommand::class.java
    )

    init {
        EVENT_BUS.register(this)
        providers.forEach(ProviderAPI::addProvider)
        initLocalization()
    }

    private fun initLocalization() {
        LocalizationAPI.apply(this.javaClass) {
            mutableListOf(
                "/assets/projectessentialschat/lang/pt_br.json",
                "/assets/projectessentialschat/lang/zh_cn.json",
                "/assets/projectessentialschat/lang/de_de.json",
                "/assets/projectessentialschat/lang/ru_ru.json",
                "/assets/projectessentialschat/lang/en_us.json",
                "/assets/projectessentialschat/lang/es_cl.json",
                "/assets/projectessentialschat/lang/fr_fr.json",
                "/assets/projectessentialschat/lang/sv_se.json"
            )
        }
    }

    @SubscribeEvent
    fun onReceivedMessage(event: ClientChatReceivedEvent) = ReceiveMessageHandler.handle(event)
}
