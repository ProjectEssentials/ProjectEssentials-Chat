package com.mairwunnx.projectessentials.chat.models

import com.mairwunnx.projectessentials.core.helpers.MOD_CONFIG_FOLDER
import com.mairwunnx.projectessentials.core.helpers.jsonInstance
import org.apache.logging.log4j.LogManager
import java.io.File

object ChatModelUtils {
    private val chatConfig = MOD_CONFIG_FOLDER + File.separator + "chat.json"
    private val logger = LogManager.getLogger()
    var chatModel = ChatModel()

    fun loadData() {
        logger.info("Loading chat configuration")
        if (!File(chatConfig).exists()) {
            logger.warn("Chat configuration file not exist! creating it now!")
            File(MOD_CONFIG_FOLDER).mkdirs()
            val defaultConfig = jsonInstance.stringify(
                ChatModel.serializer(), chatModel
            )
            File(chatConfig).writeText(defaultConfig)
        }
        val warpsConfigRaw = File(chatConfig).readText()
        chatModel = jsonInstance.parse(ChatModel.serializer(), warpsConfigRaw)
    }

    fun saveData() {
        File(MOD_CONFIG_FOLDER).mkdirs()
        val spawnConfig = jsonInstance.stringify(
            ChatModel.serializer(), chatModel
        )
        File(chatConfig).writeText(spawnConfig)
    }
}
