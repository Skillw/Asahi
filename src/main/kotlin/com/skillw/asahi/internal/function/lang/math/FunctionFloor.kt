package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import kotlin.math.floor


@AsahiFunction
object FunctionFloor {

    val func = createFunction("floor", namespaces = arrayOf("lang")) {
        val number = quest<Double>()
        function { floor(number.get()) }
    }
}