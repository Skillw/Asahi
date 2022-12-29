package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


/**
 * @className FunctionReturn
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionReturn {

    val func = createFunction("return", namespaces = arrayOf("lang")) {
        val value = quest<Any>()
        function { context["@exit"] = true; value.get() }
    }
}