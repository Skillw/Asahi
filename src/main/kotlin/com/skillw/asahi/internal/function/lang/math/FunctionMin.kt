package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.TypeUtils.cast
import kotlin.math.min


@AsahiFunction
object FunctionMin {

    val func = createFunction("min", namespaces = arrayOf("lang")) {
        val value = if (except("["))
            quest<List<Any?>>()
        else {
            val a = quest<Double>()
            except("to")
            val b = quest<Double>()
            function { a.get() to b.get() }
        }
        function {
            when (val values = value.get()) {
                is List<*> -> {
                    val numbers = values.map { it.cast<Double>() }
                    numbers.minOf { it }
                }

                is Pair<*, *> -> {
                    val a = values.first.cast<Double>()
                    val b = values.second.cast<Double>()
                    min(a, b)
                }

                else -> 0.0
            }
        }
    }
}