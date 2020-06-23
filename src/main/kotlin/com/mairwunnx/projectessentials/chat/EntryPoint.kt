//package com.mairwunnx.projectessentials.chat
//
//import com.mairwunnx.projectessentials.chat.api.MuteAPI
//import com.mairwunnx.projectessentials.chat.commands.*
//import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
//import com.mairwunnx.projectessentials.chat.models.MuteModelUtils
//import com.mairwunnx.projectessentials.core.EssBase
//import com.mairwunnx.projectessentials.core.configuration.localization.LocalizationConfigurationUtils
//import com.mairwunnx.projectessentials.core.extensions.empty
//import com.mairwunnx.projectessentials.core.localization.processLocalizations
//import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
//import net.minecraft.entity.player.ServerPlayerEntity
//import net.minecraft.util.math.AxisAlignedBB
//import net.minecraft.util.text.TextComponentUtils
//import net.minecraft.util.text.event.ClickEvent
//import net.minecraftforge.client.event.ClientChatReceivedEvent
//import net.minecraftforge.common.MinecraftForge
//import net.minecraftforge.event.ServerChatEvent
//import net.minecraftforge.eventbus.api.SubscribeEvent
//import net.minecraftforge.fml.common.Mod
//import net.minecraftforge.fml.event.server.FMLServerStartingEvent
//import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
//import org.apache.logging.log4j.LogManager
//
//@Suppress("unused")
//@Mod("project_essentials_chat")
//class EntryPoint : EssBase() {
//    private val logger = LogManager.getLogger()
//
//
//    @SubscribeEvent
//    fun onChatMessage(event: ServerChatEvent) {
//        val nicknameComponent = TextComponentUtils.toTextComponent {
//            ChatUtils.getMessagePattern(event).replace(
//                "%player", event.username
//            )
//        }.style.setClickEvent(
//            if (ChatUtils.isCommonChat()) {
//                ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "@${event.username} ")
//            } else {
//                ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "!@${event.username} ")
//            }
//        )
//
//        val group =
//            when {
//                permissionsInstalled -> PermissionsAPI.getUserGroup(event.username).name
//                else -> String.empty
//            }
//
//        event.component = TextComponentUtils.toTextComponent {
//            ChatUtils.getMessagePattern(event).replace(
//                "%group", group
//            ).replace(
//                "%player", event.username
//            ).replace(
//                "%message",
//                if (ChatUtils.isGlobalChat(event)) {
//                    event.component.formattedText.drop(1)
//                } else {
//                    event.component.formattedText
//                }
//            ).replace(
//                "&", "§"
//            )
//        }.setStyle(nicknameComponent)
//
//        val mentionSettings = ChatModelUtils.chatModel.mentions
//        val mentions = mutableListOf<String>()
//        if (mentionSettings.mentionsEnabled) {
//            Regex(
//                "@\\S[a-zA-Z0-9]*", RegexOption.IGNORE_CASE
//            ).findAll(event.component.formattedText).forEach {
//                if (it.value != "@e" &&
//                    it.value != "@a" &&
//                    it.value != "@p" &&
//                    it.value != "@r" &&
//                    it.value != "@s"
//                ) {
//                    mentions.add(it.value)
//                }
//            }
//        }
//        val anFormat = mentionSettings.mentionAtFormat.replace("&", "§")
//        val nameFormat = mentionSettings.mentionNameFormat.replace("&", "§")
//        mentions.forEach {
//            event.component = TextComponentUtils.toTextComponent {
//                event.component.formattedText.replace(
//                    it,
//                    "$anFormat@$nameFormat${it.replace("@", "")}${ChatUtils.getMessageColor(event)}"
//                )
//            }.setStyle(nicknameComponent)
//        }
//
//        if (mentions.isNotEmpty()) {
//            if (mentionSettings.mentionsEnabled && mentionSettings.mentionInActionBar) {
//                if ("@all" in mentions) {
//                    if (hasPermission(event.player, "ess.chat.mention.all", 2)) {
//                        event.player.server.playerList.players.forEach {
//                            if (it.name.string != event.player.name.string) {
//                                it.sendStatusMessage(
//                                    TextComponentUtils.toTextComponent {
//                                        mentionSettings.mentionMessage.replace(
//                                            "%player",
//                                            event.player.name.string
//                                        ).replace("&", "§")
//                                    }, true
//                                )
//                            }
//                        }
//                        return
//                    } else {
//                        sendMessage(
//                            event.player.commandSource,
//                            "chat.mention_all_aborted"
//                        )
//                    }
//                }
//                event.player.server.playerList.players.forEach {
//                    if ("@${it.name.string}" in mentions) {
//                        it.sendStatusMessage(
//                            TextComponentUtils.toTextComponent {
//                                mentionSettings.mentionMessage.replace(
//                                    "%player",
//                                    event.player.name.string
//                                ).replace("&", "§")
//                            }, true
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
