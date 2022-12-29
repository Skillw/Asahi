package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionArray : BaseAction<Array<*>>(Array::class.java) {
    init {
        //添加后缀动作
        addExec("get") { obj ->
            except("at")
            //         这里直接在 后缀动作参数 里获取一个Int
            val index = parse<Int>()
            //返回结果
            return@addExec obj[index]
        }

        addExec("set") { obj ->
            obj as? Array<Any?> ?: error("The obj should be a Array<Any>")
            except("at")
            val index = parse<Int>()
            except("to")
            val value = parse<Any>()
            obj[index] = value
            return@addExec value
        }
        addExec("length") { it.size }
    }
}