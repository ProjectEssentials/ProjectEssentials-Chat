package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.models.ChatModelBase
import com.mairwunnx.projectessentialscore.EssBase
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.event.ClickEvent
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

        if (event.message.contains("&")) {
            if (PermissionsAPI.hasPermission(event.username, "ess.chat.color")) {
                event.component = TextComponentUtils.toTextComponent {
                    event.message.replace("&", "§")
                }
            } else {
                sendMsg("chat", event.player.commandSource, "chat.color_restricted")
                event.isCanceled = true
                return
            }
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

        if (!ChatUtils.processBlockedChars(event)) {
            event.isCanceled = true
            return
        }

        if (!ChatUtils.processMessageLength(event)) {
            event.isCanceled = true
            return
        }

        val nicknameComponent = TextComponentUtils.toTextComponent {
            ChatUtils.getMessagePattern(event).replace(
                "%player", event.username
            )
        }.style.setClickEvent(
            ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/m ${event.username}")
        )

        // @MairwunNx -> &b@&dMairwunNx

        event.component = TextComponentUtils.toTextComponent {
            ChatUtils.getMessagePattern(event).replace(
                "%group", PermissionsAPI.getUserGroup(event.username).name
            ).replace(
                "%player", event.username
            ).replace(
                "%message",
                if (ChatUtils.isGlobalChat(event)) {
                    event.component.formattedText.drop(1)
                } else {
                    event.component.formattedText
                }
            ).replace(
                "&", "§"
            )
        }.setStyle(nicknameComponent)

        val mentions = mutableListOf<String>()
        Regex("@\\S[a-zA-Z0-9]*").findAll(event.component.formattedText).forEach {
            mentions.add(it.value)
        }
        mentions.forEach {
            event.component = TextComponentUtils.toTextComponent {
                event.component.formattedText.replace(
                    it, "§c@§b${it.replace("@", "")}${when {
                        ChatUtils.isGlobalChat(event) -> "§f"
                        else -> if (ChatUtils.isCommonChat()) "§f" else "§7§o"
                    }}"
                )
            }
        }

        if (mentions.isNotEmpty()) {
            event.player.server.playerList.players.forEach {
                val sortedMentions = mentions.sorted()
                if ("@${it.name.string}" in sortedMentions) {
                    it.sendStatusMessage(
                        TextComponentUtils.toTextComponent {
                            "§7you are mentioned by §l§7${event.player.name.string}§7 player, in the chat."
                        },
                        true
                    )
                }
            }
        }

        if (!ChatUtils.isGlobalChat(event)) {
            val players = event.player.serverWorld.getEntitiesWithinAABB(
                event.player.entity.javaClass, AxisAlignedBB(
                    event.player.posX - ChatModelBase.chatModel.messaging.localChatRange / 2,
                    event.player.posY - ChatModelBase.chatModel.messaging.localChatRange / 2,
                    event.player.posZ - ChatModelBase.chatModel.messaging.localChatRange / 2,
                    event.player.posX + ChatModelBase.chatModel.messaging.localChatRange / 2,
                    event.player.posY + ChatModelBase.chatModel.messaging.localChatRange / 2,
                    event.player.posZ + ChatModelBase.chatModel.messaging.localChatRange / 2
                )
            )
            players.forEach {
                it.sendMessage(event.component)
            }
            event.isCanceled = true
            return
        }
    }
}
