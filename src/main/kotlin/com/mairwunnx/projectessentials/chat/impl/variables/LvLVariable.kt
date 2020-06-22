package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity

object LvLVariable : IChatVariable {
    override val variable = "lvl"
    override fun process(player: ServerPlayerEntity) = player.experienceLevel.toString()
}
