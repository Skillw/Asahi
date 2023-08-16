package com.skillw.asahi.util

import com.google.gson.Gson

/**
 * @className TypeUtils
 *
 * @author Glom
 * @date 2023/8/15 0:53 Copyright 2023 user. All rights reserved.
 */
private val gson = Gson()

fun Any?.toJson(): String = gson.toJson(this)