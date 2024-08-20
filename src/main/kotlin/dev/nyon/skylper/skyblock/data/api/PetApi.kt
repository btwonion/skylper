package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.ScreenOpenEvent
import dev.nyon.skylper.extensions.event.SetItemEvent
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.models.Pet
import dev.nyon.skylper.skyblock.models.Rarity

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
        val storedPet = pets.find { it.name == name } ?: return
        pets.forEach { it.enabled = false }
        level?.let { storedPet.level = it }
        storedPet.enabled = true
        currentPet = storedPet
    }

    private val petTitleRegex get() = regex("menu.pets.title")
    private val petNameRegex get() = regex("menu.pets.name")
    private val petEnabledRegex get() = regex("menu.pets.enabled")

    @Suppress("unused")
    private val initPetsScreenListener = listenInfoEvent<ScreenOpenEvent> {
        if (petTitleRegex.matches(screen.title.string.clean())) pets.clear()
    }

    @Suppress("unused")
    private val setItemListener = listenInfoEvent<SetItemEvent> {
        if (!petTitleRegex.matches(rawScreenTitle)) return@listenInfoEvent
        val itemName = itemStack.nameAsString
        val lore = itemStack.rawLore
        val enabled = lore.any { petEnabledRegex.matches(it) }
        val rarityChar = itemStack.displayName.string.dropWhile { it != ']' }.drop(3).firstOrNull() ?: return@listenInfoEvent
        val rarity = Rarity.byColor(rarityChar) ?: return@listenInfoEvent
        petNameRegex.groups(itemName)?.let {
            val level = it.getOrNull(0)?.toIntOrNull() ?: return@let
            val name = it.getOrNull(1) ?: return@let
            val pet = Pet(name, level, rarity, enabled)
            pets.add(pet)
            if (enabled) currentPet = pet
        }
    }
}