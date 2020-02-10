package com.mairwunnx.projectessentials.chat.commands

import com.mairwunnx.projectessentials.chat.EntryPoint
import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TranslationTextComponent
import org.apache.logging.log4j.LogManager

object MutedPlayersCommand {
    private val logger = LogManager.getLogger()
    private val locMessageMutedPlayer = TranslationTextComponent(
        "project_essentials_chat.chat.out_muted_players"
    ).formattedText
    private val locMessageMutedBy = TranslationTextComponent(
        "project_essentials_chat.chat.out_muted_by"
    ).formattedText
    private val locMessageReason = TranslationTextComponent(
        "project_essentials_chat.chat.out_reason"
    ).formattedText
    private val locMessageNone = TranslationTextComponent(
        "project_essentials_chat.chat.out_muted_players_none"
    ).formattedText

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/muted-players\" command")

        dispatcher.register(
            literal<CommandSource>("muted-players").executes(::execute)
        )
    }

    private fun execute(
        context: CommandContext<CommandSource>
    ): Int {
        if (context.isPlayerSender() && !EntryPoint.hasPermission(
                context.source.asPlayer(),
                "ess.chat.muted",
                3
            )
        ) {
            sendMsg("chat", context.source, "chat.muted_restricted")
            return 0
        }

        val mutedPlayers = MuteAPI.getMutedPlayers()
        val message = buildString {
            this.append("§7$locMessageMutedPlayer:\n")
            if (mutedPlayers.isNotEmpty()) {
                mutedPlayers.forEach {
                    val mutedBy = MuteAPI.getMuteInitiator(it)!!
                    val reason = MuteAPI.getMuteReason(it)!!

                    this.append(
                        "§7  - §c$it§7, $locMessageMutedBy §c$mutedBy§7, $locMessageReason: §c$reason"
                    )
                }
            } else {
                this.append("§c$locMessageNone")
            }
        }

        context.source.sendFeedback(
            TextComponentUtils.toTextComponent {
                message
            }, false
        )

        return 0
    }
}
