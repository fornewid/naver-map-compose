/*
 * Copyright 2024 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naver.maps.map.compose

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * The list of the immediate subclasses if this class is a sealed class, or an empty list otherwise.
 */
internal inline fun <reified T> sealedSubclasses(): List<T> {
    return T::class.sealedSubclasses.mapNotNull { it.objectInstance }
}

/**
 * Returns the public static fields of the class, or an empty list otherwise.
 */
internal val KClass<*>.publicStaticFields: List<Field>
    get() = runCatching {
        java.declaredFields.filter { field ->
            Modifier.isStatic(field.modifiers) && Modifier.isPublic(field.modifiers)
        }
    }.getOrDefault(emptyList())
