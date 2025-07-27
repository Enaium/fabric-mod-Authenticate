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

import cn.enaium.authenticate.Authenticate.isAuthenticated
import cn.enaium.authenticate.event.Player
import cn.enaium.authenticate.event.ServerPlayerCallbacks
import cn.enaium.authenticate.utility.hasBanned
import cn.enaium.authenticate.utility.hasRegistered
import cn.enaium.authenticate.utility.i18n
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

/**
 * @author Enaium
 */
object ServerPlayerConnectCallbackImpl : ServerPlayerCallbacks.ConnectCallback {
    override fun connect(player: Player, callback: (send: String?, disconnect: String?) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            if (!hasRegistered(player.uuid)) {
                callback(i18n("authenticate.message.unregistered"), null)
            } else if (!isAuthenticated(player)) {
                callback(i18n("authenticate.message.unauthenticate"), null)
            }

            hasBanned(player.uuid)?.also {
                if (LocalDateTime.now().isBefore(it)) {
                    callback(
                        null,
                        i18n("authenticate.message.banned").format(Duration.between(LocalDateTime.now(), it).seconds)
                    )
                }
            }
        }
    }
}