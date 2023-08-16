package com.skillw.asahi.api.member.runtime.obj.basic

import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.type.StringClass
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className NumberObj
 *
 * @author Glom
 * @date 2023/8/14 21:55 Copyright 2023 user. All rights reserved.
 */
class StringObj(val string: String, source: SourceHolder) : AsahiObject(StringClass, source), Sized {
    override fun eval(): AsahiObject = this
    override fun serialize() = linkedMapOf(
        "type" to type.name(),
        "value" to string,
    )

    override fun toJava() = string
}