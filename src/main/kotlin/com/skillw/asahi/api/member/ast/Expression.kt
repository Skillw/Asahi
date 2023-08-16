package com.skillw.asahi.api.member.ast

import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Expression
 *
 * @author Glom
 * @date 2023/8/13 11:32 Copyright 2023 user. All rights reserved.
 */
interface Expression : Serializable, SourceHolder {
    fun sized(): Boolean = this is Sized
}
