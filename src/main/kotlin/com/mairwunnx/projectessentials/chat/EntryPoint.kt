package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.chat.commands.*
import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
import com.mairwunnx.projectessentials.chat.models.MuteModelUtils
import com.mairwunnx.projectessentials.core.EssBase
import com.mairwunnx.projectessentials.core.extensions.empty
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.event.ClickEvent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_chat")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.15.2-1.1.0"
        logBaseInfo()
        validateForgeVersion()
        MinecraftForge.EVENT_BUS.register(this)
        loadAdditionalModules()
        ChatModelUtils.loadData()
        MuteModelUtils.loadData()
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName(permissionAPIClassPath)
            permissionsInstalled = true
            logger.info("Permissions module found!")
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(cooldownAPIClassPath)
            cooldownInstalled = true
            logger.info("Cooldown module found!")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var permissionsInstalled: Boolean = false
        var cooldownInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 0): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.hasPermissionLevel(opLevel)
            }
    }

    @SubscribeEvent
    fun onServerStarting(it: FMLServerStartingEvent) {
        ClearChatCommand.register(it.commandDispatcher)
        MuteCommand.register(it.commandDispatcher)
        UnmuteCommand.register(it.commandDispatcher)
        UnmuteAllCommand.register(it.commandDispatcher)
        MutedPlayersCommand.register(it.commandDispatcher)
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        ChatModelUtils.saveData()
        MuteModelUtils.saveData()
    }

    @SubscribeEvent
    fun onReceivedMessage(event: ClientChatReceivedEvent) {
        if (!ChatModelUtils.chatModel.events.joinMessageEnabled) {
            if ("key='multiplayer.player.joined'" in event.message.toString()) {
                event.isCanceled = true
                return
            }
        }

        if (!ChatModelUtils.chatModel.events.leftMessageEnabled) {
            if ("key='multiplayer.player.left'" in event.message.toString()) {
                event.isCanceled = true
                return
            }
        }

        if (!ChatModelUtils.chatModel.events.advancementsEnabled) {
            if ("key='chat.type.advancement'" in event.message.toString()) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ServerChatEvent) {
        if (MuteAPI.isInMute(event.username)) {
            val mutedBy = MuteAPI.getMuteInitiator(event.username)!!
            val reason = MuteAPI.getMuteReason(event.username)!!

            sendMsg(
                "chat",
                event.player.commandSource,
                "chat.muted",
                mutedBy,
                reason.replace(" ", " §7")
            )
            event.isCanceled = true
            return
        }

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

        if (ChatModelUtils.chatModel.moderation.messagingCooldownEnabled) {
            if (!hasPermission(event.player, "ess.chat.chatdelay.bypass", 3)) {
                if (ChatCooldown.getCooldownIsExpired(
                        event.player.name.string,
                        ChatModelUtils.chatModel.moderation.messagingCooldown
                    )
                ) {
                    ChatCooldown.addCooldown(event.player.name.string)
                } else {
                    sendMsg(
                        "chat",
                        event.player.commandSource,
                        "chat.cooldown_not_expired"
                    )
                    event.isCanceled = true
                    return
                }
            }
        }

        if (!ChatModelUtils.chatModel.moderation.advertisingAllowed) {
            if (!hasPermission(event.player, "ess.chat.advertising.bypass", 3)) {
                if (event.message.contains(
                        Regex(
                            ChatModelUtils.chatModel.moderation.advertisingRegex,
                            RegexOption.IGNORE_CASE
                        )
                    )
                ) {
                    sendMsg(
                        "chat",
                        event.player.commandSource,
                        "chat.advertising_not_allowed"
                    )
                    event.isCanceled = true
                    return
                }
            }
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
            Regex(
                "@\\S[a-zA-Z0-9]*", RegexOption.IGNORE_CASE
            ).findAll(event.component.formattedText).forEach {
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
                            if (it.name.string != event.player.name.string) {
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

        if (ChatModelUtils.chatModel.messaging.enableRangedChat) {
            if (!ChatUtils.isGlobalChat(event)) {
                val players = event.player.serverWorld.getEntitiesWithinAABB(
                    event.player.entity.javaClass, AxisAlignedBB(
                        event.player.positionVec.x - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                        event.player.positionVec.y - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                        event.player.positionVec.z - ChatModelUtils.chatModel.messaging.localChatRange / 2,
                        event.player.positionVec.x + ChatModelUtils.chatModel.messaging.localChatRange / 2,
                        event.player.positionVec.y + ChatModelUtils.chatModel.messaging.localChatRange / 2,
                        event.player.positionVec.z + ChatModelUtils.chatModel.messaging.localChatRange / 2
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
}
