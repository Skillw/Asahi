package com.skillw.asahi.internal.function

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionPrint {
    val print = createFunction("print", namespaces = arrayOf("lang")) {
        //开始此前缀动作的"编译"(parse)
        val content = quest<Any>()  //请求一个任意类型对象，注意不是直接返回对象，是返回一个Supplier<Any>
        //返回执行内容(这玩意也是个Supplier)
        function {
            //     调用get 获取那个任意类型对象
            content.get().also {
                //打印它
                println(it)
            }
        }
    }
    //正常前缀表达式思路 可以看到 编译 与 执行 是分开运行的
}