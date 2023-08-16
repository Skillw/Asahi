package com.skillw.asahi.api.member.error

import com.skillw.asahi.api.member.tokenizer.ITokenStream

/**
 * @className UnexpectedException
 *
 * @author Glom
 * @date 2023/8/13 22:16 Copyright 2023 user. All rights reserved.
 */
class ParseFailedException private constructor(message: String) : Exception(message) {
    companion object {
        @JvmStatic
        fun ITokenStream.parseFailed(): Nothing {
            throw ParseFailedException(source().genError())
        }
    }
}