package com.skillw.asahi.internal.function.lang.`object`

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.Coerce


@AsahiFunction
object FunctionType {

    val func = createFunction("type", namespaces = arrayOf("lang")) {
        val type = quest<String>()
        val getter = quest<Any?>()
        function {
            val obj = getter.get()
            when (type.get()) {
                "double" -> Coerce.toDouble(obj)
                "int" -> Coerce.toInteger(obj)
                "long" -> Coerce.toLong(obj)
                "float" -> Coerce.toFloat(obj)
                "short" -> Coerce.toShort(obj)
                "byte" -> Coerce.toByte(obj)
                "bool" -> Coerce.toBoolean(obj)
                "char" -> Coerce.toChar(obj)
                "string" -> Coerce.toString(obj)
                else -> obj
            }
        }
    }
}