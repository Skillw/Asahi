package com.skillw.asahi.api.member.runtime.obj

import com.skillw.asahi.api.member.runtime.type.JavaClass
import com.skillw.asahi.api.member.tokenizer.source.NoSource
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className JavaObject
 *
 * @author Glom
 * @date 2023/8/15 11:30 Copyright 2023 user. All rights reserved.
 */
class JavaObject(val java: Any, source: SourceHolder = NoSource) : AsahiObject(JavaClass.of(java::class.java), source)