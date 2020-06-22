package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import com.mairwunnx.projectessentials.core.api.v1.extensions.capitalizeWords
import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionName
import net.minecraft.entity.player.ServerPlayerEntity

object WorldVariable : IChatVariable {
    override val variable = "world"
    override fun process(
        player: ServerPlayerEntity
    ) = player.currentDimensionName.replace("_", " ").capitalizeWords()
}
