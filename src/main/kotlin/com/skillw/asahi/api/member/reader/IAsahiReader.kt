package com.skillw.asahi.api.member.reader

interface IAsahiReader {
    val origin: String

    /**
     * Except
     *
     * @param excepts
     * @return
     */
    fun except(vararg excepts: String): Boolean
    fun prepareTokens(): ArrayList<String>

    /**
     * Has next
     *
     * @return
     */
    fun hasNext(): Boolean
    fun current(): String

    /**
     * Next
     *
     * @return
     */
    fun next(): String
    fun previous(): String?
    fun currentIndex(): Int
    fun peekNextIgnoreBlank(): String?
    fun peek(): String?
    fun skipTill(from: String, till: String): Boolean
    fun splitTill(from: String, to: String): String
    fun reset()
}