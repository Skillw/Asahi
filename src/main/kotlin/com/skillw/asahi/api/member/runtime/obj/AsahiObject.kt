package com.skillw.asahi.api.member.runtime.obj

import com.skillw.asahi.api.member.ast.Serializable
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.FunctionHolder
import com.skillw.asahi.api.member.runtime.Var
import com.skillw.asahi.api.member.runtime.obj.basic.ArrayObj
import com.skillw.asahi.api.member.runtime.obj.basic.NumberObj
import com.skillw.asahi.api.member.runtime.obj.basic.StringObj
import com.skillw.asahi.api.member.runtime.type.AsahiClass
import com.skillw.asahi.api.member.runtime.type.ObjectClass
import com.skillw.asahi.api.member.runtime.type.TypeHolder
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className AsahiObject
 *
 * @author Glom
 * @date 2023/8/14 22:00 Copyright 2023 user. All rights reserved.
 */
open class AsahiObject(override val type: AsahiClass = ObjectClass, source: SourceHolder) : TypeHolder,
    ScriptHolder by type,
    SourceHolder by source,
    FunctionHolder by type, Serializable {
    private val properties = HashMap<String, Var>()

    open fun toJava(): Any? {
        return this
    }

    override fun clone(): AsahiObject {
        return AsahiObject(type, this).also {
            it.properties.putAll(properties)
        }
    }

    override fun invoke(name: String, vararg args: AsahiObject): AsahiObject = type.invoke(this, name, *args)
    override fun variables(): HashMap<String, Var> {
        return properties
    }

    override fun combine(other: AsahiContext): AsahiContext {
        if (other !is AsahiObject) {
            return this
        }
        properties.putAll(other.properties)
        return this
    }

    companion object {
        fun Any?.toObject(source: SourceHolder): AsahiObject {
            return this?.let { obj ->
                if (obj !is AsahiObject)
                    when (obj) {
                        is String -> StringObj(obj, source)
                        is Number -> NumberObj(obj.toDouble(), source)
                        is Array<*> -> ArrayObj(obj.map { it.toObject(source) }.toTypedArray(), source)
                        else -> JavaObject(obj)
                    }
                else obj

            } ?: NullObject
        }
    }

    override fun serialize(): Map<String, Any> = linkedMapOf("type" to type.name())

    override fun toString(): String {
        return serialize().toString()
    }
}