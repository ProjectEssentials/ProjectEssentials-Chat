package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.event.ClickEvent

object DisplayNameVariable : IChatVariable {
    override val variable = "display_name"
    override fun process(player: ServerPlayerEntity): ITextComponent = player.displayName.setStyle(
        Style().apply {
            clickEvent = ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND, "/m ${player.gameProfile.name!!}"
            )
        }
    )
}
