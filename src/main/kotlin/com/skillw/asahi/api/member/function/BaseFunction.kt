package com.skillw.asahi.api.member.function

import com.skillw.asahi.api.manager.AsahiManager
import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.AsahiLoopContext
import com.skillw.asahi.api.member.context.AsahiLoopContext.Companion.loopContext
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.function.BaseFunction.IFunction
import com.skillw.asahi.api.member.parser.IParser
import com.skillw.asahi.api.member.reader.IAsahiReader
import com.skillw.asahi.internal.logger.Logger.error
import java.util.*


/**
 * @className IFunction
 *
 * @author Glom
 * @date 2022/12/25 13:39 Copyright 2022 user. All rights reserved.
 */
abstract class BaseFunction<R>(
    val key: String,
    vararg val alias: String,
    val namespaces: Array<String> = arrayOf("common"),
) : IParser<R>, FunctionCreator<R> {
    abstract override fun IAsahiReader.parse(): IFunction<R>
    fun interface IFunction<R> : IExecutor<R>
    fun interface ILazy : IFunction<Any?>

    override fun <R> IAsahiReader.function(exec: AsahiContext.() -> R): IFunction<R> {
        return IFunction {
            if (namespaces.any { this.namespaces.contains(it) }) {
                return@IFunction exec()
            }
            error("Function $key doesn't exist in this Context!")
        }
    }

    override fun IAsahiReader.runLoop(
        loop: AsahiLoopContext.(() -> ILoopContext.Result) -> Unit,
    ): IFunction<Unit> {
        val label = if (except("label")) next() else UUID.randomUUID().toString()
        except("then")
        val process = splitTill("{", "}").compile()
        return function {
            val loopContext = context.loopContext(label)

            //循环一次
            fun loopOnce(): ILoopContext.Result {
                loopContext.run {
                    process.onEach {
                        when {
                            isBreak -> true
                            isContinue -> true.also { isContinue = false }
                            context["@exit"] == true -> true
                            else -> false
                        }
                    }.run()
                    return when {
                        isBreak -> ILoopContext.Result.BREAK
                        isContinue -> ILoopContext.Result.CONTINUE.also { isContinue = false }
                        context["@exit"] == true -> ILoopContext.Result.BREAK
                        else -> ILoopContext.Result.CONTINUE
                    }
                }
            }
            loopContext.run {
                loop { loopOnce() }
            }
            loopContext.forEach { (key, value) ->
                context.run { if (containsKey(key)) put(key, value) }
            }
        }
    }

    companion object {
        @JvmStatic
        fun <R> createFunction(
            key: String,
            vararg alias: String,
            namespaces: Array<String> = arrayOf("common"),
            parse: FunctionInvoker<R>.() -> IFunction<R>,
        ): BaseFunction<R> {
            return object : BaseFunction<R>(key, *alias, namespaces = namespaces) {
                val creator = this
                override fun IAsahiReader.parse(): IFunction<R> {
                    return FunctionInvoker(creator, this).run(parse)
                }
            }
        }

        @JvmStatic
        fun <R> regFunction(
            key: String,
            vararg alias: String,
            namespaces: Array<String> = arrayOf("common"),
            parse: FunctionInvoker<R>.() -> IFunction<R>,
        ): BaseFunction<R> {
            return createFunction(key, *alias, namespaces = namespaces, parse = parse).apply { register() }
        }
    }

    fun register() {
        AsahiManager.registerFunction(this)
    }
}