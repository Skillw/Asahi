package com.skillw.asahi.util

import com.skillw.asahi.internal.util.AsahiClassBean
import taboolib.common.platform.function.info
import taboolib.common5.Coerce
import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URISyntaxException
import java.net.URL
import java.net.URLDecoder
import java.text.DecimalFormat
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> Any?.castSafely(): T? {
    this ?: return null
    if (T::class.java.isEnum) {
        return Coerce.toEnum(this, T::class.java as Class<out Enum<*>>) as T
    }
    if (this is String && T::class.java == Any::class.java) {
        return if (this.contains(".")) {
            (toDoubleOrNull() ?: this) as T
        } else {
            (toIntOrNull() ?: this) as T
        }
    }
    return when (T::class.java) {
        String::class.java -> toString()
        Int::class.java, Integer::class.java -> Coerce.toInteger(this)
        Long::class.java, java.lang.Long::class.java -> Coerce.toLong(this)
        Short::class.java, java.lang.Short::class.java -> Coerce.toShort(this)
        Double::class.java, java.lang.Double::class.java -> Coerce.toDouble(this)
        Float::class.java, java.lang.Float::class.java -> Coerce.toFloat(this)
        Boolean::class.java, java.lang.Boolean::class.java -> Coerce.toBoolean(this)
        Byte::class.java, java.lang.Byte::class.java -> Coerce.toByte(this)
        else -> this as? T
    } as? T
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> Any?.cast(): T {
    return castSafely() as? T? ?: error("Object $this cannot cast to ${T::class.java.name}!")
}

private fun Any?.toDouble() = Coerce.toDouble(this)
private fun Any?.isNumber() = Coerce.asDouble(this).isPresent


fun check(a: Any?, symbol: String, b: Any?): Boolean {
    return when (symbol) {
        "<" -> a.toDouble() < b.toDouble()
        "<=" -> a.toDouble() <= b.toDouble()
        "==" -> when {
            a.isNumber() && b.isNumber() -> a.toDouble() == b.toDouble()
            else -> a == b
        }

        "!=" -> when {
            a.isNumber() && b.isNumber() -> a.toDouble() != b.toDouble()
            else -> a != b
        }

        "===" -> a === b
        "!==" -> a !== b
        ">" -> a.toDouble() > b.toDouble()
        ">=" -> a.toDouble() >= b.toDouble()
        "equals" -> a == b
        "!equals" -> a != b
        "in" -> when (b) {
            is Array<*> -> a in b
            is Collection<*> -> a in b
            //                  Asahirange的返回值就是ClosedRange<Double> 所以直接强转了
            is ClosedRange<*> -> (b as ClosedRange<Double>).contains(a.toDouble())
            else -> a.toString() in b.toString()
        }

        "!in" -> when (b) {
            is Array<*> -> a !in b
            is Collection<*> -> a !in b
            //                  Asahirange的返回值就是ClosedRange<Double> 所以直接强转了
            is ClosedRange<*> -> !(b as ClosedRange<Double>).contains(a.toDouble())
            else -> a.toString() !in b.toString()
        }

        "is" -> when (b) {
            is Class<*> -> b.isInstance(a)
            is String -> a!!::class.java.simpleName.equals(b, true) || a::class.java.name.equals(b, true)

            else -> false
        }

        "!is" -> when (b) {
            is Class<*> -> !b.isInstance(a)
            is String -> !a!!::class.java.simpleName.equals(b, true) && !a::class.java.name.equals(b, true)
            else -> false
        }

        "contains" -> a.toString().contains(b.toString())
        "!contains" -> !a.toString().contains(b.toString())
        "equalsIgnore" -> a.toString().equals(b.toString(), true)
        "!equalsIgnore" -> !a.toString().equals(b.toString(), true)
        else -> {
            println("Unknown symbol $symbol!")
            false
        }
    }
}

fun <R> Any.fastGet(name: String): R? {
    return AsahiClassBean.of(this::class.java)[this, name] as? R?
}

fun Any.fastSet(name: String, value: Any?) {
    AsahiClassBean.of(this::class.java)[this, name] = value
}

fun <R> Any.fastInvoke(method: String, vararg params: Any?): R {
    return AsahiClassBean.of(this::class.java).invoke(this, method, *params) as R
}

fun <R> Any.fastInvoke(method: String, types: Array<Class<*>>, vararg params: Any?): R {
    return AsahiClassBean.of(this::class.java).invoke(this, method, types, *params) as R
}

fun <R> safe(run: () -> R): R? = runCatching { run() }.getOrElse { it.printStackTrace(); null }


fun MutableMap<String, Any>.putDeep(key: String, value: Any): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]

        if (i == lastIndex) {
            map?.put(keyStr, value) ?: list?.set(keyStr.toInt(), value)
            break
        }

        when (val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            null -> {
                map?.let {
                    HashMap<String, Any>().also { newMap ->
                        it[keyStr] = newMap
                        map = newMap
                    }
                }
                list?.let {
                    val index = keyStr.toInt()
                    ArrayList<Any>().also { newList ->
                        it[index] = newList
                        list = newList
                    }
                }
            }

            else -> {
                return null
            }
        }
    }
    return null
}


fun MutableMap<String, Any>.getDeep(key: String): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]
        val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }
        if (i == lastIndex) return obj
        when (obj) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            else -> {
                return null
            }
        }
    }
    return null
}

fun String.toArgs(): Array<String> =

    if (this.contains(","))
        split(",").toTypedArray()
    else
        arrayOf(this)

fun Number.format(input: String = "#.##"): String {
    val decimalFormat = DecimalFormat(input)
    return decimalFormat.format(this)
}

fun String.existClass(): Boolean {
    return kotlin.runCatching { Class.forName(this) }.isSuccess
}

fun String.findClass(): Class<*>? {
    var clazz: Class<*>? = null
    val path = this
    kotlin.runCatching {
        clazz = Class.forName(path)
    }.exceptionOrNull()?.run {
        info("未找到类: $path")
    }
    return clazz
}

fun find(name: String): Any? {
    return name.findClass()
}

fun <T> silent(run: () -> T): T? {
    return runCatching { run() }.getOrNull()
}

fun getClasses(clazz: Class<*>): List<Class<*>> {
    val classes: MutableList<Class<*>> = java.util.ArrayList()
    val url = clazz.protectionDomain.codeSource.location
    silent {
        val src: File = try {
            File(url.toURI())
        } catch (e: URISyntaxException) {
            File(url.path)
        }
        JarFile(src).stream().filter { entry: JarEntry ->
            entry.name.endsWith(".class")
        }.forEach { entry: JarEntry ->
            val className =
                entry.name.replace('/', '.').substring(0, entry.name.length - 6)
            silent { classes.add(Class.forName(className, false, clazz.classLoader)) }
        }
    }
    return classes
}

/**
 * 从包package中获取所有的Class
 *
 * @param pack
 * @return
 */

fun getClasses(pack: String): Set<Class<*>> {

    // 第一个class类的集合
    val classes: MutableSet<Class<*>> = LinkedHashSet()
    // 是否循环迭代
    val recursive = true
    // 获取包的名字 并进行替换
    var packageName = pack
    val packageDirName = packageName.replace('.', '/')
    // 定义一个枚举的集合 并进行循环来处理这个目录下的things
    val dirs: Enumeration<URL>
    try {
        dirs = Thread.currentThread().contextClassLoader.getResources(packageDirName)
        // 循环迭代下去
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            val url: URL = dirs.nextElement()
            // 得到协议的名称
            val protocol: String = url.protocol
            // 如果是以文件的形式保存在服务器上
            if ("file" == protocol) {
                System.err.println("file类型的扫描")
                // 获取包的物理路径
                val filePath = URLDecoder.decode(url.file, "UTF-8")
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes)
            } else if ("jar" == protocol) {
                // 如果是jar包文件
                // 定义一个JarFile
                // System.err.println("jar类型的扫描");
                var jar: JarFile
                try {
                    // 获取jar
                    jar = (url.openConnection() as JarURLConnection).jarFile
                    // 从此jar包 得到一个枚举类
                    val entries = jar.entries()
                    // 同样的进行循环迭代
                    while (entries.hasMoreElements()) {
                        // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                        val entry = entries.nextElement()
                        var name = entry.name
                        // 如果是以/开头的
                        if (name[0] == '/') {
                            // 获取后面的字符串
                            name = name.substring(1)
                        }
                        // 如果前半部分和定义的包名相同
                        if (name.startsWith(packageDirName)) {
                            val idx = name.lastIndexOf('/')
                            // 如果以"/"结尾 是一个包
                            if (idx != -1) {
                                // 获取包名 把"/"替换成"."
                                packageName = name.substring(0, idx).replace('/', '.')
                            }
                            // 如果可以迭代下去 并且是一个包
                            if (idx != -1 || recursive) {
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory) {
                                    // 去掉后面的".class" 获取真正的类名
                                    val className = name.substring(packageName.length + 1, name.length - 6)
                                    runCatching {
                                        classes.add(Class.forName("$packageName.$className"))
                                    }
                                }
                            }
                        }
                    }
                } catch (e: IOException) {
                    // log.error("在扫描用户定义视图时从jar包获取文件出错");
                    e.printStackTrace()
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return classes
}

val Class<*>.instance: Any?
    get() = kotlin.runCatching {
        getDeclaredField("INSTANCE").get(null) ?: getField("instance").get(null) ?: getConstructor().newInstance()
    }.getOrNull()

/**
 * 以文件的形式来获取包下的所有Class
 *
 * @param packageName
 * @param packagePath
 * @param recursive
 * @param classes
 */

fun findAndAddClassesInPackageByFile(
    packageName: String, packagePath: String?, recursive: Boolean,
    classes: MutableSet<Class<*>>,
) {
    // 获取此包的目录 建立一个File
    val dir = File(packagePath)
    // 如果不存在或者 也不是目录就直接返回
    if (!dir.exists() || !dir.isDirectory) {
        // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
        return
    }
    // 如果存在 就获取包下的所有文件 包括目录
    val dirfiles: Array<out File> = dir.listFiles { file ->
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        recursive && file.isDirectory || file.name.endsWith(".class")
    }!!
    // 循环所有文件
    for (file in dirfiles) {
        // 如果是目录 则继续扫描
        if (file.isDirectory) {
            findAndAddClassesInPackageByFile(
                packageName + "." + file.name, file.absolutePath, recursive,
                classes
            )
        } else {
            // 如果是java类文件 去掉后面的.class 只留下类名
            val className = file.name.substring(0, file.name.length - 6)
            try {
                // 添加到集合中去
                // classes.add(Class.forName(packageName + '.' + className));
                // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                classes.add(
                    Thread.currentThread().contextClassLoader.loadClass("$packageName.$className")
                )
            } catch (e: ClassNotFoundException) {
                // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                e.printStackTrace()
            }
        }
    }
}

// --------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------


fun getByInterface(clazz: Class<*>, classesAll: Set<Class<*>>): Set<Class<*>> {
    val classes: MutableSet<Class<*>> = LinkedHashSet()
    // 获取指定接口的实现类
    if (!clazz.isInterface) {
        try {
            /** 循环判断路径下的所有类是否继承了指定类 并且排除父类自己 */
            val iterator = classesAll.iterator()
            while (iterator.hasNext()) {
                val cls = iterator.next()
                /**
                 * isAssignableFrom该方法的解析，请参考博客：
                 * http://blog.csdn.net/u010156024/article/details/44875195
                 */
                if (clazz.isAssignableFrom(cls)) {
                    if (clazz != cls) { // 自身并不加进去
                        classes.add(cls)
                    } else {
                    }
                }
            }
        } catch (e: Exception) {
            println("出现异常")
        }
    }
    return classes
}

fun randomInt(a: Int, b: Int): Int {
    return taboolib.common.util.random(a, b)
}

fun random(a: Number, b: Number, format: String = "#.##"): String {
    return taboolib.common.util.random(a.toDouble(), b.toDouble()).format(format)
}

fun debug(msg: Any?) {
    info("&9[&eDebug&9] &7${msg}")
}