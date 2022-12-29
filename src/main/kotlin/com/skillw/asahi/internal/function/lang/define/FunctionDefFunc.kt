package com.skillw.asahi.internal.function.lang.define

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.api.member.function.ScriptFunction


@AsahiFunction
object FunctionDefFunc {
    /** func 函数名 ( 参数名1 , 参数名2 ) { //处理参数 } */
    val func = createFunction("func", namespaces = arrayOf("lang")) {
        val key = next()
        val params = quest<List<String>>(false)
        val content = splitTill("{", "}").compile()
        function {
            ScriptFunction(key, params.get().toTypedArray(), content, context).also { addFunction(it) }
        }
    }
}