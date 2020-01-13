package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.chat.models.ChatModelUtils
import com.mairwunnx.projectessentialscore.extensions.empty
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import net.minecraft.util.Tuple
import net.minecraftforge.event.ServerChatEvent

object ChatUtils {
    fun processBlockedWords(event: ServerChatEvent): Tuple<Boolean, String> {
        val blockedWords = ChatModelUtils.chatModel.moderation.blockedWords
        val blockedWordsMask = ChatModelUtils.chatModel.moderation.blockedWordsMask
        val modifyBlockedWords = ChatModelUtils.chatModel.moderation.modifyBlockedWords

        var fixedMessage = event.message

        blockedWords.forEach {
            if (fixedMessage.toLowerCase().contains(Regex(it, RegexOption.IGNORE_CASE)) ||
                fixedMessage.toLowerCase().matches(Regex(it, RegexOption.IGNORE_CASE)) ||
                fixedMessage.toLowerCase().contains(it, true)
            ) {
                if (hasPermission(
                        event.player, "ess.chat.blockedwords.bypass", 3
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
        if (hasPermission(
                event.player, "ess.chat.blockedchars.bypass", 3
            )
        ) return true

        val blockedChars = ChatModelUtils.chatModel.moderation.blockedChars
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
        if (hasPermission(
                event.player, "ess.chat.messagelength.bypass", 4
            )
        ) return true

        val maxLength = ChatModelUtils.chatModel.moderation.maxMessageLength
        if (event.message.length > maxLength) {
            sendMsg(
                "chat", event.player.commandSource, "chat.message_maxlength"
            )
            return false
        }
        return true
    }

    fun isGlobalChat(event: ServerChatEvent): Boolean = event.message.startsWith('!')

    fun isCommonChat(): Boolean = !ChatModelUtils.chatModel.messaging.enableRangedChat

    fun getMessagePattern(event: ServerChatEvent): String {
        return when {
            !ChatModelUtils.chatModel.messaging.enableRangedChat -> ChatModelUtils.chatModel.messaging.messageCommonPattern
            isGlobalChat(event) -> ChatModelUtils.chatModel.messaging.messageGlobalPattern
            else -> ChatModelUtils.chatModel.messaging.messageLocalPattern
        }
    }

    fun getMessageColor(event: ServerChatEvent): String {
        val messageRegex = Regex("(&.|§.){1,2}%message")
        when {
            isCommonChat() -> {
                val targetVariable = messageRegex.find(
                    ChatModelUtils.chatModel.messaging.messageCommonPattern
                )
                return getMessageVariable(targetVariable!!, "§f")
            }
            else -> return if (isGlobalChat(event)) {
                val targetVariable = messageRegex.find(
                    ChatModelUtils.chatModel.messaging.messageGlobalPattern
                )

                getMessageVariable(targetVariable!!, "§f")
            } else {
                val targetVariable = messageRegex.find(
                    ChatModelUtils.chatModel.messaging.messageLocalPattern
                )
                getMessageVariable(targetVariable!!, "§7§o")
            }
        }
    }

    private fun getMessageVariable(matchResult: MatchResult, callback: String): String {
        return matchResult.groups[0]?.value
            ?.removeRange(
                matchResult.groups[0]?.value!!.lastIndexOf("%"),
                matchResult.groups[0]?.value!!.length
            )
            ?.replace("&", "§") ?: callback
    }
}
