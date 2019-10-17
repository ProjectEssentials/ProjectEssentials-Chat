package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentialscore.EssBase
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import net.minecraft.util.text.TextComponentUtils
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_chat")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-0.1.0.0"
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $modName mod ...")
        MinecraftForge.EVENT_BUS.register(this)
    }

    companion object {
        lateinit var modInstance: EntryPoint
    }

    @SubscribeEvent
    fun onChatMessage(event: ServerChatEvent) {
        if (!PermissionsAPI.hasPermission(event.username, "ess.chat")) {
            sendMsg("chat", event.player.commandSource, "chat.restricted")
            event.isCanceled = true
            return
        }

        val blockedWordsResult = ChatUtils.processBlockedWords(event)
        if (!blockedWordsResult.a) {
            event.isCanceled = true
            return
        } else {
            event.component = TextComponentUtils.toTextComponent {
                blockedWordsResult.b
            }
        }
    }
}
