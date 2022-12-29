package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.manager.AsahiManager.actionOf
import com.skillw.asahi.api.manager.AsahiManager.allActions
import com.skillw.asahi.api.member.context.ActionContext
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.reader.AsahiReader
import com.skillw.asahi.api.member.reader.IAsahiReader
import java.util.*


object ParserAction {
    private val ignores = HashSet<String>().apply {
        add("\n")
        add("then")
        //你可以在这里把所有前缀动作token加进去，来排除掉将前缀动作当成后缀动作
        //这里我牺牲掉了同行多个动作 来换取 前缀动作与后缀动作的正常运行
        //所以没有加入所有的前缀动作token
    }

    //这里是 后缀动作 的编译(parse)
    //在这时，我是不知道每个对象是什么类型的，只有一个Supplier
    fun IAsahiReader.parse(getter: IExecutor<Any>): IExecutor<Any> {
        //查看下一个token 是否在"忽略列表"中
        if (peek() in ignores) return getter
        //查看下一个token 是否在 allActions里(所有后缀动作的set)
        if (peek() !in allActions) return getter
        //以上这俩判断，都是为了过滤掉非后缀动作

        //确定后缀动作token
        val action = next()

        val tokens = LinkedList<String>()
        while (hasNext() && peek() != "\n") {
            tokens += next()
        }
        //后缀动作 参数
        val actionReader = AsahiReader.of(tokens)
        //后缀动作 上下文
        val context = executor { ActionContext(context, actionReader, action) }
        //执行内容 （Supplier）
        return executor {
            //获取对象
            //这时候才知道对象的类型
            val obj = getter.get()
            //获取上下文
            context.get().run {
                //基于对象类型 获取BaseAction<T>对象 并调用动作
                actionOf(obj)?.run { action(obj) } ?: "null"
            }
        }
    }
//    fun <T : Any> actionOf(any: T): BaseAction<T>? {
//        return actions.get(any::class.java)
//    }
}