package com.skillw.asahi.api.member.tokenizer

/**
 * @className TokenType
 *
 * @author Glom
 * @date 2023/8/13 13:43 Copyright 2023 user. All rights reserved.
 */
enum class TokenType(val priority: Int, val infix: Boolean = true) {
    CallVar(-1, false),
    Define(-1, false),
    Endl(-1, false),
    Literal(-1, false),
    Number(-1, false),
    Comma(1, false),
    EOF(-1, false),
    LBracket(-1, false),
    RBracket(15, false),
    PlusAssign(2),
    MinusAssign(2),
    MulAssign(2),
    DivAssign(2),
    Assign(2),
    Or(3),
    And(4),
    BitOr(5),
    BitXor(6),
    BitAnd(7),
    EqTo(8),
    NotEqTo(8),
    BigThan(9),
    LessThan(9),
    BigOrEq(9),
    LessOrEq(9),
    SHL(10),
    SHR(10),
    Plus(11),
    Minus(11),
    Mul(12),
    Div(12),
    Mod(12),
    Pow(13),
    Not(14),
    Opposite(14),
    Positive(14),
    Increase(14),
    Decrease(14),
    LeftBrace(15),
    RightBrace(15),
    LeftCurlyBrace(15),
    RightCurlyBrace(15),
    Colon(15),
    Question(15),
    FullStop(15),
}