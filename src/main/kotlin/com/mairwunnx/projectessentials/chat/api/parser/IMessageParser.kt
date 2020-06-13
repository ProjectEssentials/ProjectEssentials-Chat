package com.mairwunnx.projectessentials.chat.api.parser

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent

/**
 * Base interface for message parsers.
 * @since 2.0.0.
 */
interface IMessageParser {
    /**
     * Parses the message and changes message.
     * @param sender player who sent a message.
     * @param message message represented as [ITextComponent].
     * @return ready to sent message as string.
     * @since 2.0.0.
     */
    fun parse(sender: ServerPlayerEntity, message: ITextComponent): String
}
