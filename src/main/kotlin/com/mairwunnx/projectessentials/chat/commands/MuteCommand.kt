package com.mairwunnx.projectessentials.chat.commands

import com.mairwunnx.projectessentials.chat.EntryPoint
import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.playerName
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.arguments.EntityArgument
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.TranslationTextComponent
import org.apache.logging.log4j.LogManager

object MuteCommand {
    private val logger = LogManager.getLogger()

    private val argument = Commands.argument(
        "reason", StringArgumentType.string()
    ).executes {
        return@executes execute(it, StringArgumentType.getString(it, "reason"))
    }.then(
        Commands.argument(
            "override", BoolArgumentType.bool()
        ).executes {
            return@executes execute(
                it,
                StringArgumentType.getString(it, "reason"),
                BoolArgumentType.getBool(it, "override")
            )
        }
    )

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/mute\" command")

        dispatcher.register(
            literal<CommandSource>("mute")
                .then(
                    Commands.argument(
                        "player", EntityArgument.player()
                    ).then(argument).executes {
                        return@executes execute(it)
                    }
                ).then(
                    Commands.argument(
                        "player name", StringArgumentType.string()
                    ).then(argument).executes {
                        return@executes execute(it)
                    }
                ).executes {
                    execute(it)
                }
        )
    }

    private fun execute(
        context: CommandContext<CommandSource>,
        reason: String = ChatModelUtils.chatModel.mute.defaultReason,
        override: Boolean = false
    ): Int {
        if (context.isPlayerSender() && !EntryPoint.hasPermission(
                context.source.asPlayer(),
                "ess.chat.mute"
            )
        ) {
            sendMsg("chat", context.source, "chat.mute_restricted")
            return 0
        }

        val player = getPlayer(context)
        val playerName = getPlayerName(context)

        if (player != null) {
            if (player.name.string in ChatModelUtils.chatModel.mute.ignoredPlayers) {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_failed_player_ignored",
                    player.name.string
                )
                return 0
            }

            val result = MuteAPI.mutePlayer(
                player.name.string, context.playerName(), reason, override
            )

            if (result) {
                if (ChatModelUtils.chatModel.mute.notifyAllAboutMute) {
                    context.source.server.playerList.sendMessage(
                        TranslationTextComponent(
                            "project_essentials_chat.notify_muted",
                            player.name.string,
                            context.playerName(),
                            reason
                        )
                    )
                }

                sendMsg(
                    "chat",
                    player.commandSource,
                    "chat.lol_youre_muted",
                    context.playerName(),
                    reason
                )
                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_success",
                    player.name.string
                )
            } else {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_failed",
                    player.name.string
                )
            }
        } else if (playerName != null) {
            if (playerName in ChatModelUtils.chatModel.mute.ignoredPlayers) {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_failed_player_ignored",
                    playerName
                )
                return 0
            }

            val result = MuteAPI.mutePlayer(
                playerName, context.playerName(), reason, override
            )

            if (result) {
                if (ChatModelUtils.chatModel.mute.notifyAllAboutMute) {
                    context.source.server.playerList.sendMessage(
                        TranslationTextComponent(
                            "project_essentials_chat.notify_muted",
                            playerName,
                            context.playerName(),
                            reason
                        )
                    )
                }

                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_success",
                    playerName
                )
            } else {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.mute_failed",
                    playerName
                )
            }
        }
        return 0
    }

    private fun getPlayer(context: CommandContext<CommandSource>): ServerPlayerEntity? = try {
        EntityArgument.getPlayer(context, "player")
    } catch (ex: IllegalArgumentException) {
        null
    }

    private fun getPlayerName(context: CommandContext<CommandSource>): String? = try {
        StringArgumentType.getString(context, "player name")
    } catch (ex: IllegalArgumentException) {
        null
    }
}
