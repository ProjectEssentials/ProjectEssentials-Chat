//package com.mairwunnx.projectessentials.chat.commands
//
//import com.mairwunnx.projectessentials.chat.EntryPoint
//import com.mairwunnx.projectessentials.chat.api.MuteAPI
//import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
//import com.mairwunnx.projectessentials.chat.models.MuteModelUtils
//import com.mairwunnx.projectessentials.chat.sendMessage
//import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
//import com.mairwunnx.projectessentials.core.extensions.playerName
//import com.mojang.brigadier.CommandDispatcher
//import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
//import com.mojang.brigadier.context.CommandContext
//import net.minecraft.command.CommandSource
//import org.apache.logging.log4j.LogManager
//
//object UnmuteAllCommand {
//    private val logger = LogManager.getLogger()
//
//    fun register(dispatcher: CommandDispatcher<CommandSource>) {
//        logger.info("Register \"/unmute-all\" command")
//
//        dispatcher.register(
//            literal<CommandSource>("unmute-all").executes(::execute)
//        )
//    }
//
//    private fun execute(
//        context: CommandContext<CommandSource>
//    ): Int {
//        if (context.isPlayerSender() && !EntryPoint.hasPermission(
//                context.source.asPlayer(),
//                "ess.chat.unmute.all",
//                3
//            )
//        ) {
//            sendMessage(context.source, "chat.unmute_all_restricted")
//            return 0
//        }
//
//        MuteAPI.unmuteAll()
//        MuteModelUtils.removeAll()
//
//        if (ChatModelUtils.chatModel.mute.notifyAllAboutUnmuteAll) {
//            context.source.server.playerList.players.forEach {
//                sendMessage(
//                    it.commandSource, "notify_unmuted_all",
//                    context.playerName()
//                )
//            }
//        }
//
//        sendMessage(context.source, "chat.unmute_all_success")
//        return 0
//    }
//}
