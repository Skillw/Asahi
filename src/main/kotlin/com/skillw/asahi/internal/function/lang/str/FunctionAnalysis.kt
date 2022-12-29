package com.skillw.asahi.internal.function.lang.str

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.api.member.reader.InlineParser


@AsahiFunction
object FunctionAnalysis {

    val func = createFunction("analysis", namespaces = arrayOf("lang")) {
        val text = quest<String>()
        function { InlineParser(text.get()).replace(this) }
    }
}



