package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.internal.script.NativeFunctionImpl

/**
 * @className ActionFunction
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixFunction : BaseInfix<NativeFunctionImpl>(NativeFunctionImpl::class.java) {
    init {
        infix("[", "(") { obj ->
            val params = parse<Array<Any?>>()
            obj.invoke(context(), *params)
        }
    }
}