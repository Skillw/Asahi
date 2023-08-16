package com.skillw.asahi.api.member.ast

/**
 * @className Serializable
 *
 * @author Glom
 * @date 2023/8/13 23:42 Copyright 2023 user. All rights reserved.
 */
interface Serializable {
    fun serialize(): Map<String, Any>
}
