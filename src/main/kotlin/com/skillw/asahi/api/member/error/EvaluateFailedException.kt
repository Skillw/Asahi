package com.skillw.asahi.api.member.error

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.util.toJson

/**
 * @className UnexpectedException
 *
 * @author Glom
 * @date 2023/8/13 22:16 Copyright 2023 user. All rights reserved.
 */
class EvaluateFailedException private constructor(message: String) : Exception(message) {
    companion object {
        @JvmStatic
        fun ScriptHolder.evalFailed(expr: Expression): Nothing {
            throw EvaluateFailedException(expr.genError("expression: \n ${expr.serialize().toJson()} \n"))
        }
    }
}