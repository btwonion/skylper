package dev.nyon.skylper.skyblock.menu

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.category
import dev.nyon.skylper.config.screen.extensions.description
import dev.nyon.skylper.config.screen.extensions.field
import dev.nyon.skylper.config.screen.extensions.getSet
import dev.nyon.skylper.config.screen.extensions.primitive
import dev.nyon.skylper.config.screen.extensions.tickBox

fun YetAnotherConfigLib.Builder.appendMenuCategory() =
    category("menu") {
        val categoryKey = "menu"

        val highlightNonCompletedCollectionsKey = "highlight_non_completed_collections"
        primitive(categoryKey, highlightNonCompletedCollectionsKey) {
            description(categoryKey, highlightNonCompletedCollectionsKey)
            getSet(
                { config.menu.collections.highlightNonCompletedCollections },
                { config.menu.collections.highlightNonCompletedCollections = it }
            )
            tickBox()
        }

        val nonCompletedHighlightCollectionColorKey = "non_completed_collection_highlight_color"
        primitive(categoryKey, nonCompletedHighlightCollectionColorKey) {
            description(categoryKey, nonCompletedHighlightCollectionColorKey)
            getSet(
                { config.menu.collections.nonCompletedCollectionHighlightColor },
                { config.menu.collections.nonCompletedCollectionHighlightColor = it }
            )
            field(true)
        }

        val highlightNonCompletedBestiaryKey = "highlight_non_completed_bestiary"
        primitive(categoryKey, highlightNonCompletedBestiaryKey) {
            description(categoryKey, highlightNonCompletedBestiaryKey)
            getSet({ config.menu.bestiary.highlightNonCompletedBestiary }, { config.menu.bestiary.highlightNonCompletedBestiary = it })
            tickBox()
        }

        val nonCompletedHighlightBestiaryColorKey = "non_completed_bestiary_highlight_color"
        primitive(categoryKey, nonCompletedHighlightBestiaryColorKey) {
            description(categoryKey, nonCompletedHighlightBestiaryColorKey)
            getSet(
                { config.menu.bestiary.nonCompletedBestiaryHighlightColor },
                { config.menu.bestiary.nonCompletedBestiaryHighlightColor = it }
            )
            field(true)
        }
    }
