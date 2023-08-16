package com.skillw.asahi.api.member.tokenizer.source

/**
 * @className Source
 *
 * @author Glom
 * @date 2023/8/15 15:40 Copyright 2023 user. All rights reserved.
 */
open class Source(override val script: ScriptHolder, override val source: Pair<Int, Int>) : SourceHolder {
    override fun genError(message: String): String {
        return script.genError(source, message)
    }
}