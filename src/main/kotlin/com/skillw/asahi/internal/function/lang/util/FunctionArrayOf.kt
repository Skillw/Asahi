package com.skillw.asahi.internal.function.lang.util

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


/**
 * @className FunctionArrayOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionArrayOf {

    val func = createFunction("arrayOf", namespaces = arrayOf("lang")) {
        val array = quest<Array<Any>>(false)
        function { array.get() }
    }
}