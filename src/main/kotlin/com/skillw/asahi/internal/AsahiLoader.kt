package com.skillw.asahi.internal

import com.skillw.asahi.api.annotation.*
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.Namespace
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.parser.prefix.namespacing.BaseJavaPrefix
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixCreator
import com.skillw.asahi.api.member.parser.prefix.type.TypeParser
import com.skillw.asahi.util.getClasses
import com.skillw.asahi.util.instance
import com.skillw.asahi.util.safe
import taboolib.library.reflex.ClassAnnotation
import taboolib.library.reflex.ClassStructure
import taboolib.library.reflex.ReflexClass

object AsahiLoader {
    fun inject(clazz: ClassStructure) {
        safe {
            with(clazz) {
                register<AsahiNamespace, Namespace>()
                register<AsahiTopParser, TopPrefixParser<*>>()
                register<AsahiTypeParser, TypeParser<*>>()
                register<AsahiInfix, BaseInfix<*>>()
                register<AsahiGetter, AsahiContext.Getter>()
                register<AsahiSetter, AsahiContext.Setter>()
                registerFunctions()
            }
        }
    }

    fun init(vararg paths: String) {
        paths.forEach { path ->
            getClasses(path).map { ReflexClass.of(it).structure }.forEach(::inject)
        }
    }

    private fun <R> PrefixCreator<R>.register(asahi: ClassAnnotation, defaultName: String = "") {
        val names = asahi.property<Array<String>>("names")!!
        val namespace = asahi.property<String>("namespace")!!
        val key = names.firstOrNull() ?: defaultName
        register(key, *names, namespace = namespace)
    }

    private fun ClassStructure.registerFunctions() {
        val obj = owner.instance
        if (obj is BasePrefix<*> && isAnnotationPresent(AsahiPrefix::class.java))
            obj.register()
        else if (obj is BaseJavaPrefix<*> && isAnnotationPresent(AsahiPrefix::class.java))
            obj.register(getAnnotation(AsahiPrefix::class.java), "none")
        else
            methods.forEach inner@{ method ->
                safe {
                    if (!method.isAnnotationPresent(AsahiPrefix::class.java) || method.returnType.simpleName != "PrefixCreator") return@safe
                    println(method.name)
                    println(obj)
                    val creator =
                        (if (method.isStatic) method.invokeStatic() else method.invoke(obj!!)) as PrefixCreator<*>
                    creator.register(method.getAnnotation(AsahiPrefix::class.java), method.name)
                }
            }

    }

    private inline fun <reified A : Annotation, reified R : AsahiRegistrable<*>> ClassStructure.register() {
        val obj = owner.instance
        if (obj is R && isAnnotationPresent(A::class.java)) {
            obj.register()
        } else
            methods.forEach inner@{ method ->
                safe {
                    if (!method.isAnnotationPresent(A::class.java) || !R::class.java.isAssignableFrom(method.returnType)) return@safe
                    val parser = if (method.isStatic) method.invokeStatic() else method.invoke(obj!!) as R
                    (parser as? R?)?.register()
                }
            }
    }

}
