package com.mairwunnx.projectessentials.chat.commands

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

object ClearChatCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            literal<CommandSource>("clear-chat").executes {
                return@executes execute(it, false)
            }.then(
                Commands.argument(
                    "only for you", BoolArgumentType.bool()
                ).executes {
                    return@executes execute(it, BoolArgumentType.getBool(it, "only for you"))
                }
            )
        )
    }

    private fun execute(
        context: CommandContext<CommandSource>,
        clearOnlyForSender: Boolean
    ): Int {
        if (clearOnlyForSender) {
            DistExecutor.runWhenOn(Dist.CLIENT) {
                Runnable {
                    Minecraft.getInstance().ingameGUI.chatGUI.clearChatMessages(true)
                }
            }
        } else {
            val message = StringBuilder("")
            repeat(256) {
                message.append("\n")
            }

            context.source.server.playerList.sendMessage(
                TextComponentUtils.toTextComponent { message.toString() }
            )

            DistExecutor.runWhenOn(Dist.CLIENT) {
                Runnable {
                    Minecraft.getInstance().ingameGUI.chatGUI.clearChatMessages(true)
                }
            }
        }
        return 0
    }
}
