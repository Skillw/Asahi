package com.skillw.asahi.core.parser.prefix

import com.skillw.asahi.api.annotation.AsahiMember
import com.skillw.asahi.api.member.ast.expr.Block
import com.skillw.asahi.api.member.ast.expr.DefFunction
import com.skillw.asahi.api.member.ast.expr.DefVar
import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.ast.expr.compound.Unary
import com.skillw.asahi.api.member.ast.expr.compound.unary
import com.skillw.asahi.api.member.ast.parse
import com.skillw.asahi.api.member.runtime.Var
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.basic.Function
import com.skillw.asahi.api.member.runtime.obj.basic.NumberObj
import com.skillw.asahi.api.member.runtime.obj.basic.StringObj
import com.skillw.asahi.api.member.tokenizer.ITokenStream.Companion.currentToken
import com.skillw.asahi.api.member.tokenizer.ITokenStream.Companion.nextToken
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.NoSource
import com.skillw.asahi.api.member.tokenizer.tokens.CallVar
import com.skillw.asahi.api.member.tokenizer.tokens.Literal
import com.skillw.asahi.api.member.tokenizer.tokens.Number
import com.skillw.asahi.api.member.util.prefix

/**
 * @className Lang
 *
 * @author Glom
 * @date 2023/8/13 13:39 Copyright 2023 user. All rights reserved.
 */

//@AsahiMember(type = TokenType.CallVar)
//private fun callVar() = prefix {
//    parser {
//        val token = currentToken<CallVar>()
//        unary(token, token.key.toSized())
//    }
//
//    evaluator<Unary> {
//        variables()[it.right.eval()]
//    }
//}

@AsahiMember
private fun number() = prefix {
    parser(type = TokenType.Number) {
        val token = currentToken<Number>()
        NumberObj(token.number, source())
    }
    evaluator(NumberObj::class.java) {
        it.eval()
    }
}

@AsahiMember
private fun left() = prefix {
    parser(type = TokenType.LBracket) {
        parse(0).also { next() }
    }
    parser(type = TokenType.LeftBrace) {
        parse(0).also { next() }
    }
}

@AsahiMember
private fun define() = prefix {
    parser(type = TokenType.Define) {
        val source = source()
        val key = nextToken<Literal>().string
        if (expectSafely(TokenType.LBracket)) {
            val params = ArrayList<String>()
            while (!expectSafely(TokenType.RBracket)) {
                params.add(nextToken<Literal>().string)
                expectSafely(TokenType.Comma)
            }
            val body = Block(parse(), source())
            DefFunction(key, params.toTypedArray(), body, source)
        } else {
            expect(TokenType.Assign)
            val value = parse()
            DefVar(key, value, source)
        }
    }

    evaluator(DefFunction::class.java) {
        val name = it.name
        val params = it.params
        val body = it.body
        val function = Function(name, params = params, body = body, this)
        addFunction(function)
        function
    }
    evaluator(DefVar::class.java) {
        val name = it.name
        val value = it.value.eval<AsahiObject>()
        val variable = Var(name, value)
        variables()[name] = variable
        variable
    }
}

@AsahiMember
private fun callVar() = prefix {
    parser(type = TokenType.CallVar) {
        val token = currentToken<CallVar>()
        unary(token, token.key.toSized())
    }
    evaluator<Unary>(type = TokenType.CallVar) {
        val name = (it.right as Sized).eval().toString()
        variables()[name] ?: StringObj("\$$name", NoSource)
    }
}


//    val params = ArrayList<Expression<out Any?>>()
//    while (!expectSafely(TokenType.RBracket)) {
//        params.add(parse())
//        expectSafely(TokenType.Comma)
//    }
//    InvokeFunc(left.eval(), params.then { it.map { state -> state.eval() }.toTypedArray() }, source())