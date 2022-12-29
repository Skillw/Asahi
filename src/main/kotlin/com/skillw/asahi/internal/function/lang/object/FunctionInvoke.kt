package com.skillw.asahi.internal.function.lang.`object`

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.api.member.function.ScriptFunction
import com.skillw.asahi.internal.logger.Logger.error


@AsahiFunction
object FunctionInvoke {

    val func = createFunction("invoke", namespaces = arrayOf("lang")) {
        val func = if (except("func")) quest<String>() else quest<ScriptFunction>()
        except("with")
        val paramsGetter = quest<Array<Any>>()
        function {
            val params = paramsGetter.get()
            when (val function = func.get()) {
                is String -> context.invoke(function, *params)
                is ScriptFunction -> function.invoke(*params)
                else -> {
                    error("")
                }
            }
        }
    }
}