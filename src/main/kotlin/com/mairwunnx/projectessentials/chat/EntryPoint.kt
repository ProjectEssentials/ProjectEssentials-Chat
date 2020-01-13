package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
import com.mairwunnx.projectessentialscore.EssBase
import com.mairwunnx.projectessentialscore.extensions.empty
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.event.ClickEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_chat")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-0.2.0.0"
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $modName mod ...")
        MinecraftForge.EVENT_BUS.register(this)
        loadAdditionalModules()
        ChatModelUtils.loadData()
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName(
                "com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI"
            )
            permissionsInstalled = true
            logger.info("Permissions module found!")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var permissionsInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 0): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.server.opPermissionLevel >= opLevel
            }
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        logger.info("Shutting down $modName mod ...")
        ChatModelUtils.saveData()
    }

    @SubscribeEvent
    fun onChatMessage(event: ServerChatEvent) {
        if (!ChatModelUtils.chatModel.messaging.chatEnabled) {
            sendMsg("chat", event.player.commandSource, "chat.disabled")
            event.isCanceled = true
            return
        }

        if (!hasPermission(event.player, "ess.chat")) {
            sendMsg("chat", event.player.commandSource, "chat.restricted")
            event.isCanceled = true
            return
        }

        if (event.message.contains("&")) {
            if (hasPermission(event.player, "ess.chat.color", 2)) {
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
            if (ChatUtils.isCommonChat()) {
                ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "@${event.username} ")
            } else {
                ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "!@${event.username} ")
            }
        )

        val group =
            when {
                permissionsInstalled -> PermissionsAPI.getUserGroup(event.username).name
                else -> String.empty
            }

        event.component = TextComponentUtils.toTextComponent {
            ChatUtils.getMessagePattern(event).replace(
                "%group", group
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

        val mentionSettings = ChatModelUtils.chatModel.mentions
        val mentions = mutableListOf<String>()
        if (mentionSettings.mentionsEnabled) {
            Regex("@\\S[a-zA-Z0-9]*").findAll(event.component.formattedText).forEach {
                if (it.value != "@e" &&
                    it.value != "@a" &&
                    it.value != "@p" &&
                    it.value != "@r" &&
                    it.value != "@s"
                ) {
                    mentions.add(it.value)
                }
            }
        }
        val anFormat = mentionSettings.mentionAtFormat.replace("&", "§")
        val nameFormat = mentionSettings.mentionNameFormat.replace("&", "§")
        mentions.forEach {
            event.component = TextComponentUtils.toTextComponent {
                event.component.formattedText.replace(
                    it,
                    "$anFormat@$nameFormat${it.replace("@", "")}${ChatUtils.getMessageColor(event)}"
                )
            }.setStyle(nicknameComponent)
        }

        if (mentions.isNotEmpty()) {
            if (mentionSettings.mentionsEnabled && mentionSettings.mentionInActionBar) {
                if ("@all" in mentions) {
                    if (hasPermission(event.player, "ess.chat.mention.all", 2)) {
                        event.player.server.playerList.players.forEach {
                            it.sendStatusMessage(
                                TextComponentUtils.toTextComponent {
                                    mentionSettings.mentionMessage.replace(
                                        "%player",
                                        event.player.name.string
                                    ).replace("&", "§")
                                }, true
                            )
                        }
                        return
                    } else {
                        sendMsg(
                            "chat",
                            event.player.commandSource,
                            "chat.mention_all_aborted"
                        )
                    }
                }
                event.player.server.playerList.players.forEach {
                    if ("@${it.name.string}" in mentions) {
                        it.sendStatusMessage(
                            TextComponentUtils.toTextComponent {
                                mentionSettings.mentionMessage.replace(
                                    "%player",
                                    event.player.name.string
                                ).replace("&", "§")
                            }, true
                        )
                    }
                }
            }
        }

        if (!ChatUtils.isGlobalChat(event)) {
            val players = event.player.serverWorld.getEntitiesWithinAABB(
                event.player.entity.javaClass, AxisAlignedBB(
                    event.player.posX - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                    event.player.posY - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                    event.player.posZ - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                    event.player.posX + ChatModelUtils.chatModel.messaging.localChatRange / 2,
                    event.player.posY + ChatModelUtils.chatModel.messaging.localChatRange / 2,
                    event.player.posZ + ChatModelUtils.chatModel.messaging.localChatRange / 2
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
