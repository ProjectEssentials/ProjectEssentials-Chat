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
            if (event.message.contains(Regex(it, RegexOption.IGNORE_CASE)) ||
                event.message.matches(Regex(it, RegexOption.IGNORE_CASE)) ||
                event.message.contains(it, true)
            ) {
                if (PermissionsAPI.hasPermission(
                        event.username, "ess.chat.blockedwords.bypass"
                    )
                ) return Tuple(true, fixedMessage)

                if (modifyBlockedWords) {
                    if (blockedWordsMask.isNotEmpty()) {
                        val newMessage = fixedMessage.replace(it, blockedWordsMask)
                        fixedMessage = newMessage
                    }
                } else {
                    sendMsg(
                        "chat",
                        event.player.commandSource,
                        "ess.chat.blocked_word",
                        it
                    )
                    return Tuple(false, String.empty)
                }
            }
        }
        return Tuple(true, fixedMessage)
    }
}
