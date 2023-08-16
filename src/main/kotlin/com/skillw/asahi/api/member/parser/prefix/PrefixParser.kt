package com.skillw.asahi.api.member.parser.prefix

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.parser.Parser

/**
 * @className InfixParser
 *
 * @author Glom
 * @date 2023/8/13 16:00 Copyright 2023 user. All rights reserved.
 */
interface PrefixParser : Parser {
    fun parse(context: IParseContext): Expression
}