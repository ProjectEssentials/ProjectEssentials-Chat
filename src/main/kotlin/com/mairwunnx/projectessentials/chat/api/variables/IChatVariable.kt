package com.mairwunnx.projectessentials.chat.api.variables

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.text.ITextComponent

/**
 * Interface for chat variable, (basically for replacing
 * chat variables which starts with `%` symbol.)
 * @since 2.0.0.
 */
interface IChatVariable {
    /**
     * Chat variable literal, without `%` symbol.
     * @since 2.0.0.
     */
    val variable: String

    /**
     * @param player player to process this.
     * @return variable result.
     * @since 2.0.0.
     */
    fun process(player: ServerPlayerEntity): ITextComponent
}
