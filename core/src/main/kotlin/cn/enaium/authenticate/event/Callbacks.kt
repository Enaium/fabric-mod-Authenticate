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

package cn.enaium.authenticate.event

import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.*

/**
 * @author Enaium
 */
class ServerPlayerCallbacks {
    fun interface ConnectCallback {
        companion object {
            val EVENT = Event<ConnectCallback> { listeners: List<ConnectCallback> ->
                ConnectCallback { player, send ->
                    for (listener in listeners) {
                        listener.connect(player, send)
                    }
                }
            }
        }

        fun connect(player: Player, callback: (send: String?, disconnect: String?) -> Unit)
    }

    fun interface ChatMessageCallback {
        companion object {
            val EVENT =
                Event<ChatMessageCallback> { listeners: List<ChatMessageCallback> ->
                    ChatMessageCallback { player, chat ->
                        for (listener in listeners) {
                            val cancel = listener.cancel(player, chat)
                            if (cancel) {
                                return@ChatMessageCallback true
                            }
                        }
                        return@ChatMessageCallback false
                    }
                }
        }

        fun cancel(player: Player, chat: String): Boolean
    }

    fun interface ActionCallback {
        companion object {
            val EVENT =
                Event<ActionCallback> { listeners: List<ActionCallback> ->
                    ActionCallback {
                        for (listener in listeners) {
                            val cancel = listener.cancel(it)
                            if (cancel) {
                                return@ActionCallback true
                            }
                        }
                        return@ActionCallback false
                    }
                }
        }

        fun cancel(player: Player): Boolean
    }

    fun interface MoveCallback {
        companion object {
            val EVENT =
                Event<MoveCallback> { listeners: List<MoveCallback> ->
                    MoveCallback {
                        for (listener in listeners) {
                            val cancel = listener.cancel(it)
                            if (cancel) {
                                return@MoveCallback true
                            }
                        }
                        return@MoveCallback false
                    }
                }
        }

        fun cancel(player: Player): Boolean
    }

    fun interface InteractBlockCallback {
        companion object {
            val EVENT =
                Event<InteractBlockCallback> { listeners: List<InteractBlockCallback> ->
                    InteractBlockCallback {
                        for (listener in listeners) {
                            val cancel = listener.cancel(it)
                            if (cancel) {
                                return@InteractBlockCallback true
                            }
                        }
                        return@InteractBlockCallback false
                    }
                }
        }

        fun cancel(player: Player): Boolean
    }

    fun interface InteractItemCallback {
        companion object {
            val EVENT =
                Event<InteractItemCallback> { listeners: List<InteractItemCallback> ->
                    InteractItemCallback {
                        for (listener in listeners) {
                            val cancel = listener.cancel(it)
                            if (cancel) {
                                return@InteractItemCallback true
                            }
                        }
                        return@InteractItemCallback false
                    }
                }
        }

        fun cancel(player: Player): Boolean
    }

    fun interface InteractEntityCallback {
        companion object {
            val EVENT =
                Event<InteractEntityCallback> { listeners: List<InteractEntityCallback> ->
                    InteractEntityCallback {
                        for (listener in listeners) {
                            val cancel = listener.cancel(it)
                            if (cancel) {
                                return@InteractEntityCallback true
                            }
                        }
                        return@InteractEntityCallback false
                    }
                }
        }

        fun cancel(player: Player): Boolean
    }

    fun interface ExecuteCallback {
        companion object {
            val EVENT =
                Event<ExecuteCallback> { listeners: List<ExecuteCallback> ->
                    ExecuteCallback { player, command ->
                        for (listener in listeners) {
                            val cancel = listener.cancel(player, command)
                            if (cancel) {
                                return@ExecuteCallback true
                            }
                        }
                        return@ExecuteCallback false
                    }
                }
        }

        fun cancel(player: Player, command: String): Boolean
    }

    fun interface TakeItemsCallback {
        companion object {
            val EVENT =
                Event<TakeItemsCallback> { listeners: List<TakeItemsCallback> ->
                    TakeItemsCallback {
                        for (listener in listeners) {
                            val cancel = listener.value(it)
                            if (cancel) {
                                return@TakeItemsCallback true
                            }
                        }
                        return@TakeItemsCallback false
                    }
                }
        }

        fun value(player: Player): Boolean
    }

    fun interface InvisibleCallback {
        companion object {
            val EVENT =
                Event<InvisibleCallback> { listeners: List<InvisibleCallback> ->
                    InvisibleCallback {
                        for (listener in listeners) {
                            val cancel = listener.value(it)
                            if (cancel) {
                                return@InvisibleCallback true
                            }
                        }
                        return@InvisibleCallback false
                    }
                }
        }

        fun value(player: Player): Boolean
    }

    fun interface InvulnerableCallback {
        companion object {
            val EVENT =
                Event<InvulnerableCallback> { listeners: List<InvulnerableCallback> ->
                    InvulnerableCallback {
                        for (listener in listeners) {
                            val cancel = listener.value(it)
                            if (cancel) {
                                return@InvulnerableCallback true
                            }
                        }
                        return@InvulnerableCallback false
                    }
                }
        }

        fun value(player: Player): Boolean
    }
}

class ServerCommandCallbacks {
    fun interface RegisterCallback {
        companion object {
            val EVENT =
                Event<RegisterCallback> { listeners: List<RegisterCallback> ->
                    RegisterCallback { player, password, confirm, send ->
                        for (listener in listeners) {
                            listener.execute(player, password, confirm, send)
                        }
                    }
                }
        }

        fun execute(player: Player, password: String, confirm: String, send: (String) -> Unit)
    }

    fun interface LoginCallback {
        companion object {
            val EVENT =
                Event<LoginCallback> { listeners: List<LoginCallback> ->
                    LoginCallback { player, password, send ->
                        for (listener in listeners) {
                            listener.execute(player, password, send)
                        }
                    }
                }
        }

        fun execute(player: Player, password: String, send: (String) -> Unit)
    }

    fun interface LogoutCallback {
        companion object {
            val EVENT =
                Event<LogoutCallback> { listeners: List<LogoutCallback> ->
                    LogoutCallback { player, send ->
                        for (listener in listeners) {
                            listener.execute(player, send)
                        }
                    }
                }
        }

        fun execute(player: Player, send: (String) -> Unit)
    }

    fun interface BanCallback {
        companion object {
            val EVENT =
                Event<BanCallback> { listeners: List<BanCallback> ->
                    BanCallback { player, unit, amount, callback ->
                        for (listener in listeners) {
                            listener.execute(player, unit, amount, callback)
                        }
                    }
                }
        }

        enum class TimeUnit {
            MINUTES,
            HOURS,
            DAYS,
            WEEKS,
            MONTHS,
            YEARS
        }

        fun execute(
            player: Player,
            unit: TimeUnit,
            amount: Int,
            callback: (send: String, disconnect: String?) -> Unit
        )
    }
}

data class Player(val uuid: UUID, val address: SocketAddress) {
    val key = "${uuid}:${(address as? InetSocketAddress)?.hostString}"
}