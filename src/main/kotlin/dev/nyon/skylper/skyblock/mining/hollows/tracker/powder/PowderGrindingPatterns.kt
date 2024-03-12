package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

object PowderGrindingPatterns {
    val pickedLockPattern = "You have successfully picked the lock on this chest!".toPattern()
    val powderStartedPattern = ".*2X POWDER STARTED!.*".toPattern()
    val powderEndedPattern = ".*2X POWDER ENDED!.*".toPattern()
    val powderBossBarPattern = "§e§lPASSIVE EVENT §b§l2X POWDER §e§lRUNNING FOR §a§l(?<time>.*)".toPattern()
}
