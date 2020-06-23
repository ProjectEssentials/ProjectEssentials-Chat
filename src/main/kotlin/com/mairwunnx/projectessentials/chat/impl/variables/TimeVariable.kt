package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.event.HoverEvent
import java.time.ZonedDateTime.now

object TimeVariable : IChatVariable {
    override val variable = "time"
    override fun process(
        player: ServerPlayerEntity
    ): ITextComponent = TextComponentUtils.toTextComponent {
        now().toLocalTime().toString().replace(Regex("\\..*"), "")
    }.setStyle(
        Style().apply {
            hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                TextComponentUtils.toTextComponent { "Message sent at ${now().toLocalTime()}" }
            )
        }
    )
}
