package com.mairwunnx.projectessentials.chat.impl.parsers

import com.mairwunnx.projectessentials.chat.api.parser.IMessageParser
import com.mairwunnx.projectessentials.chat.api.validator.ChatValidatorAPI
import com.mairwunnx.projectessentials.chat.api.variables.ChatVariableAPI
import com.mairwunnx.projectessentials.chat.chatSettingsConfiguration
import com.mairwunnx.projectessentials.core.api.v1.extensions.empty
import com.mairwunnx.projectessentials.core.api.v1.extensions.playSound
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentUtils

internal class DefaultMessageParser : IMessageParser {
    override fun parse(sender: ServerPlayerEntity, message: String): ITextComponent {
        val component = TextComponentUtils.toTextComponent { String.empty }
        val builder = StringBuilder()
        val common = !chatSettingsConfiguration.messaging.enableRangedChat
        val global = ChatValidatorAPI.validator.isGlobalChat(message) && !common
        return when {
            common -> chatSettingsConfiguration.messaging.messageCommonPattern
            global -> chatSettingsConfiguration.messaging.messageGlobalPattern
            else -> chatSettingsConfiguration.messaging.messageLocalPattern
        }.replace("&", "§").let { pattern ->
            val matches = Regex("(?!:.*)(%\\w+%)*").findAll(pattern).toList()
            val variables = matches.count()
            builder.append(pattern[0])
            repeat(variables) { index ->
                matches[index].apply {
                    if (value.isBlank()) {
                        try {
                            builder.append(pattern[range.first])
                        } catch (ex: IndexOutOfBoundsException) {
                            // Ignored.
                        }
                    } else if (value in ChatVariableAPI.all().map { "%${it.variable}%" }) {
                        component.appendSibling(TextComponentUtils.toTextComponent { builder.toString() })
                        component.appendSibling(
                            ChatVariableAPI.all().find {
                                it.variable == value.drop(1).dropLast(1)
                            }?.process(sender) ?: TextComponentUtils.toTextComponent { "" }
                        ).also { builder.clear() }
                    } else if (value == "%message%") {
                        component.appendSibling(getMessage(sender, message))
                    } else builder.append(String.empty)
                }.run {
                    component.appendSibling(
                        TextComponentUtils.toTextComponent { builder.toString() }
                    ).also { builder.clear() }
                    println(component.unformattedComponentText)
                }
            }.let {
                println("Result: ${component.unformattedComponentText}")
                component
            }
        }
    }

    private fun getMessage(sender: ServerPlayerEntity, message: String): ITextComponent {
        var newMessage = message

        fun default() = TextComponentUtils.toTextComponent { message }

        if (message.first() == '!') message.drop(1)
        if (!hasPermission(sender, "ess.chat.mention", 0)) return default()

        if (chatSettingsConfiguration.mentions.mentionsEnabled) {
            val mentions = mutableListOf<String>()
            val anFormat = chatSettingsConfiguration.mentions.mentionAtFormat.replace("&", "§")
            val nameFormat = chatSettingsConfiguration.mentions.mentionNameFormat.replace("&", "§")

            Regex("@\\S[a-zA-Z0-9]*", RegexOption.IGNORE_CASE).findAll(message).forEach {
                if (it.value != "@e" && it.value != "@a" && it.value != "@p" && it.value != "@r" && it.value != "@s") {
                    mentions.add(it.value)
                }
            }

            mentions.forEach {
                newMessage = message.replace(
                    it, "$anFormat@$nameFormat${it.drop(1)}§r"
                )
            }

            if (mentions.isNotEmpty()) {
                if (chatSettingsConfiguration.mentions.mentionInActionBar) {
                    if ("@${chatSettingsConfiguration.mentions.mentionsAllLiteral}" in mentions) {
                        if (chatSettingsConfiguration.mentions.mentionsAllEnabled) {
                            if (hasPermission(sender, "ess.chat.mention.all", 3)) {
                                sender.server.playerList.players.filter {
                                    it.name.string != sender.name.string
                                }.forEach { notify(it, sender) }
                            }
                        }
                    } else {
                        sender.server.playerList.players.filter {
                            "@${it.name.string}" in mentions
                        }.forEach { notify(it, sender) }
                    }
                }
            }

            return TextComponentUtils.toTextComponent { newMessage }
        }
        return default()
    }

    private fun notify(player: ServerPlayerEntity, sender: ServerPlayerEntity) =
        player.sendStatusMessage(
            TextComponentUtils.toTextComponent {
                chatSettingsConfiguration.mentions.mentionMessage.replace(
                    "%player", sender.name.string
                ).replace("&", "§")
            }, true
        ).also {
            if (!chatSettingsConfiguration.mentions.mentionWithSound) return@also
            player.playSound(player, SoundEvents.UI_TOAST_IN)
        }
}
