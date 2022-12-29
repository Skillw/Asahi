package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.NumberUtils.randomInt


@AsahiFunction
object FunctionRandomInt {

    val func = createFunction("randomInt", namespaces = arrayOf("lang")) {
        val x = quest<Int>()
        except("to")
        val y = quest<Int>()
        function { randomInt(x.get(), y.get()) }
    }
}