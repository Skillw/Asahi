package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix

open class Namespace(override val key: String, val shared: Boolean = false) : AsahiRegistrable<String> {
    /** Functions 函数容器， key -> 函数 */
    internal val functions = HashMap<String, BasePrefix<*>>()

    /** Actions 动作容器， 类型 -> 动作 */
    internal val actions = java.util.HashMap<Class<*>, BaseInfix<*>>()

    internal val allActions = HashSet<String>()

    fun hasFunction(key: String): Boolean {
        return functions.containsKey(key)
    }

    fun getFunction(key: String): BasePrefix<*>? {
        return functions[key]
    }

    fun registerFunction(func: BasePrefix<*>) {
        val keys = listOf(func.key, *func.alias)
        keys.forEach { key ->
            functions[key] = func
        }
    }

    fun <T : Any> getAction(type: Class<T>): BaseInfix<T> {
        return actions[type] as? BaseInfix<T>? ?: kotlin.run {
            val newAction = BaseInfix.createInfix(type)
            actions.entries.sortedWith { a, b ->
                if (a.key.isAssignableFrom(b.key)) -1 else 1
            }.forEach {
                newAction.putAll(it.value)
            }
            newAction.apply { register() }
        }
    }

    fun registerAction(action: BaseInfix<*>) {
        val type = action.key
        if (actions.containsKey(type)) {
            actions[type]?.putAll(action)
            return
        }
        actions[type] = action
        action.actions.keys.forEach(allActions::add)
    }

    fun hasAction(action: String?): Boolean {
        return allActions.contains(action)
    }

    fun hasAction(type: Class<*>): Boolean {
        return actions.containsKey(type)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> actionOf(any: T): BaseInfix<T> {
        return getAction(any::class.java as Class<T>)
    }

    override fun register() {
        AsahiManager.namespaces[key] = this
    }

}
