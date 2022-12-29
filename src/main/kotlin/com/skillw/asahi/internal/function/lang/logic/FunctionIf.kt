package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionIf {

    val func = createFunction("if", namespaces = arrayOf("lang")) {
        val condition = quest<Boolean>()
        except("then")
        val ifTrue = quest<Any>()
        except("else")
        val ifFalse = quest<Any>()
        function { (if (condition.get()) ifTrue else ifFalse).get() }
    }
}