package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor

object ParserString {
    private val symbols = arrayOf('\'', '"')
    fun isStr(str: String): Boolean {
        return str.first() in symbols && str.last() in symbols
    }

    fun parseStr(str: String): IExecutor<Any> {
        return executor {
            str.run { substring(1, lastIndex) }
        }
    }
}