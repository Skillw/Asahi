package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import kotlin.math.round


@AsahiFunction
object FunctionRound {

    val func = createFunction("round", namespaces = arrayOf("lang")) {
        val x = quest<Double>()
        function { round(x.get()).toInt() }
    }
}