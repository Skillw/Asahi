package com.skillw.asahi.api.member.tokenizer

/**
 * @className Tokenizer
 *
 * @author Glom
 * @date 2023/8/13 8:46 Copyright 2023 user. All rights reserved.
 */
interface Tokenizer {
    fun tokenize(script: String): ITokenStream
}