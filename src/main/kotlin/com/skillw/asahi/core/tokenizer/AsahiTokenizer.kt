package com.skillw.asahi.core.tokenizer

import com.skillw.asahi.api.member.tokenizer.ITokenStream
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.Tokenizer
import com.skillw.asahi.api.member.tokenizer.source.Script
import com.skillw.asahi.api.member.tokenizer.source.Source
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder
import com.skillw.asahi.api.member.tokenizer.tokens.*
import com.skillw.asahi.api.member.tokenizer.tokens.Number
import java.util.regex.Pattern

/**
 * @className AsahiTokenizer
 *
 * @author Glom
 * @date 2023/8/13 9:07 Copyright 2023 user. All rights reserved.
 */
object AsahiTokenizer : Tokenizer {
    override fun tokenize(script: String): ITokenStream {
        val lines = script.split("\n")
        val scripts = Script(lines)
        val context = TokenizeContext(scripts)
        for ((lineNum, line) in lines.withIndex()) {
            if (line.firstOrNull() == '#') continue
            context.addLine(lineNum, line)
            if (lineNum == lines.lastIndex) {
                context.add(EndOfFile(Source(scripts, lineNum to lines.lastIndex)))
            }
        }
        return TokenStream(context.tokens, scripts)
    }

    private fun TokenizeContext.addLine(lineNum: Int, line: String) {
        var strings = false
        var escape = false
        for ((index, char) in line.withIndex()) {
            if (strings) {
                if (char == '"' || char == '\'') {
                    if (escape) escape = false
                    else {
                        strings = false
                        continue
                    }
                }
                add(toLiteral(lineNum, index))
            }
            when (char) {
                '"', '\'' -> strings = true
                '\\' -> escape = true
                ' ' -> if (builder.isNotEmpty()) add(toToken(lineNum, index))
                else -> operatorOf(char, lineNum to index)
                    ?.also {
                        if (builder.isNotEmpty()) add(toToken(lineNum, index))
                        add(it)
                    }
                    ?: builder.append(char)
            }
        }
        if (builder.isNotEmpty())
            add(toToken(lineNum, line.lastIndex))
    }

    private fun TokenizeContext.toLiteral(line: Int, index: Int): Literal =
        Literal(builder.toString(), line to index).also { builder.clear() }

    private val pattern = Pattern.compile("^([+\\-])?(\\d+(\\.\\d+)?)$")

    private fun TokenizeContext.toToken(line: Int, index: Int): Token {
        val tokenStr = builder.toString().also { builder.clear() }
        if (tokenStr.firstOrNull() == '$' || tokenStr.firstOrNull() == '&') {
            val key = tokenStr.substring(0)
            return CallVar(key, line to index)
        }
        if (pattern.matcher(tokenStr).find()) {
            return Number(tokenStr.toDouble(), line to index)
        }
        return when (tokenStr) {
            "set", "def" -> Define(line to index)
            "and" -> Operator(Operator.Type.And, line to index)
            "or" -> Operator(Operator.Type.Or, line to index)
            "pow" -> Operator(Operator.Type.Pow, line to index)
            else -> Literal(tokenStr, line to index)
        }
    }

    private fun TokenizeContext.operatorOf(char: Char, source: SourceHolder): Operator? {
        val type = typeOf(char) ?: return null
        return Operator(type, source)
    }

    private fun TokenizeContext.typeOf(char: Char): Operator.Type? {
        return tokens.run {
            when (char) {
                '+' -> when (val last = last()) {
                    is Number -> Operator.Type.Plus
                    is Operator -> {
                        when (last.type) {
                            Operator.Type.Plus -> {
                                removeLast()
                                Operator.Type.Increase
                            }

                            else -> Operator.Type.Positive
                        }
                    }

                    else -> Operator.Type.Positive
                }

                '-' -> when (val last = last()) {
                    is Number -> Operator.Type.Minus
                    is Operator -> {
                        when (last.type) {
                            Operator.Type.Minus -> {
                                removeLast()
                                Operator.Type.Decrease
                            }

                            else -> Operator.Type.Opposite
                        }
                    }

                    else -> Operator.Type.Opposite
                }

                '*' -> Operator.Type.Mul
                '/' -> Operator.Type.Div
                '%' -> Operator.Type.Mod
                '(' -> Operator.Type.LBracket
                ')' -> Operator.Type.RBracket
                '!' -> Operator.Type.Not
                '{' -> Operator.Type.LBrace
                '}' -> Operator.Type.RBrace
                '[' -> Operator.Type.LCBrace
                ']' -> Operator.Type.RCBrace
                ':' -> Operator.Type.Colon
                '?' -> Operator.Type.Question
                '.' -> Operator.Type.FullStop
                ',' -> Operator.Type.Comma
                '^' -> Operator.Type.Pow
                '&' -> {
                    val last = last()
                    if (last is Operator && last.type == Operator.Type.BitAnd) {
                        removeLast()
                        Operator.Type.And
                    } else Operator.Type.BitAnd
                }

                '|' -> {
                    val last = last()
                    if (last is Operator && last.type == Operator.Type.BitOr) {
                        removeLast()
                        Operator.Type.Or
                    } else Operator.Type.BitOr
                }

                '>' -> when (val last = last()) {
                    is Operator -> {
                        when (last.type) {
                            Operator.Type.BigThan -> {
                                removeLast()
                                Operator.Type.SHR
                            }

                            else -> Operator.Type.BigThan
                        }
                    }

                    else -> Operator.Type.BigThan
                }

                '<' -> when (val last = last()) {
                    is Operator -> {
                        when (last.type) {
                            Operator.Type.LessThan -> {
                                removeLast()
                                Operator.Type.SHL
                            }

                            else -> Operator.Type.LessThan
                        }
                    }

                    else -> Operator.Type.LessThan
                }

                '=' -> when (val last = last()) {
                    is Operator -> {
                        when (last.type) {
                            Operator.Type.Assign -> {
                                removeLast()
                                Operator.Type.EqTo
                            }

                            Operator.Type.Not -> {
                                removeLast()
                                Operator.Type.NotEqTo
                            }

                            Operator.Type.BigThan -> {
                                removeLast()
                                Operator.Type.BigOrEq
                            }

                            Operator.Type.LessThan -> {
                                removeLast()
                                Operator.Type.LessOrEq
                            }

                            Operator.Type.Plus -> {
                                removeLast()
                                Operator.Type.PlusAssign
                            }

                            Operator.Type.Minus -> {
                                removeLast()
                                Operator.Type.MinusAssign
                            }

                            Operator.Type.Mul -> {
                                removeLast()
                                Operator.Type.MulAssign
                            }

                            Operator.Type.Div -> {
                                removeLast()
                                Operator.Type.DivAssign
                            }

                            else -> Operator.Type.Assign
                        }
                    }

                    else -> Operator.Type.Assign
                }

                else -> null
            }
        }
    }
}