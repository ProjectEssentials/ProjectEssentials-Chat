package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.core.configuration.localization.LocalizationConfigurationUtils
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.localization.sendMsgV2
import net.minecraft.command.CommandSource

internal fun sendMessage(
    source: CommandSource,
    message: String,
    vararg args: String
) {
    if (LocalizationConfigurationUtils.getConfig().enabled) {
        sendMsgV2(
            source.asPlayer(),
            "project_essentials_chat.$message", *args
        )
    } else {
        sendMsg(
            "chat", source, message, *args
        )
    }
}
