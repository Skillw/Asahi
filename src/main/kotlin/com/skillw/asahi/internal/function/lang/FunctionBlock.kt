package com.skillw.asahi.internal.function.lang

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.internal.logger.Logger.error


@AsahiFunction
object FunctionBlock {

    val func = createFunction("lang", namespaces = arrayOf("lang")) {
        function { splitTill("{", "}").compile().run() ?: error("Parse Block error") }
    }
}