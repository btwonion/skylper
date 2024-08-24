package dev.nyon.skylper.extensions.event

import dev.nyon.skylper.skyblock.models.Area

/**
 * This annotation is only applicable to functions and marks it as an event listener.
 * The annotation is needed for the symbol processor to recognize this event.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class SkylperEvent(val area: Area = Area.EVERYWHERE)
