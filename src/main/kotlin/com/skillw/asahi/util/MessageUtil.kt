package com.skillw.asahi.util

import taboolib.module.chat.colored


object MessageUtil {

    @JvmStatic
    fun warning(msg: Any?) {
        taboolib.common.platform.function.warning(msg.toString().colored())
    }


    @JvmStatic
    fun info(msg: Any?) {
        println(msg.toString().colored())
    }


    @JvmStatic
    fun debug(msg: Any?) {
        println("&9[&eDebug&9] &7${msg}".colored())
    }

}