package dev.nyon.skylper

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

class EventProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {
    private val skylperEventPath = "dev.nyon.skylper.extensions.event.SkylperEvent"

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(skylperEventPath).filterIsInstance<KSFunctionDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(
                false, *symbols.mapNotNull { it.containingFile }.toList().toTypedArray()
            ), packageName = "dev.nyon.skylper.events", fileName = "LoadedEvents"
        )

        file.writer().use { writer ->
            writer.write(
                """
                package dev.nyon.skylper.events
                
                import dev.nyon.skylper.extensions.event.EventInstance
                import dev.nyon.skylper.extensions.event.EventListener
                import dev.nyon.skylper.skyblock.models.Area
                
                object LoadedEvents {
                    val events = setOf(
                
            """.trimIndent()
            )

            val instances =
                symbols.groupBy { it.parameters.first().type.resolve().declaration.qualifiedName!!.asString() }
                    .map { (event, listeners) ->
                        val returnType = listeners.first().returnType?.resolve()
                        val returnTypeName = (returnType?.declaration?.qualifiedName?.asString()
                            ?: "Unit").run { if (returnType?.isMarkedNullable == true) "$this?" else this }

                        val eventListeners = listeners.map { listener ->
                            """
                            object : EventListener<$event, $returnTypeName> {
                                override val area: Area
                                    get() = Area.${listener.annotations.first { it.shortName.asString() == "SkylperEvent" }.arguments.first { it.name?.asString() == "area" }.value}
                                    
                                override fun invoke(event: $event): $returnTypeName {
                                    ${if (returnTypeName != "kotlin.Unit") "return " else ""}${listener.qualifiedName!!.asString()}(event)
                                }
                            }
                        """.trimIndent()
                        }

                        "EventInstance(\n$event::class, \nlistOf(${
                            eventListeners.joinToString(
                                prefix = "\n",
                                postfix = "\n",
                                separator = ", \n"
                            )
                        })\n)"
                    }

            writer.write(instances.joinToString(postfix = "\n", separator = ", \n"))

            writer.write(")\n}")
        }

        logger.warn("Processed ${symbols.toList().size} events!")

        return emptyList()
    }
}
