package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity

object DisplayNameVariable : IChatVariable {
    override val variable = "display_name"
    override fun process(player: ServerPlayerEntity): String = player.displayName.formattedText
}
