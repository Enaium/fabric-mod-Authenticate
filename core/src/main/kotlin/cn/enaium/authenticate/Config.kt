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

package cn.enaium.authenticate

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.IOException
import java.util.*
import kotlin.io.path.*

/**
 * @author Enaium
 */
object Config {
    private val configFile = Path(System.getProperty("user.dir")).resolve("config").resolve("Authenticate.json")
    var model = Model()
        private set

    fun load() {
        if (configFile.exists()) {
            try {
                model =
                    Gson().fromJson(configFile.readText(Charsets.UTF_8), Model::class.java)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            save()
        }
    }

    fun save() {
        try {
            configFile.createParentDirectories()
            configFile.writeText(GsonBuilder().setPrettyPrinting().create().toJson(model), Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reset() {
        model = Model()
        save()
    }

    data class Model(
        val lang: String = "en_us",
        val jdbcUrl: String = "jdbc:h2:file:./db/authenticate",
        val username: String? = null,
        val password: String? = null,
        val salt: String = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().toByteArray()),
        val ddlStatement: String = """
            create table if not exists player_v0
            (
                id uuid primary key not null,
                created_time timestamp not null,
                modified_time timestamp not null,
                uuid uuid unique not null,
                password varchar(255) not null,
                banned timestamp
            );
        """.trimIndent(),
        val authExpire: Long = 60000,
    )
}
