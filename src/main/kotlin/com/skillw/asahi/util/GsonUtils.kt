package com.skillw.asahi.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtils {
    val gson: Gson by lazy {
        GsonBuilder().serializeNulls().create()
    }

    inline fun <reified T> T.encodeJson(): String {
        return gson.toJson(this)
    }


    inline fun <reified T> String.decodeFromString(): T? {
        return gson.fromJson(this, T::class.java)
    }
}