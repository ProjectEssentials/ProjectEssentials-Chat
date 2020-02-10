package com.mairwunnx.projectessentials.chat.commands

import com.mairwunnx.projectessentials.chat.EntryPoint
import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.playerName
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.arguments.EntityArgument
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.TranslationTextComponent
import org.apache.logging.log4j.LogManager

object UnmuteCommand {
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/unmute\" command")

        dispatcher.register(
            literal<CommandSource>("unmute")
                .then(
                    Commands.argument(
                        "player", EntityArgument.player()
                    ).executes {
                        return@executes execute(it)
                    }
                ).then(
                    Commands.argument(
                        "player name", StringArgumentType.string()
                    ).executes {
                        return@executes execute(it)
                    }
                )
        )
    }

    private fun execute(
        context: CommandContext<CommandSource>
    ): Int {
        if (context.isPlayerSender() && !EntryPoint.hasPermission(
                context.source.asPlayer(),
                "ess.chat.unmute",
                3
            )
        ) {
            sendMsg("chat", context.source, "chat.unmute_restricted")
            return 0
        }

        val player = getPlayer(context)
        val playerName = getPlayerName(context)

        if (player != null) {
            if (MuteAPI.unmutePlayer(player.name.string)) {
                if (ChatModelUtils.chatModel.mute.notifyAllAboutUnmute) {
                    context.source.server.playerList.sendMessage(
                        TranslationTextComponent(
                            "project_essentials_chat.notify_unmuted",
                            player.name.string,
                            context.playerName()
                        )
                    )
                }

                sendMsg(
                    "chat",
                    player.commandSource,
                    "chat.youre_unmuted",
                    context.playerName()
                )
                sendMsg(
                    "chat",
                    context.source,
                    "chat.unmute_success",
                    player.name.string
                )
            } else {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.unmute_failed",
                    player.name.string
                )
            }
        } else if (playerName != null) {
            if (MuteAPI.unmutePlayer(playerName)) {
                if (ChatModelUtils.chatModel.mute.notifyAllAboutUnmute) {
                    context.source.server.playerList.sendMessage(
                        TranslationTextComponent(
                            "project_essentials_chat.notify_unmuted",
                            playerName,
                            context.playerName()
                        )
                    )
                }

                sendMsg(
                    "chat",
                    context.source,
                    "chat.unmute_success",
                    playerName
                )
            } else {
                sendMsg(
                    "chat",
                    context.source,
                    "chat.unmute_failed",
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
