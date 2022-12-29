package com.skillw.asahi.util

object TypeUtils {

    @Suppress("IMPLICIT_CAST_TO_ANY")
    inline fun <reified T> Any?.cast(): T {
        return when (T::class.java) {
            String::class.java -> toString()
            Int::class.java, Integer::class.java -> Coerce.toInteger(this)
            Long::class.java, java.lang.Long::class.java -> Coerce.toLong(this)
            Short::class.java, java.lang.Short::class.java -> Coerce.toShort(this)
            Double::class.java, java.lang.Double::class.java -> Coerce.toDouble(this)
            Float::class.java, java.lang.Float::class.java -> Coerce.toFloat(this)
            Boolean::class.java, java.lang.Boolean::class.java -> Coerce.toBoolean(this)
            Byte::class.java, java.lang.Byte::class.java -> Coerce.toByte(this)
            else -> this as? T ?: error("Obj $this cast to ${T::class.java} failed!")
        } as T
    }

    private fun Any?.toDouble() = Coerce.toDouble(this)
    private fun Any?.isNumber() = Coerce.asDouble(this).isPresent

    @JvmStatic
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
                is Collection<*> -> a in b
                //                  内联函数range的返回值就是ClosedRange<Double> 所以直接强转了
                is ClosedRange<*> -> (b as ClosedRange<Double>).contains(a.toDouble())
                else -> a.toString() in b.toString()
            }

            "!in" -> when (b) {
                is Collection<*> -> a !in b
                //                  内联函数range的返回值就是ClosedRange<Double> 所以直接强转了
                is ClosedRange<*> -> !(b as ClosedRange<Double>).contains(a.toDouble())
                else -> a.toString() !in b.toString()
            }

            "is" -> when {
                b is Class<*> -> b.isInstance(a)
                else -> false
            }

            "!is" -> when (b) {
                is Class<*> -> !b.isInstance(a)
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


}