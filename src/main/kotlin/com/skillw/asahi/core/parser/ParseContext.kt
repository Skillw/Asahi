package com.skillw.asahi.core.parser

import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.tokenizer.ITokenStream


/**
 * @className ParseContext
 *
 * @author Glom
 * @date 2023/8/13 18:22 Copyright 2023 user. All rights reserved.
 */
class ParseContext(private val tokens: ITokenStream) : ITokenStream by tokens,
    IParseContext {

    override fun tokens() = tokens

}