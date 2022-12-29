package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionRange {

    val func = createFunction("range", namespaces = arrayOf("lang")) {
        val from = quest<Double>()
        except("to", "~", "..")
        val to = quest<Double>()
        function {
            val a = from.get()
            val b = to.get()
            a..b
        }
    }
}