package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.runtime.obj.AsahiObject

/**
 * @className Evaluator
 *
 * @author Glom
 * @date 2023/8/14 17:31 Copyright 2023 user. All rights reserved.
 */
interface Evaluator<E : Expression> {
    fun E.evaluate(context: AsahiContext): AsahiObject
    fun eval(expression: E, context: AsahiContext): AsahiObject = expression.evaluate(context)
}