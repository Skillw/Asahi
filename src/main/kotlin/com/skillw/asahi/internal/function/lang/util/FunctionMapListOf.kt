package com.skillw.asahi.internal.function.lang.util

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.MapTemplate

/**
 * @className FunctionMapOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionMapListOf {

    val func = createFunction("mapListOf", namespaces = arrayOf("lang")) {
        except("with")
        val templateGetter = quest<MapTemplate>()
        val list = quest<List<Any>>(false)
        function {
            val template = templateGetter.get()
            list.get().mapNotNull { template.build(it as? List<Any>? ?: return@mapNotNull null) }
                .toMutableList()
        }
    }
}