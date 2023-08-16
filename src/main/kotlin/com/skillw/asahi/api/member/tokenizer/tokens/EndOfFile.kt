package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

class EndOfFile(val holder: SourceHolder) : Token, SourceHolder by holder {

    override fun type() = TokenType.EOF

    override fun equals(other: Any?): Boolean {
        return other is EndOfFile
    }

    override fun hashCode(): Int {
        return type().hashCode()
    }

    override fun toString(): String {
        return "End Of File"
    }
}