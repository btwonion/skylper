package dev.nyon.skylper.skyblock.data.online

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.world.phys.Vec3

object Locations : OnlineData<LocationsData>(LocationsData::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "locations.json"

    var locations: LocationsData = LocationsData(listOf())

    override fun setData(data: LocationsData?) {
        locations = data ?: return
    }
}

@Serializable
data class LocationsData(val metalDetectorChestOffsets: List<@Contextual Vec3>)