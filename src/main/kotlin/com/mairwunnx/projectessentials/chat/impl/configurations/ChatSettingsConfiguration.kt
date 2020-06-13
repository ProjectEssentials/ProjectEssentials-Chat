package com.mairwunnx.projectessentials.chat.impl.configurations

import com.mairwunnx.projectessentials.core.api.v1.configuration.IConfiguration
import com.mairwunnx.projectessentials.core.api.v1.helpers.jsonInstance
import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException

object ChatSettingsConfiguration : IConfiguration<ChatSettingsConfigurationModel> {
    private val logger = LogManager.getLogger()
    private var configurationData = ChatSettingsConfigurationModel()

    override val name = "chat-settings"
    override val version = 1
    override val configuration = take()
    override val path = projectConfigDirectory + File.separator + "chat-settings.json"

    override fun load() {
        try {
            val configRaw = File(path).readText()
            configurationData = jsonInstance.parse(
                ChatSettingsConfigurationModel.serializer(), configRaw
            )
        } catch (ex: FileNotFoundException) {
            logger.error("Configuration file ($path) not found!")
            logger.warn("The default configuration will be used")
        }
    }

    override fun save() {
        logger.info("Saving configuration `${name}`")
        val raw = jsonInstance.stringify(
            ChatSettingsConfigurationModel.serializer(), configurationData
        )
        try {
            File(path).writeText(raw)
        } catch (ex: SecurityException) {
            logger.error(
                "An error occurred while saving $name configuration", ex
            )
        }
    }

    override fun take() = configurationData
}
