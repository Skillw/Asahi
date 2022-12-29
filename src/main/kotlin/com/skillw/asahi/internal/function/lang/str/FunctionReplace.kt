package com.skillw.asahi.internal.function.lang.str

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionReplace {

    val func = createFunction("replace", namespaces = arrayOf("lang")) {
        val str = quest<String>()
        except("with")
        val replacement = quest<Map<String, Any>>()
        function {
            var cache = str.get()
            replacement.get().mapValues { it.value.toString() }.forEach { (origin, new) ->
                cache = cache.replace(origin, new)
            }
            cache
        }
    }
}



