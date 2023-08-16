package com.skillw.asahi.api.member.ast

import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.obj.AsahiObject

/**
 * @className Invokable
 *
 * @author Glom
 * @date 2023/8/15 15:22 Copyright 2023 user. All rights reserved.
 */
interface Invokable : Expression {
    fun AsahiContext.invoke(): AsahiObject
}