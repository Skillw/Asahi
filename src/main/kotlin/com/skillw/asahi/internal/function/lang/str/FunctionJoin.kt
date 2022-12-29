package com.skillw.asahi.internal.function.lang.str

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionJoin {

    val func = createFunction("join", namespaces = arrayOf("lang")) {
        val list = quest<List<Any>>()
        val by = if (!except("by")) executor { "" } else quest()
        function {
            list.get().joinToString(by.get().replace("\\n", "\n"))
        }
    }
}



