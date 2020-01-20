package com.mairwunnx.projectessentials.chat

import java.time.Duration
import java.time.ZonedDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

object ChatCooldown {
    private const val DEFAULT_COOLDOWN = 5
    private val cooldownMap = hashMapOf<String, ZonedDateTime>()

    fun addCooldown(nickname: String) {
        removeCooldown(nickname)
        cooldownMap[nickname] = ZonedDateTime.now()
    }

    fun removeCooldown(nickname: String) {
        cooldownMap.remove(nickname)
    }

    fun getCooldownIsExpired(
        nickname: String,
        minSecondsDuration: Int = DEFAULT_COOLDOWN
    ): Boolean = getCooldownTimeLeft(nickname) >= minSecondsDuration

    @UseExperimental(ExperimentalTime::class)
    fun getCooldownTimeLeft(nickname: String): Double {
        if (cooldownMap[nickname] != null) {
            val commandExecutionTime = cooldownMap[nickname]
            val dateTimeNow = ZonedDateTime.now()
            val duration = Duration.between(commandExecutionTime, dateTimeNow)
            return duration.toKotlinDuration().inSeconds
        }
        return Double.MAX_VALUE
    }
}
