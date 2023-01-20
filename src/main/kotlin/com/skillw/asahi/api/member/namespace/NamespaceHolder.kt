package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix

/**
 * @className NamespaceHolder
 *
 * @author Glom
 * @date 2023/1/19 16:48 Copyright 2023 user. All rights reserved.
 */
interface NamespaceHolder<T : NamespaceHolder<T>> {
    val namespaces: MutableSet<Namespace>
    fun namespaceNames(): Array<String> = namespaces.map { it.key }.toTypedArray()
    fun addNamespaces(vararg names: String): T {
        namespaces.addAll(AsahiManager.getNamespaces(*names))
        return this as T
    }

    fun removeSpaces(vararg names: String): T {
        namespaces.removeAll(AsahiManager.getNamespaces(*names))
        return this as T
    }

    fun hasFunction(key: String): Boolean {
        return namespaces.any { it.hasPrefix(key) }
    }

    fun getFunction(key: String): List<BasePrefix<*>> {
        return namespaces.filter { it.hasPrefix(key) }.map { it.getPrefix(key)!! }
    }
}