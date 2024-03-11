package hollows

import dev.nyon.skylper.skyblock.mining.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.mining.hollows.locations.PlayerChatLocationListener
import net.minecraft.world.phys.Vec3
import kotlin.test.Test

// Actually was used for debugging - gives no result --> but it works
class PlayerChatParserTest {
    private val regex =
        "^.*?(?<Num>-?\\d{1,3})(?:[,\\s]*(?<Num2>-?\\d{1,3}))?(?:[,\\s]*(?<Num3>-?\\d{1,3}))?.*?\$".toRegex()

    @Test
    fun testParsePlayerMessages() {
        val allCoordsTest = "jungle temple 320 110 290"
        val onlyXZCoordsTest = "jungle temple 320 290"
        val withoutNameTest = "320 110 290"

        println("test $allCoordsTest")
        listenForStructureCoords(allCoordsTest)
        println("test $onlyXZCoordsTest")
        listenForStructureCoords(onlyXZCoordsTest)
        println("test $withoutNameTest")
        listenForStructureCoords(withoutNameTest)
    }

    private fun listenForStructureCoords(rawMessage: String) {
        val found = regex.find(rawMessage)
        if (found == null) println("null")

        val loc = found?.groupValues?.toMutableList()
            .also { set -> set?.removeIf { it.isEmpty() || it.toDoubleOrNull() == null } }
            ?.let {
                when (it.size) {
                    2 -> PlayerChatLocationListener.RawLocation(it[0].toDouble(), null, it[1].toDouble())
                    3 -> PlayerChatLocationListener.RawLocation(it[0].toDouble(), it[1].toDouble(), it[2].toDouble())
                    else -> return
                }
            } ?: return

        println("loc: $loc")

        handleRawLocation(loc, rawMessage)
    }

    private fun handleRawLocation(location: PlayerChatLocationListener.RawLocation, rawMessage: String) {
        val matchingStructure = HollowsStructure.entries.find { rawMessage.contains(it.displayName, true) }
        if (matchingStructure != null) {
            println(matchingStructure)
            val pos = Vec3(
                location.x, location.y ?: matchingStructure.minY.toDouble(), location.z
            )
            println(pos)
            return
        }

        HollowsStructure.entries.forEach { structure ->
            println("possibility: $structure")
        }
    }
}