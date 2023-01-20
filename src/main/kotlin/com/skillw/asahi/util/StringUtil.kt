package com.skillw.asahi.util

import taboolib.common.util.asList


internal fun String.toList(): List<String> {
    return if (this.contains("\n")) {
        this.split("\n").asList()
    } else listOf(this)
}

internal fun String.toArgs(): Array<String> =

    if (this.contains(","))
        split(",").toTypedArray()
    else
        arrayOf(this)


internal fun String.format(): String {
    return this.replace(Regex("\\s+"), " ")
}