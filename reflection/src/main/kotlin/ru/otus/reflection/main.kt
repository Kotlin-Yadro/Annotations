package ru.otus.reflection

fun main() {
    val somethingElse = SomethingElse(100)
    println(somethingElse)
    reflect()
}

fun reflect() {
    val something = SomethingElse(100)
    val methods = SomethingElse::class.java.methods
    val someFun = methods.find { it.name == "someFun" }
    someFun?.invoke(something)
}