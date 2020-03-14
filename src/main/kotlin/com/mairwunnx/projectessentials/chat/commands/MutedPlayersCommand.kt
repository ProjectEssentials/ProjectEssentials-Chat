package com.mairwunnx.projectessentials.chat.commands

import com.mairwunnx.projectessentials.chat.EntryPoint
import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.chat.sendMessage
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.localization.getLocalizedString
import com.mojang.authlib.GameProfile
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.util.FakePlayer
import org.apache.logging.log4j.LogManager
import java.util.*

object MutedPlayersCommand {
    private val logger = LogManager.getLogger()
    private const val domain = "project_essentials_chat.chat"
    private var player: ServerPlayerEntity? = null
    private var server: MinecraftServer? = null

    private val fakePlayer by lazy {
        FakePlayer(
            server?.getWorld(DimensionType.OVERWORLD),
            GameProfile(UUID.randomUUID(), "#thisIsBug")
        )
    }

    private val locMessageMutedPlayer = getLocalizedString(
        player ?: fakePlayer, "$domain.out_muted_players"
    )
    private val locMessageMutedBy = getLocalizedString(
        player ?: fakePlayer, "$domain.out_muted_by"
    )
    private val locMessageReason = getLocalizedString(
        player ?: fakePlayer, "$domain.out_reason"
    )
    private val locMessageNone = getLocalizedString(
        player ?: fakePlayer, "$domain.out_muted_players_none"
    )

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/muted-players\" command")

        dispatcher.register(
            literal<CommandSource>("muted-players").executes(::execute)
        )
    }

    private fun execute(
        context: CommandContext<CommandSource>
    ): Int {
        server = context.source.server

        if (context.isPlayerSender() && !EntryPoint.hasPermission(
                context.source.asPlayer(),
                "ess.chat.muted",
                3
            )
        ) {
            sendMessage(context.source, "chat.muted_restricted")
            return 0
        }

        if (context.isPlayerSender()) player = context.source.asPlayer()

        val mutedPlayers = MuteAPI.getMutedPlayers()
        val message = buildString {
            this.append("§7$locMessageMutedPlayer:\n")
            if (mutedPlayers.isNotEmpty()) {
                mutedPlayers.forEach {
                    val mutedBy = MuteAPI.getMuteInitiator(it)!!
                    val reason = MuteAPI.getMuteReason(it)!!.replace(" ", " §c")

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
