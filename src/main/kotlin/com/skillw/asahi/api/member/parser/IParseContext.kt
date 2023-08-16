package com.skillw.asahi.api.member.parser

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.ast.sized
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
import com.skillw.asahi.api.member.tokenizer.ITokenStream

interface IParseContext : ITokenStream {
    fun tokens(): ITokenStream
    fun PrefixParser.parse(): Expression = parse(this@IParseContext)
    fun InfixParser.parse(left: Expression): Expression = parse(this@IParseContext, left)

    fun Any?.toSized(): Sized = sized { this@toSized }
}