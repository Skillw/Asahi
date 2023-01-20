package com.skillw.asahi.api.annotation

/**
 * Asahi function annotation
 *
 * 自动注册
 *
 * @constructor Asahi function annotation
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class AsahiPrefix(val names: Array<String> = [], val namespace: String = "common")
