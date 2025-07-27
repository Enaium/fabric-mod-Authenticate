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
import cn.enaium.authenticate.SimpleDataSource
import cn.enaium.authenticate.entity.*
import cn.enaium.authenticate.event.ServerCommandCallbacks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.dialect.*
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.exists
import org.babyfish.jimmer.sql.kt.newKSqlClient
import org.babyfish.jimmer.sql.runtime.ConnectionManager
import org.babyfish.jimmer.sql.runtime.DefaultDatabaseNamingStrategy
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
fun getDialectFromJdbcUrl(jdbcUrl: String): Dialect {
    return when {
        jdbcUrl.startsWith("jdbc:h2:") -> H2Dialect()
        jdbcUrl.startsWith("jdbc:mysql:") -> MySqlDialect()
        jdbcUrl.startsWith("jdbc:postgresql:") -> PostgresDialect()
        jdbcUrl.startsWith("jdbc:oracle:") -> OracleDialect()
        jdbcUrl.startsWith("jdbc:sqlite:") -> SQLiteDialect()
        else -> H2Dialect()
    }
}

fun sql(): KSqlClient {
    return newKSqlClient {
        val datasource = SimpleDataSource(Config.model.jdbcUrl, Config.model.username, Config.model.password)

        datasource.connection.use {
            it.createStatement().use { statement ->
                Config.model.ddlStatement.split(";")
                    .forEach { line ->
                        line.isBlank() && return@forEach
                        statement.execute(line.trimIndent())
                    }
            }
        }

        setConnectionManager(ConnectionManager.simpleConnectionManager(datasource))
        setDatabaseNamingStrategy(DefaultDatabaseNamingStrategy.LOWER_CASE)
        setDialect(getDialectFromJdbcUrl(Config.model.jdbcUrl))
        addDraftInterceptor(BaseEntityDraftInterceptor())
    }
}

suspend fun hasRegistered(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
    return@withContext sql().exists(Player::class) {
        where(table.uuid eq uuid)
    }
}

suspend fun hasBanned(uuid: UUID): LocalDateTime? = withContext(Dispatchers.IO) {
    return@withContext sql().createQuery(Player::class) {
        where(table.uuid eq uuid)
        select(table.banned)
    }.fetchOneOrNull()
}

suspend fun register(uuid: UUID, password: String) = withContext(Dispatchers.IO) {
    sql().save(new(Player::class).by {
        this.uuid = uuid
        this.password = "$password:${uuid}:${Config.model.salt}".md5()
    })
}

suspend fun login(uuid: UUID, password: String): Player? = withContext(Dispatchers.IO) {
    return@withContext sql().createQuery(Player::class) {
        where(table.uuid eq uuid)
        where(table.password eq "$password:${uuid}:${Config.model.salt}".md5())
        select(table)
    }.fetchOneOrNull()
}

suspend fun ban(uuid: UUID, unit: ServerCommandCallbacks.BanCallback.TimeUnit, amount: Long) =
    withContext(Dispatchers.IO) {
        sql().save(new(Player::class).by {
            this.uuid = uuid
            this.banned = LocalDateTime.now().let {
                when (unit) {
                    ServerCommandCallbacks.BanCallback.TimeUnit.MINUTES -> it.plusMinutes(amount)
                    ServerCommandCallbacks.BanCallback.TimeUnit.HOURS -> it.plusHours(amount)
                    ServerCommandCallbacks.BanCallback.TimeUnit.DAYS -> it.plusDays(amount)
                    ServerCommandCallbacks.BanCallback.TimeUnit.WEEKS -> it.plusWeeks(amount)
                    ServerCommandCallbacks.BanCallback.TimeUnit.MONTHS -> it.plusMonths(amount)
                    ServerCommandCallbacks.BanCallback.TimeUnit.YEARS -> it.plusYears(amount)
                }
            }
        })
    }