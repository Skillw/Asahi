package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionRandomObj {

    val func = createFunction("randomObj", namespaces = arrayOf("lang")) {
        val list = quest<List<Any>>()
        function { (list.get().random() as IExecutor<Any>).get() }
    }
}