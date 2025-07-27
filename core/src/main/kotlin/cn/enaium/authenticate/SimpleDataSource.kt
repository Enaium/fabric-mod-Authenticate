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

import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * @author Enaium
 */
class SimpleDataSource(
    val url: String,
    val username: String? = null,
    val password: String? = null,
) : DataSource {
    override fun getConnection(): Connection {
        return DriverManager.getConnection(url, username, password)
    }

    override fun getConnection(username: String?, password: String?): Connection? {
        return DriverManager.getConnection(url, username, password)
    }

    override fun getLogWriter(): PrintWriter {
        TODO("Not yet implemented")
    }

    override fun setLogWriter(out: PrintWriter?) {
        TODO("Not yet implemented")
    }

    override fun setLoginTimeout(seconds: Int) {
        TODO("Not yet implemented")
    }

    override fun getLoginTimeout(): Int {
        TODO("Not yet implemented")
    }

    override fun getParentLogger(): Logger? {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> unwrap(iface: Class<T?>?): T? {
        TODO("Not yet implemented")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        TODO("Not yet implemented")
    }
}