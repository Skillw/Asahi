package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.Coerce

/**
 * @className FunctionAll
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionAll {

    val func = createFunction("all", namespaces = arrayOf("lang")) {
        val array = quest<Array<Any?>>()
        function {
            array.get().all { Coerce.toBoolean(it) }
        }

    }
}