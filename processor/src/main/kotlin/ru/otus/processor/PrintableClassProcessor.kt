package ru.otus.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toClassName
import ru.otus.annotation.PrintableClass

class PrintableClassProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val toSearchFor = requireNotNull(PrintableClass::class.qualifiedName)
        val symbols: Sequence<KSAnnotated> = resolver.getSymbolsWithAnnotation(toSearchFor)

        val validated = symbols
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .toList()

        val visitor = PrintableClassVisitor()
        validated.forEach { symbol ->
            symbol.accept(visitor, Unit)
        }

        return symbols.toList() - validated.toSet()
    }

    inner class PrintableClassVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            logger.warn("Visiting declaration: ${classDeclaration.toClassName()}")
        }
    }
}

class PrintableClassProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PrintableClassProcessor(environment.codeGenerator, environment.logger)
    }
}