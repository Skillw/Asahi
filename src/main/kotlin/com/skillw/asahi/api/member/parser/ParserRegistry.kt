package com.skillw.asahi.api.member.parser

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.parse
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
import com.skillw.asahi.api.member.tokenizer.ITokenStream
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenKey
import com.skillw.asahi.api.member.tokenizer.TokenKey.Companion.toKey
import com.skillw.asahi.core.parser.ParseContext

object ParserRegistry {
    internal val prefixes = HashMap<TokenKey, PrefixParser>()
    internal val infixes = HashMap<TokenKey, InfixParser>()

    fun Token.prefix(): PrefixParser? {
        val key = toKey()
        return prefixes[key]
    }

    fun Token.infix(): InfixParser? {
        val key = toKey()
        return infixes[key]
    }

    fun register(key: TokenKey, parser: Parser) {
        when (parser) {
            is PrefixParser -> {
                prefixes[key] = parser
            }

            is InfixParser -> {
                infixes[key] = parser
            }
        }
    }

    fun parse(stream: ITokenStream): Expression {
        val context = ParseContext(stream)
        return context.parse()
    }
}