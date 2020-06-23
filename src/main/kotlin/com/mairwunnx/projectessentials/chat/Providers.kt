package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentials.chat.impl.ModuleObject
import com.mairwunnx.projectessentials.chat.impl.configurations.ChatSettingsConfiguration
import com.mairwunnx.projectessentials.chat.impl.configurations.MutedPlayersConfiguration

internal val providers = listOf(
    ChatSettingsConfiguration::class.java,
    MutedPlayersConfiguration::class.java,
    ModuleObject::class.java//,
//    ClearChatCommand::class.java,
//    MuteCommand::class.java,
//    UnmuteCommand::class.java,
//    UnmuteAllCommand::class.java,
//    MutedPlayersCommand::class.java
    // PrefixCommand::class.java,
    // SuffixCommand::class.java
)

internal val localizations = mutableListOf(
    "/assets/projectessentialschat/lang/pt_br.json",
    "/assets/projectessentialschat/lang/zh_cn.json",
    "/assets/projectessentialschat/lang/de_de.json",
    "/assets/projectessentialschat/lang/ru_ru.json",
    "/assets/projectessentialschat/lang/en_us.json",
    "/assets/projectessentialschat/lang/es_cl.json",
    "/assets/projectessentialschat/lang/fr_fr.json",
    "/assets/projectessentialschat/lang/sv_se.json"
)
