package com.mairwunnx.projectessentials.chat.commands

import com.mairwunnx.projectessentials.chat.EntryPoint
import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.playerName
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.helpers.PERMISSION_LEVEL
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.util.text.TextComponentUtils
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import org.apache.logging.log4j.LogManager

object ClearChatCommand {
    private val logger = LogManager.getLogger()
    private val aliases = listOf("clearchat", "chat-clear", "chatclear", "cc")

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/clear-chat\" command")
        applyAliases()

        val literalArgument = literal<CommandSource>("clear-chat").then(
            Commands.argument(
                "only for you", BoolArgumentType.bool()
            ).executes {
                return@executes execute(it, BoolArgumentType.getBool(it, "only for you"))
            }
        )

        val literalNode = dispatcher.register(literalArgument.executes {
            execute(it, false)
        })

        aliases.forEach { alias ->
            dispatcher.register(
                Commands.literal(alias).executes { ctx ->
                    execute(ctx, false)
                }.redirect(literalNode)
            )
        }
    }

    private fun applyAliases() {
        if (!EntryPoint.cooldownInstalled) return
        CommandsAliases.aliases["clear-chat"] = aliases.toMutableList()
    }

    private fun execute(
        context: CommandContext<CommandSource>,
        clearOnlyForSender: Boolean
    ): Int {
        if (clearOnlyForSender) {
            if (context.isPlayerSender()) {
                if (EntryPoint.hasPermission(context.source.asPlayer(), "ess.chat.clear", 2)) {
                    DistExecutor.runWhenOn(Dist.CLIENT) {
                        Runnable {
                            Minecraft.getInstance().ingameGUI.chatGUI.clearChatMessages(true)
                        }
                    }
                } else {
                    logger.warn(
                        PERMISSION_LEVEL
                            .replace("%0", context.playerName())
                            .replace("%1", "clear-chat")
                    )
                    sendMsg("chat", context.source, "chat.clear_restricted")
                }
            } else {
                logger.info("Command with parameter `clearOnlyForSender` can't be executed from server. Type `cls` if you use windows or `/clear` if you use mac os or linux.")
            }
        } else {
            if (!context.isPlayerSender() || EntryPoint.hasPermission(
                    context.source.asPlayer(),
                    "ess.chat.clear.other",
                    3
                )
            ) {
                val message = StringBuilder()
                repeat(256) {
                    message.append("\n")
                }

                context.source.server.playerList.sendMessage(
                    TextComponentUtils.toTextComponent { message.toString() }
                )

                if (context.isPlayerSender()) {
                    DistExecutor.runWhenOn(Dist.CLIENT) {
                        Runnable {
                            Minecraft.getInstance().ingameGUI.chatGUI.clearChatMessages(true)
                        }
                    }
                }
            } else {
                sendMsg("chat", context.source, "chat.clear_restricted")
                return 0
            }
        }
        return 0
    }
}
