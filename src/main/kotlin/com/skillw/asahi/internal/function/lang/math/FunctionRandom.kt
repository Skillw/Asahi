package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.NumberUtils.random


@AsahiFunction
object FunctionRandom {

    val func = createFunction("random", namespaces = arrayOf("lang")) {
        val x = quest<Double>()
        except("to")
        val y = quest<Double>()
        function { random(x.get(), y.get()) }
    }
}