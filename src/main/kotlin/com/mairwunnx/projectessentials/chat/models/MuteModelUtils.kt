package com.mairwunnx.projectessentials.chat.models

import com.mairwunnx.projectessentials.chat.api.MuteAPI
import com.mairwunnx.projectessentials.core.helpers.MOD_CONFIG_FOLDER
import com.mairwunnx.projectessentials.core.helpers.jsonInstance
import org.apache.logging.log4j.LogManager
import java.io.File

object MuteModelUtils {
    private val mutedPlayersConfig = MOD_CONFIG_FOLDER + File.separator + "muted-players.json"
    private val logger = LogManager.getLogger()
    var muteModel = MuteModel()

    fun loadData() {
        logger.info("Loading muted players configuration")
        if (!File(mutedPlayersConfig).exists()) {
            logger.warn("Muted players configuration file not exist! creating it now!")
            File(MOD_CONFIG_FOLDER).mkdirs()
            val defaultConfig = jsonInstance.stringify(
                MuteModel.serializer(), muteModel
            )
            File(mutedPlayersConfig).writeText(defaultConfig)
        }
        val mutedConfigRaw = File(mutedPlayersConfig).readText()
        muteModel = jsonInstance.parse(MuteModel.serializer(), mutedConfigRaw)
        processData()
    }

    private fun processData() {
        muteModel.players.forEach {
            MuteAPI.mutePlayer(it.name, it.mutedBy, it.reason, false)
        }
    }

    fun saveData() {
        File(MOD_CONFIG_FOLDER).mkdirs()
        val mutedConfig = jsonInstance.stringify(
            MuteModel.serializer(), muteModel
        )
        File(mutedConfig).writeText(mutedConfig)
    }

    fun addPlayer(name: String, mutedBy: String, reason: String) {
        removePlayer(name)
        muteModel.players.add(MuteModel.Player(name, mutedBy, reason))
    }

    fun removePlayer(name: String) {
        muteModel.players.removeIf {
            it.name == name
        }
    }

    fun removeAll() = muteModel.players.clear()
}
