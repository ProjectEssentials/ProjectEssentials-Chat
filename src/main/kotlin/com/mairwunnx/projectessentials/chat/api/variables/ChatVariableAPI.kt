package com.mairwunnx.projectessentials.chat.api.variables

import org.apache.logging.log4j.LogManager.getLogger
import java.util.*

/**
 * Chat variable store api, contains all variables to
 * process in chat.
 * @since 2.0.0.
 */
object ChatVariableAPI {
    private val store = Collections.synchronizedList<IChatVariable>(mutableListOf())

    /**
     * Adds new chat variable to variable store.
     *
     * If chat variable already exist and registered
     * adding the same variable will canceled.
     *
     * @param variable chat variable to add.
     * @since 2.0.0.
     */
    fun add(variable: IChatVariable) = synchronized(store) {
        store.find { it.variable == variable.variable }?.let {
            getLogger().error(
                "Chat variable `${variable.javaClass.canonicalName}` (${variable.variable}) already exist."
            )
        } ?: run {
            store.add(variable).also {
                getLogger().debug("Chat variable (${variable.variable}) added.")
            }.let { return@synchronized }
        }
    }

    /**
     * @return all registered chat variables.
     * @since 2.0.0.
     */
    fun all() = store.toList().asSequence()
}
