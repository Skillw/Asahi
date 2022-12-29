package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


/**
 * @className FunctionNot
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionNot {

    val func = createFunction("not", namespaces = arrayOf("lang")) {
        val bool = quest<Boolean>()
        function { !bool.get() }
    }
}