package com.mairwunnx.projectessentials.chat

import com.mairwunnx.projectessentialscore.EssBase
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_chat")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-0.1.0.0"
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $modName mod ...")
        MinecraftForge.EVENT_BUS.register(this)
    }

    companion object {
        lateinit var modInstance: EntryPoint
    }
}
