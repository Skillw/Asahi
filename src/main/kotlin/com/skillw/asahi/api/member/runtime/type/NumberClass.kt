package com.skillw.asahi.api.member.runtime.type

import com.skillw.asahi.api.member.runtime.obj.basic.NumberObj
import com.skillw.asahi.api.member.tokenizer.source.NoSource

/**
 * @className ObjectClass
 *
 * @author Glom
 * @date 2023/8/14 23:07 Copyright 2023 user. All rights reserved.
 */
object NumberClass : AsahiClass() {
    override fun name(): String = "NumberClass"
    override fun toJava(): Class<*> = Double::class.java

    init {
        function("+", params = arrayOf("other")) {
            println(serialize())
            val thiz = this as NumberObj
            val other = variables()["other"]?.value as NumberObj
            NumberObj(thiz.value + other.value, NoSource)
        }
    }
}