package com.skillw.asahi.api.member.ast.expr.compound

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.None
import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.tokenizer.Token

fun IParseContext.unary(
    operator: Token,
    right: Expression = None(source()),
) =
    object : Unary(source(), operator, right) {
    }

fun IParseContext.binary(
    operator: Token,
    left: Expression = None(source()),
    right: Expression = None(source()),
) =
    object : Binary(source(), left, operator, right) {
    }


fun IParseContext.ternary(
    operator: Token,
    left: Expression,
    middle: Expression,
    right: Expression,
) = object : Ternary(source(), operator, left, middle, right) {}
