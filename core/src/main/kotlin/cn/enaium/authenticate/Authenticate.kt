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

import cn.enaium.authenticate.event.Player
import cn.enaium.authenticate.event.ServerCommandCallbacks
import cn.enaium.authenticate.event.ServerPlayerCallbacks
import cn.enaium.authenticate.event.impl.*
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

/**
 * @author Enaium
 */
object Authenticate {

    private val auth =
        CacheBuilder<String, Player>.newBuilder().expireAfterWrite(Config.model.authExpire, TimeUnit.MILLISECONDS)
            .build<String, Player>()

    fun login(player: Player) {
        auth.put(player.key, player)
    }

    fun logout(player: Player) {
        auth.invalidate(player.key)
    }

    fun isAuthenticated(player: Player): Boolean {
        val authed = auth.getIfPresent(player.key) != null
        if (authed) {
            refresh(player)
        }
        return authed
    }

    fun refresh(player: Player) {
        login(player)
    }

    @JvmStatic
    fun initializer() {
        println("Hello Authenticate world!")
        Config.load()
        Runtime.getRuntime().addShutdownHook(Thread { Config.save() })
        ServerPlayerCallbacks.ConnectCallback.EVENT.register(ServerPlayerConnectCallbackImpl)
        ServerPlayerCallbacks.ChatMessageCallback.EVENT.register(ServerPlayerChatMessageCallbackImpl)
        ServerPlayerCallbacks.ActionCallback.EVENT.register(ServerPlayerActionCallbackImpl)
        ServerPlayerCallbacks.MoveCallback.EVENT.register(ServerPlayerMoveCallbackImpl)
        ServerPlayerCallbacks.InteractBlockCallback.EVENT.register(ServerPlayerInteractBlockCallbackImpl)
        ServerPlayerCallbacks.InteractItemCallback.EVENT.register(ServerPlayerInteractItemCallbackImpl)
        ServerPlayerCallbacks.InteractEntityCallback.EVENT.register(ServerPlayerInteractEntityCallbackImpl)
        ServerPlayerCallbacks.ExecuteCallback.EVENT.register(ServerPlayerExecuteCallbackImpl)
        ServerPlayerCallbacks.TakeItemsCallback.EVENT.register(ServerPlayerTakeItemsCallbackCallbackImpl)
        ServerPlayerCallbacks.InvisibleCallback.EVENT.register(ServerPlayerInvisibleCallbackImpl)
        ServerPlayerCallbacks.InvulnerableCallback.EVENT.register(ServerPlayerInvulnerableCallbackImpl)
        ServerCommandCallbacks.RegisterCallback.EVENT.register(ServerCommandRegisterCallbackImpl)
        ServerCommandCallbacks.LoginCallback.EVENT.register(ServerCommandLoginCallbackImpl)
        ServerCommandCallbacks.LogoutCallback.EVENT.register(ServerCommandLogoutCallbackImpl)
        ServerCommandCallbacks.BanCallback.EVENT.register(ServerCommandBanCallbackImpl)
    }
}