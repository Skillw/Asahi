package com.skillw.asahi.internal.function.lang.define

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionHas {
    val func = createFunction("has", namespaces = arrayOf("lang")) {
        val key = quest<String>()
        function { containsKey(key.get()) }
    }
}