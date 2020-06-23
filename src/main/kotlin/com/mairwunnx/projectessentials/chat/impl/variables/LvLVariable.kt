package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentUtils

object LvLVariable : IChatVariable {
    override val variable = "lvl"
    override fun process(
        player: ServerPlayerEntity
    ): ITextComponent = TextComponentUtils.toTextComponent { player.experienceLevel.toString() }
}
