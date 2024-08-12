package ru.otus.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.writeTo
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
            val packageName = classDeclaration.packageName.asString()
            val className = classDeclaration.simpleName.asString()
            val properties = classDeclaration.getDeclaredProperties()
            logger.warn("Working with class: $packageName.$className")

            var funSpec = FunSpec.builder("printProperties").receiver(ClassName(packageName, className))
            funSpec = funSpec.addStatement(
                "println(\"My name is '%L'\")",
                className
            )
            properties.forEach { property ->
                logger.warn("==> Property found: ${property.simpleName.asString()} (${property.type})")
                funSpec = funSpec.addStatement(
                    "println(\" - I have property '%L'(%L), the value is \$%L\")",
                    property.simpleName.asString(),
                    property.type,
                    property.simpleName.asString()
                )
            }

            val fileSpec = FileSpec.builder(packageName, "print$className")
                .addFunction(funSpec.build())
                .build()

            fileSpec.writeTo(codeGenerator, false)
        }
    }
}

class PrintableClassProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PrintableClassProcessor(environment.codeGenerator, environment.logger)
    }
}