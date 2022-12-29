package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.TypeUtils


@AsahiFunction
object FunctionCheck {


    val func = createFunction("check", namespaces = arrayOf("lang")) {
        val a = quest<Any>()
        val symbol = next()
        val b = quest<Any>()
        function {
            TypeUtils.check(a.get(), symbol, b.get())
        }
    }
}