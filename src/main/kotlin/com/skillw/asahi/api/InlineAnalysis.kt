package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.NamespaceHolder
import com.skillw.asahi.internal.lexer.InlineAnalysisImpl

/**
 * @className InlineReaderImpl
 *
 * @author Glom
 * @date 2023/1/14 10:04 Copyright 2023 user. All rights reserved.
 */
interface InlineAnalysis : NamespaceHolder<InlineAnalysis> {
    fun analysis(context: AsahiContext = AsahiContext.create()): String

    companion object {
        @JvmStatic
        fun of(string: String): InlineAnalysis {
            return InlineAnalysisImpl.of(string)
        }
    }
}