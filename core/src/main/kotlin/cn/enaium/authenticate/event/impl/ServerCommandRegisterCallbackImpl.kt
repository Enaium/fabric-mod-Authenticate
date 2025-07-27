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

import cn.enaium.authenticate.event.Player
import cn.enaium.authenticate.event.ServerCommandCallbacks
import cn.enaium.authenticate.utility.i18n
import cn.enaium.authenticate.utility.register
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
object ServerCommandRegisterCallbackImpl : ServerCommandCallbacks.RegisterCallback {
    override fun execute(player: Player, password: String, confirm: String, send: (String) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                if (password == confirm) {
                    register(player.uuid, password)
                    send(i18n("authenticate.command.register.success"))
                } else {
                    send(i18n("authenticate.command.register.password"))
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                send(i18n("authenticate.command.register.unsuccess"))
            }
        }
    }
}