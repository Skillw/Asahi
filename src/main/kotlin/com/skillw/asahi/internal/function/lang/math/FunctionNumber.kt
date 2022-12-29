package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionNumber {

    val func = createFunction("number", namespaces = arrayOf("lang")) {
        val number = quest<Double>()
        function { number.get() }
    }
}