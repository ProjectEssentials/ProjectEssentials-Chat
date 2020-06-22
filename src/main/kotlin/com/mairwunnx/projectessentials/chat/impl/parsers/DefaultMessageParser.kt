package com.mairwunnx.projectessentials.chat.impl.parsers

import com.mairwunnx.projectessentials.chat.api.parser.IMessageParser
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent

class DefaultMessageParser : IMessageParser {
    override fun parse(sender: ServerPlayerEntity, message: ITextComponent): String {
        TODO("Not yet implemented")
    }
}
