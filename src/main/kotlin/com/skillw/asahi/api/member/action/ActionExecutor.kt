package com.skillw.asahi.api.member.action

import com.skillw.asahi.api.member.context.ActionContext


/**
 * @className ActionExecutor
 *
 * @author Glom
 * @date 2022年12月27日 8:36 Copyright 2022 user. All rights reserved.
 */
fun interface ActionExecutor<T> {
    fun ActionContext.executor(obj: T): Any?
}