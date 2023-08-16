package com.skillw.asahi.api.member.ast

import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.NullObject
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder


class None(source: SourceHolder) : Expression, Sized, SourceHolder by source {
    override fun serialize() = mapOf("value" to "none")
    override fun eval(): AsahiObject = NullObject
}