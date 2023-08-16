package com.skillw.asahi.api.member.util.infix

/**
 * @className InfixCreator
 *
 * @author Glom
 * @date 2023/8/14 19:19 Copyright 2023 user. All rights reserved.
 */
class InfixCreator(
    private val builder: Infix.() -> Unit,
) {
    fun create(): Infix {
        return Infix().apply(builder)
    }
}