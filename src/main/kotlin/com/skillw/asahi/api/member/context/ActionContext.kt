package com.skillw.asahi.api.member.context

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.reader.IAsahiReader

/**
 * @className AsahiContext
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
class ActionContext(
    context: AsahiContext, reader: IAsahiReader, val action: String,
) : AsahiContext(context), IAsahiReader by reader {
    inline fun <reified R> parse(): R {
        return quest<R>().get()
    }

    fun skip() {
        quest<Any>()
    }
}