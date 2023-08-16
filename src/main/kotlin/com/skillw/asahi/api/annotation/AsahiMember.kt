package com.skillw.asahi.api.annotation

/**
 * Asahi Prefix annotation
 *
 * 自动注册
 *
 * @constructor Asahi Prefix annotation
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Repeatable
annotation class AsahiMember
