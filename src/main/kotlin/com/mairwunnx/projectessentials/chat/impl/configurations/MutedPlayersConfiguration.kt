package com.mairwunnx.projectessentials.chat.impl.configurations

import com.mairwunnx.projectessentials.core.api.v1.configuration.IConfiguration
import com.mairwunnx.projectessentials.core.api.v1.helpers.jsonInstance
import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import java.time.Duration
import java.time.ZonedDateTime

object MutedPlayersConfiguration : IConfiguration<MutedPlayersConfigurationModel> {
    private val logger = LogManager.getLogger()
    private var configurationData = MutedPlayersConfigurationModel()

    override val name = "muted-players"
    override val version = 1
    override val configuration = take()
    override val path by lazy {
        projectConfigDirectory + File.separator + getCurrentServer().folderName + File.separator + "muted-players.json"
    }

    override fun load() {
        try {
            val configRaw = File(path).readText()
            configurationData = jsonInstance.parse(
                MutedPlayersConfigurationModel.serializer(), configRaw
            )
        } catch (ex: FileNotFoundException) {
            logger.error("Configuration file ($path) not found!")
            logger.warn("The default configuration will be used")
        } finally {
            logger.info("Loaded muted players (${take().entries.count()})")
            logger.debug("Purging data of expired muted players").also {
                take().entries.removeIf {
                    Duration.between(
                        ZonedDateTime.parse(it.timeEnd), ZonedDateTime.now()
                    ).toMillis() >= 1
                }
            }
        }
    }

    override fun save() {
        logger.info("Saving configuration `${name}`")
        val raw = jsonInstance.stringify(
            MutedPlayersConfigurationModel.serializer(), configurationData
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
