/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.skillw.asahi.util

import com.google.common.base.Preconditions
import com.google.common.collect.Lists
import com.google.common.primitives.*
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/** Utility class for coercing unknown values to specific target types. */
object Coerce {
    private val listPattern = Pattern.compile("^([(\\[{]?)(.+?)([)\\]}]?)$")
    private val listPairings = arrayOf("([{", ")]}")
    fun format(value: Double): Double {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun format(value: Double, scale: Int): Double {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Coerce the supplied object to a string.
     *
     * @param obj Object to coerce
     * @return Object as a string, empty string if the object is null
     */
    fun toString(obj: Any?): String {
        if (obj == null) {
            return ""
        }
        return if (obj.javaClass.isArray) {
            toList(obj).toString()
        } else obj.toString()
    }

    /**
     * Gets the given object as a [String].
     *
     * @param obj The object to translate
     * @return The string value, if available
     */
    fun asString(obj: Any?): Optional<String> {
        return if (obj is String) {
            Optional.of(obj)
        } else if (obj == null) {
            Optional.empty()
        } else {
            Optional.of(obj.toString())
        }
    }

    /**
     * Coerce the supplied object to a list. Accepts lists and all types of
     * 1D arrays. Also (naively) supports lists in Strings in a format like
     * `{1,2,3,I,am,a,list}`
     *
     * @param obj Object to coerce
     * @return Some kind of List filled with unimaginable horrors
     */
    fun toList(obj: Any?): List<*> {
        if (obj == null) {
            return emptyList<Any>()
        }
        if (obj is List<*>) {
            return obj
        }
        val clazz: Class<*> = obj.javaClass
        return if (clazz.isArray) {
            if (clazz.componentType.isPrimitive) {
                primitiveArrayToList(obj)
            } else Arrays.asList(*obj as Array<Any?>)
        } else parseStringToList(obj.toString())
    }

    /**
     * Gets the given object as a [List].
     *
     * @param obj The object to translate
     * @return The list, if available
     */
    fun asList(obj: Any?): Optional<List<*>> {
        if (obj == null) {
            return Optional.empty()
        }
        if (obj is List<*>) {
            return Optional.of(obj)
        }
        val clazz: Class<*> = obj.javaClass
        return if (clazz.isArray) {
            if (clazz.componentType.isPrimitive) {
                Optional.of(primitiveArrayToList(obj))
            } else Optional.of(Arrays.asList(*obj as Array<Any?>))
        } else Optional.of(parseStringToList(obj.toString()))
    }

    /**
     * Coerce the specified object to a list containing only objects of type
     * specified by `ofClass`. Also coerces list values where possible.
     *
     * @param obj Object to coerce
     * @param ofClass Class to coerce to
     * @param <T> type of list (notional)
     * @return List of coerced values </T>
     */
    fun <T> toListOf(obj: Any?, ofClass: Class<T>): List<T> {
        Preconditions.checkNotNull(ofClass, "ofClass")
        val filteredList: MutableList<T> = Lists.newArrayList()
        for (o in toList(obj)) {
            if (o?.javaClass?.let { ofClass.isAssignableFrom(it) } == true) {
                filteredList.add(o as T)
            } else if (ofClass == String::class.java) {
                filteredList.add(toString(o) as T)
            } else if (ofClass == Integer.TYPE || ofClass == Int::class.java) {
                filteredList.add(toInteger(o) as T)
            } else if (ofClass == java.lang.Float.TYPE || ofClass == Float::class.java) {
                filteredList.add(toDouble(o) as T)
            } else if (ofClass == java.lang.Double.TYPE || ofClass == Double::class.java) {
                filteredList.add(toDouble(o) as T)
            } else if (ofClass == java.lang.Boolean.TYPE || ofClass == Boolean::class.java) {
                filteredList.add(toBoolean(o) as T)
            }
        }
        return filteredList
    }

    /**
     * Coerce the supplied object to a boolean, matches strings such as "yes"
     * as well as literal boolean values.
     *
     * @param obj Object to coerce
     * @return Object as a boolean, `false` if the object is null
     */
    fun toBoolean(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj is Boolean) {
            return obj
        }
        val value = obj.toString().trim { it <= ' ' }
        return value == "1" || value.equals("true", ignoreCase = true) || value.equals("yes", ignoreCase = true)
    }

    /**
     * Gets the given object as a [Boolean].
     *
     * @param obj The object to translate
     * @return The boolean, if available
     */
    fun asBoolean(obj: Any?): Optional<Boolean> {
        if (obj is Boolean) {
            return Optional.of(obj)
        } else if (obj is Byte) {
            return Optional.of(obj.toInt() != 0)
        }
        return Optional.empty()
    }

    /**
     * Coerce the supplied object to an integer, parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as an integer, `0` if the object is null or cannot be
     *     parsed
     */
    fun toInteger(obj: Any?): Int {
        if (obj == null) {
            return 0
        }
        if (obj is Number) {
            return obj.toInt()
        }
        val strObj = sanitiseNumber(obj)
        val iParsed = Ints.tryParse(strObj)
        if (iParsed != null) {
            return iParsed
        }
        val dParsed = Doubles.tryParse(strObj)
        return dParsed?.toInt() ?: 0
    }

    /**
     * Gets the given object as a [Integer].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The integer value, if available
     */
    fun asInteger(obj: Any?): Optional<Int> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toInt())
        }
        try {
            return Optional.of(Integer.valueOf(obj.toString()))
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        val strObj = sanitiseNumber(obj)
        val iParsed = Ints.tryParse(strObj)
        if (iParsed == null) {
            val dParsed = Doubles.tryParse(strObj)
            // try parsing as double now
            return if (dParsed == null) Optional.empty() else Optional.of(dParsed.toInt())
        }
        return Optional.of(iParsed)
    }

    /**
     * Coerce the supplied object to a double-precision floating-point number,
     * parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a double, `0.0` if the object is null or cannot be
     *     parsed
     */
    fun toDouble(obj: Any?): Double {
        if (obj == null) {
            return 0.0
        }
        if (obj is Number) {
            return obj.toDouble()
        }
        val parsed = Doubles.tryParse(sanitiseNumber(obj))
        return parsed ?: 0.0
    }

    /**
     * Gets the given object as a [Double].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The double value, if available
     */
    fun asDouble(obj: Any?): Optional<Double> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toDouble())
        }
        try {
            return Optional.of(java.lang.Double.valueOf(obj.toString()))
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        val strObj = sanitiseNumber(obj)
        val dParsed = Doubles.tryParse(strObj)
        // try parsing as double now
        return if (dParsed == null) Optional.empty() else Optional.of(dParsed)
    }

    /**
     * Coerce the supplied object to a single-precision floating-point number,
     * parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a float, `0.0` if the object is null or cannot be
     *     parsed
     */
    fun toFloat(obj: Any?): Float {
        if (obj == null) {
            return 0.0f
        }
        if (obj is Number) {
            return obj.toFloat()
        }
        val parsed = Floats.tryParse(sanitiseNumber(obj))
        return parsed ?: 0.0f
    }

    /**
     * Gets the given object as a [Float].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The float value, if available
     */
    fun asFloat(obj: Any?): Optional<Float> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toFloat())
        }
        try {
            return Optional.of(java.lang.Float.valueOf(obj.toString()))
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        val strObj = sanitiseNumber(obj)
        val dParsed = Doubles.tryParse(strObj)
        return if (dParsed == null) Optional.empty() else Optional.of(dParsed.toFloat())
    }

    /**
     * Coerce the supplied object to a short number, parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a short, `0` if the object is null or cannot be parsed
     */
    fun toShort(obj: Any?): Short {
        if (obj == null) {
            return 0
        }
        return if (obj is Number) {
            obj.toShort()
        } else try {
            sanitiseNumber(obj).toShort()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Gets the given object as a [Short].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The short value, if available
     */
    fun asShort(obj: Any?): Optional<Short> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toShort())
        }
        try {
            return Optional.of(sanitiseNumber(obj).toShort())
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        return Optional.empty()
    }

    /**
     * Coerce the supplied object to a byte number, parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a byte, `0` if the object is null or cannot be parsed
     */
    fun toByte(obj: Any?): Byte {
        if (obj == null) {
            return 0
        }
        return if (obj is Number) {
            obj.toByte()
        } else try {
            sanitiseNumber(obj).toByte()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Gets the given object as a [Byte].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The byte value, if available
     */
    fun asByte(obj: Any?): Optional<Byte> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toByte())
        }
        try {
            return Optional.of(sanitiseNumber(obj).toByte())
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        return Optional.empty()
    }

    /**
     * Coerce the supplied object to a long number, parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a long, `0` if the object is null or cannot be parsed
     */
    fun toLong(obj: Any?): Long {
        if (obj == null) {
            return 0
        }
        return if (obj is Number) {
            obj.toLong()
        } else try {
            sanitiseNumber(obj).toLong()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Gets the given object as a [Long].
     *
     * Note that this does not translate numbers spelled out as strings.
     *
     * @param obj The object to translate
     * @return The long value, if available
     */
    fun asLong(obj: Any?): Optional<Long> {
        if (obj == null) {
            // fail fast
            return Optional.empty()
        }
        if (obj is Number) {
            return Optional.of(obj.toLong())
        }
        try {
            return Optional.of(sanitiseNumber(obj).toLong())
        } catch (e: NumberFormatException) {
            // do nothing
        } catch (e: NullPointerException) {
        }
        return Optional.empty()
    }

    /**
     * Coerce the supplied object to a character, parse it if necessary.
     *
     * @param obj Object to coerce
     * @return Object as a character, `'\u0000'` if the object is null or
     *     cannot be parsed
     */
    fun toChar(obj: Any?): Char {
        if (obj == null) {
            return '0'
        }
        if (obj is Char) {
            return obj
        }
        try {
            return obj.toString()[0]
        } catch (e: Exception) {
            // do nothing
        }
        return '\u0000'
    }

    /**
     * Gets the given object as a [Character].
     *
     * @param obj The object to translate
     * @return The character, if available
     */
    fun asChar(obj: Any?): Optional<Char> {
        if (obj == null) {
            return Optional.empty()
        }
        if (obj is Char) {
            return Optional.of(obj)
        }
        try {
            return Optional.of(obj.toString()[0])
        } catch (e: Exception) {
            // do nothing
        }
        return Optional.empty()
    }

    /**
     * Coerce the specified object to an enum of the supplied type, returns the
     * first enum constant in the enum if parsing fails.
     *
     * @param obj Object to coerce
     * @param enumClass Enum class to coerce to
     * @param <E> enum type
     * @return Coerced enum value </E>
     */
    fun <E : Enum<E>?> toEnum(obj: Any?, enumClass: Class<E>): E {
        return toEnum(obj, enumClass, enumClass.enumConstants[0])
    }

    /**
     * Coerce the specified object to an enum of the supplied type, returns the
     * specified default value if parsing fails.
     *
     * @param obj Object to coerce
     * @param enumClass Enum class to coerce to
     * @param defaultValue default value to return if coercion fails
     * @param <E> enum type
     * @return Coerced enum value </E>
     */
    fun <E : Enum<E>?> toEnum(obj: Any?, enumClass: Class<E>, defaultValue: E): E {
        Preconditions.checkNotNull(enumClass, "enumClass")
        Preconditions.checkNotNull(defaultValue, "defaultValue")
        if (obj == null) {
            return defaultValue
        }
        if (enumClass.isAssignableFrom(obj.javaClass)) {
            return obj as E
        }
        val strObj = obj.toString().trim { it <= ' ' }
        try {
            // Efficient but case-sensitive lookup in the constant map
            return java.lang.Enum.valueOf(enumClass, strObj)
        } catch (ex: IllegalArgumentException) {
            // fall through
        }

        // Try a case-insensitive lookup
        for (value in enumClass.enumConstants) {
            if (value!!.name.equals(strObj, ignoreCase = true)) {
                return value
            }
        }
        return defaultValue
    }

    /**
     * Coerce the specified object to the specified pseudo-enum type using the
     * supplied pseudo-enum dictionary class.
     *
     * @param obj Object to coerce
     * @param pseudoEnumClass The pseudo-enum class
     * @param dictionaryClass Pseudo-enum dictionary class to look in
     * @param defaultValue Value to return if lookup fails
     * @param <T> pseudo-enum type
     * @return Coerced value or default if coercion fails </T>
     */
    fun <T> toPseudoEnum(obj: Any?, pseudoEnumClass: Class<T>, dictionaryClass: Class<*>, defaultValue: T): T {
        Preconditions.checkNotNull(pseudoEnumClass, "pseudoEnumClass")
        Preconditions.checkNotNull(dictionaryClass, "dictionaryClass")
        Preconditions.checkNotNull(defaultValue, "defaultValue")
        if (obj == null) {
            return defaultValue
        }
        if (pseudoEnumClass.isAssignableFrom(obj.javaClass)) {
            return obj as T
        }
        val strObj = obj.toString().trim { it <= ' ' }
        try {
            for (field in dictionaryClass.fields) {
                if (field.modifiers and Modifier.STATIC != 0 && pseudoEnumClass.isAssignableFrom(field.type)) {
                    val fieldName = field.name
                    val entry = field[null] as T
                    if (strObj.equals(fieldName, ignoreCase = true)) {
                        return entry
                    }
                }
            }
        } catch (ex: Exception) {
            // well that went badly
        }
        return defaultValue
    }

    /**
     * Sanitise a string containing a common representation of a number to make
     * it parsable. Strips thousand-separating commas and trims later members
     * of a comma-separated list. For example the string "(9.5, 10.6, 33.2)"
     * will be sanitised to "9.5".
     *
     * @param obj Object to sanitise
     * @return Sanitised number-format string to parse
     */
    private fun sanitiseNumber(obj: Any): String {
        var string = obj.toString().trim { it <= ' ' }
        if (string.length < 1) {
            return "0"
        }
        val candidate = listPattern.matcher(string)
        if (listBracketsMatch(candidate)) {
            string = candidate.group(2).trim { it <= ' ' }
        }
        val decimal = string.indexOf('.')
        val comma = string.indexOf(',', decimal)
        if (decimal > -1 && comma > -1) {
            return sanitiseNumber(string.substring(0, comma))
        }
        return if (string.indexOf('-', 1) != -1) {
            "0"
        } else string.replace(",", "").split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    private fun listBracketsMatch(candidate: Matcher): Boolean {
        return candidate.matches() && listPairings[0].indexOf(candidate.group(1)) == listPairings[1].indexOf(
            candidate.group(
                3
            )
        )
    }

    private fun primitiveArrayToList(obj: Any): List<*> {
        if (obj is BooleanArray) {
            return Booleans.asList(*obj)
        } else if (obj is CharArray) {
            return Chars.asList(*obj)
        } else if (obj is ByteArray) {
            return Bytes.asList(*obj)
        } else if (obj is ShortArray) {
            return Shorts.asList(*obj)
        } else if (obj is IntArray) {
            return Ints.asList(*obj)
        } else if (obj is LongArray) {
            return Longs.asList(*obj)
        } else if (obj is FloatArray) {
            return Floats.asList(*obj)
        } else if (obj is DoubleArray) {
            return Doubles.asList(*obj)
        }
        return emptyList<Any>()
    }

    private fun parseStringToList(string: String): List<*> {
        val candidate = listPattern.matcher(string)
        if (!listBracketsMatch(candidate)) {
            return emptyList<Any>()
        }
        val list: MutableList<String> = Lists.newArrayList()
        for (part in candidate.group(2).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (part != null) {
                list.add(part)
            }
        }
        return list
    }
}