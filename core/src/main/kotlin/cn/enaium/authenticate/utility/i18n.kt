/*
 * Copyright 2025 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enaium.authenticate.utility

import cn.enaium.authenticate.Config
import com.google.gson.Gson
import com.google.gson.JsonObject


/**
 * @author Enaium
 */
fun i18n(key: String): String {
    val lang: String? = Config.model.lang
    try {
        var url = object {}::class.java.getResource("/lang/$lang.json")
        if (url == null) {
            url = object {}::class.java.getResource("/lang/en_us.json")
        }

        if (url == null) {
            return key
        }

        val text = url.readText()
        return Gson().fromJson(text, JsonObject::class.java).get(key).asString
    } catch (_: Throwable) {
    }
    return key
}