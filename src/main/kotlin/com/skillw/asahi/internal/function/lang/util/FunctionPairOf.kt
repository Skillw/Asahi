package com.skillw.asahi.internal.function.lang.util

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


/**
 * @className FunctionListOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionPairOf {

    val func = createFunction("pairOf", namespaces = arrayOf("lang")) {
        val pair = quest<Pair<Any, Any>>(false)
        function { pair.get() }
    }
}