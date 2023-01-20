package com.skillw.asahi.internal.namespacing.prefix.lang.linking

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.internal.util.AsahiClassBean

internal object PrefixBean {
    @AsahiPrefix(["bean"], "lang")
    private fun bean() = prefixParser {
        when (val type = next()) {
            "info" -> {
                val any = questAny()
                result {
                    println(AsahiClassBean.of(any.get()::class.java).info.joinToString("\n"))
                }
            }

            "load" -> {
                val any = questAny()
                result {
                    AsahiClassBean.of(any.get()::class.java)
                }
            }

            else -> {
                error("Unknown function bean type $type")
            }
        }
    }

}