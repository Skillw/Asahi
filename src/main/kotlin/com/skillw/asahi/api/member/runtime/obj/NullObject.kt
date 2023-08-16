package com.skillw.asahi.api.member.runtime.obj

import com.skillw.asahi.api.member.runtime.type.NullClass
import com.skillw.asahi.api.member.tokenizer.source.NoSource

object NullObject : AsahiObject(NullClass, NoSource) {
    override fun toJava() = null
}