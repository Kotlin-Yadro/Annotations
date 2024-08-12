package ru.otus.reflection

import ru.otus.annotation.ForFunction

fun main() {
    val somethingElse = SomethingElse(100)
    println(somethingElse)
    reflect()
}

fun reflect() {
    val something = SomethingElse(100)
    val methods = SomethingElse::class.java.methods
    methods.forEach {
        if (it.isAnnotationPresent(ForFunction::class.java)) {
            it.invoke(something)
        }
    }
}