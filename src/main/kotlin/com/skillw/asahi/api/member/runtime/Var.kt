package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.ast.serialize
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.type.VarClass
import com.skillw.asahi.api.member.tokenizer.source.NoSource
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Var
 *
 * @author Glom
 * @date 2023/8/13 17:22 Copyright 2023 user. All rights reserved.
 */
class Var(val key: String, var value: AsahiObject, source: SourceHolder = NoSource) :
    AsahiObject(VarClass, source) {
    override fun toJava(): Any? {
        return value.toJava()
    }

    override fun clone(): Var {
        return Var(key, value.clone())
    }

    override fun serialize() = linkedMapOf(
        "type" to type.serialize(),
        "key" to key,
        "value" to value.serialize()
    )
}