package com.skillw.asahi.api.member.parser

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.expr.compound.binary
import com.skillw.asahi.api.member.ast.expr.compound.unary
import com.skillw.asahi.api.member.ast.parse
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.prefix.PrefixParser

object DefaultParser {
    object Unary : PrefixParser {
        override var priority: Int = 0

        override fun parse(context: IParseContext) = context.run {
            val token = current()
            val right = parse(priority)
            unary(token, right)
        }
    }

    object Binary : InfixParser {
        override var priority: Int = 0
        override fun parse(context: IParseContext, left: Expression) = context.run {
            val token = current()
            val right = parse(priority)
            binary(token, left, right)
        }
    }
}