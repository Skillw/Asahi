package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction

object ParserVar {
    const val varPrefix = '&'
    fun isVar(key: String): Boolean {
        return key.startsWith(varPrefix)
    }

    fun parseVar(key: String): IExecutor<Any> {
        val varKey = key.substring(1)
        return executor {
            get(varKey).let {
                when (it) {
                    is BaseFunction.ILazy -> {
                        it.run()
                    }

                    else -> it
                }
            } ?: "null"
        }
    }
}