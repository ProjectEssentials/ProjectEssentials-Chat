package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TextFormatting

object TeamVariable : IChatVariable {
    override val variable = "team"
    override fun process(player: ServerPlayerEntity) =
        { player.team?.color ?: TextFormatting.RESET }.let {
            if (it() == TextFormatting.RESET) {
                return@let TextComponentUtils.toTextComponent { player.team!!.name }.formattedText
            }
            return@let TextComponentUtils.toTextComponent {
                player.team!!.name
            }.applyTextStyle(player.team!!.color).formattedText
        }!!
}
