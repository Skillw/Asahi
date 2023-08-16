package com.skillw.asahi.api.member.runtime.obj.basic

import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.ast.serialize
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.type.ArrayClass
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className NumberObj
 *
 * @author Glom
 * @date 2023/8/14 21:55 Copyright 2023 user. All rights reserved.
 */
class ArrayObj(val array: Array<AsahiObject>, source: SourceHolder) : AsahiObject(ArrayClass, source), Sized {
    override fun eval() = this
    override fun serialize() = linkedMapOf(
        "type" to type.name(),
        "values" to toJava().map { it.serialize() },
    )

    override fun toJava() = array.map { it.toJava() }.toTypedArray()
}