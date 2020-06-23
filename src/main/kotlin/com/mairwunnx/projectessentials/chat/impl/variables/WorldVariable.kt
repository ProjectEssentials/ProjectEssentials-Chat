package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import com.mairwunnx.projectessentials.core.api.v1.extensions.capitalizeWords
import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionName
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentUtils

object WorldVariable : IChatVariable {
    override val variable = "world"
    override fun process(
        player: ServerPlayerEntity
    ): ITextComponent = TextComponentUtils.toTextComponent {
        player.currentDimensionName
            .dropWhile { it == ':' }.drop(1)
            .replace("_", " ")
            .capitalizeWords()
    }
}
