package com.skillw.asahi.internal.inject

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.annotation.AsahiParser
import com.skillw.asahi.api.member.action.BaseAction
import com.skillw.asahi.api.member.function.BaseFunction
import com.skillw.asahi.api.member.parser.BaseParser
import com.skillw.asahi.util.PluginUtils

object Register {
    fun register(path: String) {
        PluginUtils.getClasses(path).run {
            filter { it.isAnnotationPresent(AsahiParser::class.java) }
                .forEach { clazz ->
                    val obj = clazz.getField("INSTANCE").get(null)
                    clazz.declaredFields.forEach inner@{ field ->
                        field.isAccessible = true
                        if (field.type != BaseParser::class.java) return@inner
                        val parser = field.get(obj) as BaseParser<*>
                        parser.register()
                    }
                }
            filter { it.isAnnotationPresent(AsahiFunction::class.java) }
                .forEach { clazz ->
                    val obj = clazz.getField("INSTANCE").get(null)
                    clazz.declaredFields.forEach inner@{ field ->
                        field.isAccessible = true
                        if (field.type != BaseFunction::class.java) return@inner
                        val function = field.get(obj) as BaseFunction<*>
                        function.register()
                    }
                }
            filter { it.isAnnotationPresent(AsahiAction::class.java) }
                .filterIsInstance<Class<BaseAction<*>>>()
                .forEach { clazz ->
                    val obj = clazz.getField("INSTANCE").get(null) as? BaseAction<*>
                    obj?.register()
                }
        }
    }

    fun registerAsahi() {
        register("com.skillw.asahi")
    }
}