package com.skillw.asahi.api.member.util

import com.skillw.asahi.api.member.parser.IParseContext

/**
 * @className ParserEnv
 *
 * @author Glom
 * @date 2023/8/14 18:16 Copyright 2023 user. All rights reserved.
 */
class ParserEnv(var priority: Int = -1, private val parseContext: IParseContext) :
    IParseContext by parseContext