package com.skillw.asahi.api.member.ast.expr

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.runtime.obj.AsahiObject

/**
 * @className Sized
 *
 * @author Glom
 * @date 2023/8/14 17:34 Copyright 2023 user. All rights reserved.
 */
interface Sized : Expression {
    fun eval(): AsahiObject
}