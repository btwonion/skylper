package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.*
import dev.nyon.skylper.config.config
import java.awt.Color

fun RootDsl.appendMenuCategory() {
    val menu by categories.registering {
        val highlightNonCompletedCollections by rootOptions.registering {
            binding(true, { config.menu.collections.highlightNonCompletedCollections }, { config.menu.collections.highlightNonCompletedCollections = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val nonCompletedHighlightCollectionColor by rootOptions.registering {
            binding(Color(255, 0, 0, 255), { config.menu.collections.nonCompletedCollectionHighlightColor }, { config.menu.collections.nonCompletedCollectionHighlightColor = it })
            controller = colorPicker()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val highlightNonCompletedBestiary by rootOptions.registering {
            binding(true, { config.menu.bestiary.highlightNonCompletedBestiary }, { config.menu.bestiary.highlightNonCompletedBestiary = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val nonCompletedHighlightBestiaryColor by rootOptions.registering {
            binding(Color(255, 0, 0, 255), { config.menu.bestiary.nonCompletedBestiaryHighlightColor }, { config.menu.bestiary.nonCompletedBestiaryHighlightColor = it })
            controller = colorPicker()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
    }
}