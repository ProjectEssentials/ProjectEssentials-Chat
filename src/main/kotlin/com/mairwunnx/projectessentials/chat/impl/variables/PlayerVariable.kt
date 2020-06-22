package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity

object PlayerVariable : IChatVariable {
    override val variable = "player"
    override fun process(player: ServerPlayerEntity) = player.gameProfile.name!!
}
