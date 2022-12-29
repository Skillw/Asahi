package com.skillw.asahi.api.manager

import com.skillw.asahi.api.member.action.ActionExecutor
import com.skillw.asahi.api.member.action.BaseAction
import com.skillw.asahi.api.member.executor.ExecutorCompound
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction
import com.skillw.asahi.api.member.parser.BaseParser
import com.skillw.asahi.api.member.parser.IParser
import com.skillw.asahi.api.member.reader.AsahiReader
import com.skillw.asahi.api.member.reader.IAsahiReader
import com.skillw.asahi.internal.logger.Logger.error
import com.skillw.asahi.internal.parser.ParserAction.parse
import com.skillw.asahi.internal.parser.ParserString.isStr
import com.skillw.asahi.internal.parser.ParserString.parseStr
import com.skillw.asahi.internal.parser.ParserVar.isVar
import com.skillw.asahi.internal.parser.ParserVar.parseVar
import com.skillw.asahi.util.TypeUtils.cast

object AsahiManager {
    /** Parsers 解释器容器， 类型 -> 解释器 */
    internal val parsers = HashMap<Class<*>, IParser<*>>()

    /** Functions 函数容器， id -> 函数 */
    internal val functions = HashMap<String, BaseFunction<*>>()

    /** Actions 动作容器， 类型 -> 动作 */
    internal val actions = HashMap<Class<*>, BaseAction<*>>()

    internal val allActions = HashSet<String>()

    internal val cache = HashMap<Int, ExecutorCompound>()

    fun String.compile(): ExecutorCompound {
        return cache.getOrPut(hashCode()) {
            ExecutorCompound().also { execs ->
                AsahiReader.of(this).run {
                    while (hasNext()) {
                        execs.add(quest<Any?>())
                    }
                }
            }
        }
    }

    fun hasFunction(key: String): Boolean {
        return functions.containsKey(key)
    }

    fun getFunction(key: String): BaseFunction<*>? {
        return functions[key]
    }

    fun registerAction(action: BaseAction<*>) {
        actions[action.type] = action
        action.actions.keys.forEach { key ->
            allActions.add(key)
            println("Action ${action.type.simpleName} $key  has been registered!")
        }
    }

    fun registerFunction(func: BaseFunction<*>) {
        val keys = listOf(func.key, *func.alias)
        keys.forEach { key ->
            functions[key] = func
            println("Function $key has been registered!")
        }
    }

    fun registerParser(parser: BaseParser<*>) {
        val types = parser.types
        types.forEach { type ->
            if (parsers.containsKey(type)) return@forEach
            parsers[type] = parser
            println("Parser ${type.simpleName} has been registered!")
        }
    }

    fun hasParser(type: Class<*>): Boolean {
        return parsers.containsKey(type)
    }

    fun <R> getParser(type: Class<R>): IParser<R>? {
        return parsers[type] as? IParser<R>?
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    inline fun <reified R> IAsahiReader.quest(action: Boolean = true): IExecutor<R> {
        val token = next()
        return when {
            isVar(token) -> parseVar(token)
            isStr(token) -> parseStr(token)
            hasFunction(token) -> getFunction(token)?.run { parse() }
            hasParser(R::class.java) -> getParser(R::class.java)?.run { parse() }
            else -> executor { token.cast<R>() }
        }?.let { if (action) parse(it as IExecutor<Any>) else it } as? IExecutor<R> ?: error("Quest of $token failed!")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> actionOf(any: T): BaseAction<T>? {
        val clazz = any::class.java
        with(actions) {
            return (get(clazz)
                ?: (filterKeys { it.isAssignableFrom(clazz) }.values as Collection<BaseAction<Any>>).merge<Any, T>(clazz)
                    .also { it.register() }) as? BaseAction<T>?
        }
    }

    private fun <A : Any, T : A> Collection<BaseAction<A>>.merge(clazz: Class<*>): BaseAction<T> {
        return object : BaseAction<T>(clazz as Class<T>) {
            init {
                this@merge.forEach {
                    it.actions.forEach inner@{ action ->
                        val key = action.key
                        val exec = action.value
                        actions[key] = ActionExecutor { obj ->
                            val a = obj as? A? ?: return@ActionExecutor null
                            return@ActionExecutor exec.run { executor(a) }
                        }
                    }
                }
            }
        }
    }

}