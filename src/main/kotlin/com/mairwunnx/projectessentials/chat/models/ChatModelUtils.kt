package com.mairwunnx.projectessentials.chat.models

import com.mairwunnx.projectessentialscore.helpers.MOD_CONFIG_FOLDER
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.logging.log4j.LogManager
import java.io.File

@UseExperimental(UnstableDefault::class)
object ChatModelUtils {
    private val warpsConfig = MOD_CONFIG_FOLDER + File.separator + "chat.json"
    private val logger = LogManager.getLogger()
    var chatModel = ChatModel()
    private val json = Json(
        JsonConfiguration(
            strictMode = false,
            allowStructuredMapKeys = true,
            prettyPrint = true
        )
    )

    fun loadData() {
        logger.info("Loading chat configuration")
        if (!File(warpsConfig).exists()) {
            logger.warn("Chat configuration file not exist! creating it now!")
            createConfigDirs(MOD_CONFIG_FOLDER)
            val defaultConfig = json.stringify(
                ChatModel.serializer(),
                chatModel
            )
            File(warpsConfig).writeText(defaultConfig)
        }
        val warpsConfigRaw = File(warpsConfig).readText()
        chatModel = json.parse(ChatModel.serializer(), warpsConfigRaw)
        logger.info("Chat configuration loading done")
    }

    fun saveData() {
        logger.info("Saving chat configuration to file")
        createConfigDirs(MOD_CONFIG_FOLDER)
        val spawnConfig = json.stringify(
            ChatModel.serializer(),
            chatModel
        )
        File(warpsConfig).writeText(spawnConfig)
        logger.info("Saving chat configuration done")
    }

    @Suppress("SameParameterValue")
    private fun createConfigDirs(path: String) {
        logger.info("Creating config directory for chat configuration")
        val configDirectory = File(path)
        if (!configDirectory.exists()) configDirectory.mkdirs()
    }
}
