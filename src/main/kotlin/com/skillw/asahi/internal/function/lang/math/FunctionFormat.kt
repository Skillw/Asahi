package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.NumberUtils.format


@AsahiFunction
object FunctionFormat {

    val func = createFunction("format", namespaces = arrayOf("lang")) {
        val number = quest<Double>()
        val format = quest<String>()
        function { number.get().format(format.get()) }
    }
}