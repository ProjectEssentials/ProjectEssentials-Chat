package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.models.ChatModelBase
import com.mairwunnx.projectessentialscore.extensions.empty
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import net.minecraft.util.Tuple
import net.minecraftforge.event.ServerChatEvent

object ChatUtils {
    fun processBlockedWords(event: ServerChatEvent): Tuple<Boolean, String> {
        val blockedWords = ChatModelBase.chatModel.moderation.blockedWords
        val blockedWordsMask = ChatModelBase.chatModel.moderation.blockedWordsMask
        val modifyBlockedWords = ChatModelBase.chatModel.moderation.modifyBlockedWords

        var fixedMessage = event.message

        blockedWords.forEach {
            if (fixedMessage.toLowerCase().contains(Regex(it, RegexOption.IGNORE_CASE)) ||
                fixedMessage.toLowerCase().matches(Regex(it, RegexOption.IGNORE_CASE)) ||
                fixedMessage.toLowerCase().contains(it, true)
            ) {
                if (PermissionsAPI.hasPermission(
                        event.username, "ess.chat.blockedwords.bypass"
                    )
                ) return Tuple(true, fixedMessage)

                if (modifyBlockedWords) {
                    if (blockedWordsMask.isNotEmpty()) {
                        val newMessage = fixedMessage.replace(it, blockedWordsMask).replace(
                            Regex(it, RegexOption.IGNORE_CASE), blockedWordsMask
                        )
                        fixedMessage = newMessage
                    }
                } else {
                    sendMsg(
                        "chat",
                        event.player.commandSource,
                        "chat.blocked_word",
                        it
                    )
                    return Tuple(false, String.empty)
                }
            }
        }
        return Tuple(true, fixedMessage)
    }

    fun processBlockedChars(event: ServerChatEvent): Boolean {
        if (PermissionsAPI.hasPermission(
                event.username, "ess.chat.blockedchars.bypass"
            )
        ) return true

        val blockedChars = ChatModelBase.chatModel.moderation.blockedChars
        blockedChars.forEach {
            if (event.message.contains(it)) {
                sendMsg(
                    "chat",
                    event.player.commandSource,
                    "chat.blocked_char"
                )
                return false
            }
        }
        return true
    }

    fun processMessageLength(event: ServerChatEvent): Boolean {
        if (PermissionsAPI.hasPermission(
                event.username, "ess.chat.messagelength.bypass"
            )
        ) return true

        val maxLength = ChatModelBase.chatModel.moderation.maxMessageLength
        if (event.message.length > maxLength) {
            sendMsg(
                "chat", event.player.commandSource, "chat.message_maxlength"
            )
            return false
        }
        return true
    }

    fun isGlobalChat(event: ServerChatEvent): Boolean = event.message.startsWith('!')

    fun isCommonChat(): Boolean = !ChatModelBase.chatModel.messaging.enableRangedChat

    fun getMessagePattern(event: ServerChatEvent): String {
        return when {
            !ChatModelBase.chatModel.messaging.enableRangedChat -> ChatModelBase.chatModel.messaging.messageCommonPattern
            isGlobalChat(event) -> ChatModelBase.chatModel.messaging.messageGlobalPattern
            else -> ChatModelBase.chatModel.messaging.messageLocalPattern
        }
    }
}
