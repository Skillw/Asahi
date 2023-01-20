package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager

/**
 * @className Namespacing
 *
 * @author Glom
 * @date 2023/1/20 10:12 Copyright 2023 user. All rights reserved.
 */
interface Namespacing {
    val namespace: String
    fun namespace(): Namespace {
        return AsahiManager.getNamespace(namespace)
    }
}