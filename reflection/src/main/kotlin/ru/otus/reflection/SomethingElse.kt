package ru.otus.reflection

import ru.otus.annotation.ForConstructor
import ru.otus.annotation.ForFunction
import ru.otus.annotation.ForProperty

class SomethingElse @ForConstructor constructor(value: Int) {
    @ForProperty
    val value1: Int = value

    @get:ForProperty
    var value2: Int = 0

    @ForFunction
    fun someFun() {
        println("Very funny!")
    }
}