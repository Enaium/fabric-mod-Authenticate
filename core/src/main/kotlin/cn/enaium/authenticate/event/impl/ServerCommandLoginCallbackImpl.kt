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

package cn.enaium.authenticate.event.impl

import cn.enaium.authenticate.Authenticate
import cn.enaium.authenticate.event.Player
import cn.enaium.authenticate.event.ServerCommandCallbacks
import cn.enaium.authenticate.utility.i18n
import cn.enaium.authenticate.utility.login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
object ServerCommandLoginCallbackImpl : ServerCommandCallbacks.LoginCallback {
    override fun execute(player: Player, password: String, send: (String) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            if (!Authenticate.isAuthenticated(player)) {
                try {
                    login(player.uuid, password)?.also {
                        Authenticate.login(player)
                        send(i18n("authenticate.command.login.success"))
                    } ?: send(i18n("authenticate.command.login.passwordIncorrect"))
                } catch (e: Throwable) {
                    e.printStackTrace()
                    send(i18n("authenticate.command.login.unsuccess"))
                }
            }
        }
    }
}