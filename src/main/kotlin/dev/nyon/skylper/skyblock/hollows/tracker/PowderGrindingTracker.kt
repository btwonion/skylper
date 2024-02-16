package dev.nyon.skylper.skyblock.hollows.tracker

import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.render.tabhud.PowderGrindingWidget
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

object PowderGrindingTracker {
    private const val DOUBLE_POWDER_STARTED_MESSAGE = "2X POWDER STARTED!"
    private const val DOUBLE_POWDER_ENDED_MESSAGE = "2X POWDER ENDED!"
    private const val UNCOVER_CHEST_MESSAGE = "You uncovered a treasure chest!"
    private const val GEMSTONE_PATTERN = "Gemstone Powder: "
    private const val MITHRIL_PATTERN = "Mithril Powder: "
    private const val RECEIVED_REWARD_MESSAGE = "You received "

    private var originalGemstonePowder: Int? = null
    private var originalMithrilPowder: Int? = null

    var openedChests: Int = 0
    var miningStart: Instant? = null
    var farmedMithrilPowder: Int = 0
    var farmedGemstonePowder: Int = 0
    var lastChestOpened: Instant? = null
    var doublePowderEnabled: Boolean = false

    val mithrilPowderPerMinute: Int
        get() {
            val farmedTime = Clock.System.now() - (miningStart ?: return 0)
            val seconds = max(farmedTime.inWholeSeconds, 1)
            return (farmedMithrilPowder / seconds).toInt() * 60
        }

    val mithrilPowderPerHour: Int
        get() {
            return mithrilPowderPerMinute * 60
        }

    val gemstonePowderPerMinute: Int
        get() {
            val farmedTime = Clock.System.now() - (miningStart ?: return 0)
            val seconds = max(farmedTime.inWholeSeconds, 1)
            return (farmedGemstonePowder / seconds).toInt() * 60
        }

    val gemstonePowderPerHour: Int
        get() {
            return gemstonePowderPerMinute * 60
        }

    fun init() {
        listenChat()
        listenWorldLoad()
    }

    private fun listenChat() = listenEvent<MessageEvent> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        if (it.text.string.contains(DOUBLE_POWDER_STARTED_MESSAGE)) doublePowderEnabled = true
        if (it.text.string.contains(DOUBLE_POWDER_ENDED_MESSAGE)) doublePowderEnabled = false
        if (it.text.string.contains(UNCOVER_CHEST_MESSAGE)) {
            val currentTime = Clock.System.now()
            if (lastChestOpened != null && currentTime - lastChestOpened!! >= 5.minutes) reset()
            if (miningStart == null) miningStart = currentTime
            lastChestOpened = currentTime
            openedChests++
        }

        updatePowderScores()
        PowderGrindingWidget.update()
    }

    private fun updatePowderScores() = independentScope.launch {
        delay(50.milliseconds)
        minecraft.connection?.onlinePlayers?.forEach { entry ->
            val string = entry.tabListDisplayName?.string ?: return@forEach
            if (string.contains(MITHRIL_PATTERN)) string.replace(MITHRIL_PATTERN, "").replace(" ", "").replace(",", "")
                .toIntOrNull()?.also { currentMithril ->
                    if (originalMithrilPowder == null) originalMithrilPowder = currentMithril
                    farmedMithrilPowder = currentMithril - (originalMithrilPowder ?: return@forEach)
                }

            if (string.contains(GEMSTONE_PATTERN)) string.replace(GEMSTONE_PATTERN, "").replace(" ", "")
                .replace(",", "").toIntOrNull()?.also { currentGemstone ->
                    if (originalGemstonePowder == null) originalGemstonePowder = currentGemstone
                    farmedGemstonePowder = currentGemstone - (originalGemstonePowder ?: return@forEach)
                }
        }
    }

    private fun listenWorldLoad() = listenEvent<LevelChangeEvent> { reset() }

    private fun reset() {
        openedChests = 0
        miningStart = null
        farmedMithrilPowder = 0
        farmedGemstonePowder = 0
        lastChestOpened = null
        doublePowderEnabled = false
        originalMithrilPowder = null
        originalGemstonePowder = null
        PowderGrindingWidget.update()
    }
}