package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.ScreenOpenEvent
import dev.nyon.skylper.extensions.event.SetItemEvent
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.models.Pet

object PetApi {
    val pets: MutableList<Pet> get() = currentProfile.pets
    var currentPet: Pet? = null

    private val summonedPetRegex get() = regex("chat.general.pet.summoned")
    private val autoPetRegex get() = regex("chat.general.pet.autopet")
    private val levelUpRegex get() = regex("chat.general.pet.levelup")

    @Suppress("unused")
    private val messageListener = listenInfoEvent<MessageEvent> {
        summonedPetRegex.singleGroup(rawText)?.let { foundName ->
            petFound(foundName, null)
        }

        autoPetRegex.groups(rawText)?.let {
            val name = it.getOrNull(0) ?: return@let
            val level = it.getOrNull(1)?.toIntOrNull() ?: return@let
            petFound(name, level)
        }

        levelUpRegex.groups(rawText)?.let {
            val name = it.getOrNull(0) ?: return@let
            val level = it.getOrNull(1)?.toIntOrNull() ?: return@let
            petFound(name, level)
        }
    }

    private fun petFound(name: String, level: Int?) {
        val storedPet = pets.find { it.type == name } ?: return
        pets.forEach { it.active = false }
        level?.let { storedPet.level = it }
        storedPet.active = true
        currentPet = storedPet
    }

    private val petTitleRegex get() = regex("menu.pets.title")
    private val petNameRegex get() = regex("menu.pets.name")

    @Suppress("unused")
    private val initPetsScreenListener = listenInfoEvent<ScreenOpenEvent> {
        if (petTitleRegex.matches(screen.title.string.clean())) pets.clear()
    }

    @Suppress("unused")
    private val setItemListener = listenInfoEvent<SetItemEvent> {
        if (!petTitleRegex.matches(rawScreenTitle)) return@listenInfoEvent
        val itemName = itemStack.nameAsString
        println(itemName.matches(petNameRegex))
        val level = petNameRegex.singleGroup(itemName)?.toIntOrNull() ?: return@listenInfoEvent
        val petInfo = itemStack.compoundTag?.getCompound("petInfo") ?: return@listenInfoEvent
        val pet = json.decodeFromString<Pet>(petInfo.toString())
        if (pet.active) currentPet = pet
        pet.level = level
        pets.add(pet)
    }
}