package com.mairwunnx.projectessentials.chat.impl.handlers

import com.mairwunnx.projectessentials.chat.api.ChatMuteAPI
import com.mairwunnx.projectessentials.chat.api.ChatSlowmodeAPI
import com.mairwunnx.projectessentials.chat.api.parser.ChatParserAPI
import com.mairwunnx.projectessentials.chat.api.validator.ChatValidatorAPI
import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration
import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimension
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import net.minecraft.network.play.server.SChatPacket
import net.minecraft.util.text.ChatType
import net.minecraftforge.event.ServerChatEvent

internal object ChatMessageHandler {
    private inline fun ServerChatEvent.cancelAndReturn(action: () -> Unit) {
        run { this.isCanceled = true }.also { action() }
    }

    fun handle(event: ServerChatEvent) {
        var message = event.message

        fun out(action: String, vararg args: String) = MessagingAPI.sendMessage(
            event.player, "${MESSAGE_MODULE_PREFIX}chat.$action", args = *args
        )

        if (!chatSettingsConfiguration.messaging.chatEnabled) {
            if (!hasPermission(event.player, "ess.chat.disabled.except", 4)) {
                out("disabled").run { event.cancelAndReturn { return } }
            }
        }
        if (!hasPermission(event.player, "ess.chat", 0)) {
            out("restricted").run { event.cancelAndReturn { return } }
        }

        if (ChatMuteAPI.isInMute(event.player.name.string, event.player.uniqueID.toString())) {
            val entry = ChatMuteAPI.getMutePlayer(
                event.player.name.string, event.player.uniqueID.toString()
            )

            fun reason() = entry?.reason?.replace(" ", " §7")
                ?: chatSettingsConfiguration.mute.defaultReason.replace(" ", " §7")

            out(
                if (entry?.isTemp == true) "tempmuted" else "muted",
                entry?.mutedBy ?: "#server", reason()
            ).run { event.cancelAndReturn { return } }
        }

        if (chatSettingsConfiguration.moderation.messagingSlowMode > 0) {
            if (!hasPermission(event.player, "ess.chat.slowmode.except", 3)) {
                val isExpired = ChatSlowmodeAPI.isExpired(
                    event.player.name.string,
                    chatSettingsConfiguration.moderation.messagingSlowMode
                )
                if (isExpired) {
                    ChatSlowmodeAPI.action(event.player.name.string)
                } else {
                    val left = chatSettingsConfiguration.moderation.messagingSlowMode.minus(
                        ChatSlowmodeAPI.passed(event.player.name.string)
                    ).toString()
                    out("slowmode", left).run { event.cancelAndReturn { return } }
                }
            }
        }

        if (chatSettingsConfiguration.filters.filterAdvertising) {
            if (!hasPermission(event.player, "ess.chat.filter.advertising.bypass", 3)) {
                if (ChatValidatorAPI.validator.isContainsAdvertising(message)) {
                    out("advertising").run { event.cancelAndReturn { return } }
                }
            }
        }

        if (chatSettingsConfiguration.filters.filterChars) {
            if (!hasPermission(event.player, "ess.chat.filter.chars.bypass", 3)) {
                if (
                    ChatValidatorAPI.validator.isContainsBlockedChars(message) ||
                    !ChatValidatorAPI.validator.isContainsAllowedChars(message)
                ) out("chars").run { event.cancelAndReturn { return } }
            }
        }

        if (chatSettingsConfiguration.filters.filterExceeds) {
            if (!hasPermission(event.player, "ess.chat.filter.length.bypass", 3)) {
                if (ChatValidatorAPI.validator.isMessageExceeds(message)) {
                    out("length").run { event.cancelAndReturn { return } }
                }
            }
        }

        if (chatSettingsConfiguration.filters.filterWords) {
            if (!hasPermission(event.player, "ess.chat.filter.words.bypass", 3)) {
                if (ChatValidatorAPI.validator.isContainsBlockedWord(message)) {
                    if (
                        chatSettingsConfiguration.moderation.modifyBlockedWords &&
                        chatSettingsConfiguration.moderation.blockedWordsMask.isNotEmpty()
                    ) {
                        chatSettingsConfiguration.moderation.blockedWords.forEach {
                            message = message.replace(
                                it, chatSettingsConfiguration.moderation.blockedWordsMask
                            )
                        }
                    } else out("words").run { event.cancelAndReturn { return } }
                }
            }
        }

        if (chatSettingsConfiguration.moderation.colorsAllowed) {
            if ("&" in message) {
                if (hasPermission(event.player, "ess.chat.color", 2)) {
                    out("color").run { event.cancelAndReturn { return } }
                } else {
                    message = message.replace("&", "§")
                }
            }
        } else if ("&" in message) out("color").run { event.cancelAndReturn { return } }

        if (chatSettingsConfiguration.messaging.enableRangedChat) {
            if (!ChatValidatorAPI.validator.isGlobalChat(message)) {
                event.player.server.playerList.sendToAllNearExcept(
                    null,
                    event.player.positionVec.x,
                    event.player.positionVec.y,
                    event.player.positionVec.z,
                    chatSettingsConfiguration.messaging.localChatRange.toDouble(),
                    event.player.currentDimension,
                    SChatPacket(ChatParserAPI.parser.parse(event.player, message), ChatType.SYSTEM)
                ).run { event.cancelAndReturn { return } }
            }
        }
        event.component = ChatParserAPI.parser.parse(event.player, message)
    }
}
