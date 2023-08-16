@file:Suppress("UNCHECKED_CAST")

package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.TokenBased
import com.skillw.asahi.api.member.error.EvaluateFailedException.Companion.evalFailed
import com.skillw.asahi.api.member.runtime.ExpressionKey.Companion.toKey
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.tokenizer.TokenKey

object EvaluatorRegistry {
    private val evaluators = HashMap<ExpressionKey, Evaluator<out Expression>>()
    fun register(key: TokenKey, evaluator: Evaluator<out TokenBased>) {
        evaluators[key.toKey()] = evaluator
    }

    fun register(clazz: Class<out Expression>, evaluator: Evaluator<in Expression>) {
        evaluators[clazz.toKey()] = evaluator
    }

    fun register(key: ExpressionKey, evaluator: Evaluator<out Expression>) {
        evaluators[key] = evaluator
    }

    private fun <E : Expression> E.evaluator(): Evaluator<in E>? {
//        println("== $key ${key.hashCode()}")
//        evaluators.forEach {
//            println("   ${it.key.toString()} ${it.key.hashCode()} -== ${it.key == key}")
//        }
//        println("== ---")
        return evaluators[toKey()] as? Evaluator<in E>
    }

    fun eval(expression: Expression, context: AsahiContext = AsahiContext.create()): AsahiObject {
        val evaluator = expression.evaluator() ?: context.evalFailed(expression)
        return evaluator.eval(expression, context)
    }
}