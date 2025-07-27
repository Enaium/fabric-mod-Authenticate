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
import cn.enaium.authenticate.event.ServerPlayerCallbacks

/**
 * @author Enaium
 */
object ServerPlayerTakeItemsCallbackCallbackImpl : ServerPlayerCallbacks.TakeItemsCallback {
    override fun value(player: Player): Boolean {
        return Authenticate.isAuthenticated(player)
    }
}
