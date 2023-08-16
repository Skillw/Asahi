package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className NumBinaryOperator
 *
 * @author Glom
 * @date 2023/8/13 9:39 Copyright 2023 user. All rights reserved.
 */
data class Operator(val type: Type, val holder: SourceHolder) : Token, SourceHolder by holder {
    enum class Type(val display: String) {
        Comma(","),
        PlusAssign("+="),
        MinusAssign("-="),
        MulAssign("*="),
        DivAssign("/="),
        Assign("="),
        Or("or"),
        And("and"),
        BitOr("|"),
        BitXor("^"),
        BitAnd("&"),
        EqTo("=="),
        NotEqTo("!="),
        BigThan(">"),
        LessThan("<"),
        BigOrEq(">="),
        LessOrEq("<="),
        SHL("<<"),
        SHR(">>"),
        Plus("+"),
        Minus("-"),
        Mul("*"),
        Div("/"),
        Mod("%"),
        Pow("^"),
        Not("!"),
        Opposite("-"),
        Positive("+"),
        Increase("++"),
        Decrease("--"),
        LBracket("("),
        RBracket(")"),
        LCBrace("["),
        RCBrace("]"),
        LBrace("{"),
        RBrace("}"),
        Colon(":"),
        Question("?"),
        FullStop("."),
    }

    override fun type(): TokenType =
        when (type) {
            Type.Assign -> TokenType.Assign
            Type.PlusAssign -> TokenType.PlusAssign
            Type.MinusAssign -> TokenType.MinusAssign
            Type.BigThan -> TokenType.BigThan
            Type.LessThan -> TokenType.LessThan
            Type.BigOrEq -> TokenType.BigOrEq
            Type.LessOrEq -> TokenType.LessOrEq
            Type.EqTo -> TokenType.EqTo
            Type.NotEqTo -> TokenType.NotEqTo
            Type.Plus -> TokenType.Plus
            Type.Minus -> TokenType.Minus
            Type.Mul -> TokenType.Mul
            Type.Div -> TokenType.Div
            Type.Mod -> TokenType.Mod
            Type.Pow -> TokenType.Pow
            Type.LBracket -> TokenType.LBracket
            Type.RBracket -> TokenType.RBracket
            Type.Not -> TokenType.Not
            Type.And -> TokenType.And
            Type.Or -> TokenType.Or
            Type.BitXor -> TokenType.BitXor
            Type.BitAnd -> TokenType.BitAnd
            Type.BitOr -> TokenType.BitOr
            Type.SHL -> TokenType.SHL
            Type.SHR -> TokenType.SHR
            Type.LCBrace -> TokenType.LeftCurlyBrace
            Type.RCBrace -> TokenType.RightCurlyBrace
            Type.LBrace -> TokenType.LeftBrace
            Type.RBrace -> TokenType.RightBrace
            Type.Colon -> TokenType.Colon
            Type.Question -> TokenType.Question
            Type.FullStop -> TokenType.FullStop
            Type.Comma -> TokenType.Comma
            Type.MulAssign -> TokenType.MulAssign
            Type.DivAssign -> TokenType.DivAssign
            Type.Increase -> TokenType.Increase
            Type.Decrease -> TokenType.Decrease
            Type.Positive -> TokenType.Positive
            Type.Opposite -> TokenType.Opposite


        }

    override fun toString(): String {
        return type.display
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Operator
        return type == other.type
    }

    override fun hashCode(): Int {
        return type().hashCode()
    }
}