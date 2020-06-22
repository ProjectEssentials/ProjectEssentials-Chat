package com.mairwunnx.projectessentials.chat.impl.variables

import com.mairwunnx.projectessentials.chat.api.variables.IChatVariable
import net.minecraft.entity.player.ServerPlayerEntity
import java.time.ZonedDateTime.now

object TimeVariable : IChatVariable {
    override val variable = "time"
    override fun process(
        player: ServerPlayerEntity
    ) = now().toLocalTime().toString().replace(Regex("\\..*"), "")
}
