package com.skillw.asahi.api.member.util.prefix

/**
 * @className PrefixCreator
 *
 * @author Glom
 * @date 2023/8/14 19:19 Copyright 2023 user. All rights reserved.
 */
class PrefixCreator(
    private val builder: Prefix.() -> Unit,
) {

    fun create(): Prefix {
        return Prefix().apply(builder)
    }
}