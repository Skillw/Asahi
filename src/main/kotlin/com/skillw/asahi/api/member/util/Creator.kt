package com.skillw.asahi.api.member.util

import com.skillw.asahi.api.member.util.infix.Infix
import com.skillw.asahi.api.member.util.infix.InfixCreator
import com.skillw.asahi.api.member.util.prefix.Prefix
import com.skillw.asahi.api.member.util.prefix.PrefixCreator

/**
 * @className Creator
 *
 * @author Glom
 * @date 2023/8/14 19:18 Copyright 2023 user. All rights reserved.
 */

fun prefix(builder: Prefix.() -> Unit): PrefixCreator = PrefixCreator(builder)
fun infix(builder: Infix.() -> Unit): InfixCreator = InfixCreator(builder)