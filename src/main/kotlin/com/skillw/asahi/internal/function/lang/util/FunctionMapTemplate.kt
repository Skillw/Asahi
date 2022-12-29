package com.skillw.asahi.internal.function.lang.util

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.MapTemplate

/**
 * @className FunctionMapTemplate
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionMapTemplate {

    val func = createFunction("mapTemplate", namespaces = arrayOf("lang")) {
        val list = quest<List<Any>>(false)
        function { MapTemplate(list.get().map { it.toString() }) }
    }
}