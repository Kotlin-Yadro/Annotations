package ru.otus.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import ru.otus.annotation.PrintableClass

class PrintableClassProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val toSearchFor = requireNotNull(PrintableClass::class.qualifiedName)
        val symbols: Sequence<KSAnnotated> = resolver.getSymbolsWithAnnotation(toSearchFor)
        symbols.forEach {
            logger.warn("Found symbols: $it")
        }
        return emptyList()
    }
}

class PrintableClassProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PrintableClassProcessor(environment.codeGenerator, environment.logger)
    }
}