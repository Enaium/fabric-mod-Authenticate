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

package cn.enaium.authenticate.command

import cn.enaium.authenticate.event.Player
import cn.enaium.authenticate.event.ServerCommandCallbacks
import cn.enaium.authenticate.event.ServerCommandCallbacks.BanCallback.TimeUnit
import net.legacyfabric.fabric.api.registry.CommandRegistry
import net.minecraft.command.AbstractCommand
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos

/**
 * @author Enaium
 */
fun register(): AbstractCommand {
    return object : AbstractCommand() {
        override fun getCommandName(): String {
            return "register"
        }

        override fun getAliases(): List<String> {
            return listOf("r")
        }

        override fun getUsageTranslationKey(source: CommandSource): String? {
            return null
        }

        override fun method_3279(
            minecraftServer: MinecraftServer,
            commandSource: CommandSource,
            args: Array<String>
        ) {
            args.size < 2 && return
            val entity = commandSource.entity
            if (entity is ServerPlayerEntity) {
                ServerCommandCallbacks.RegisterCallback.EVENT.invoker.execute(
                    Player(
                        entity.uuid,
                        entity.networkHandler.connection.address
                    ), args[0].toString(), args[1].toString()
                ) {
                    commandSource.entity?.sendMessage(LiteralText(it))
                }
            }
        }

        override fun getPermissionLevel(): Int {
            return 0
        }
    }
}

fun login(): AbstractCommand {
    return object : AbstractCommand() {
        override fun getCommandName(): String {
            return "login"
        }

        override fun getAliases(): List<String> {
            return listOf("l")
        }

        override fun getUsageTranslationKey(source: CommandSource): String? {
            return null
        }

        override fun method_3279(
            minecraftServer: MinecraftServer,
            commandSource: CommandSource,
            args: Array<String>
        ) {
            args.isEmpty() && return
            val entity = commandSource.entity
            if (entity is ServerPlayerEntity) {
                ServerCommandCallbacks.LoginCallback.EVENT.invoker.execute(
                    Player(
                        entity.uuid,
                        entity.networkHandler.connection.address
                    ), args[0].toString()
                ) {
                    entity.sendMessage(LiteralText(it))
                }
            }
        }

        override fun getPermissionLevel(): Int {
            return 0
        }
    }
}

fun logout(): AbstractCommand {
    return object : AbstractCommand() {
        override fun getCommandName(): String {
            return "logout"
        }

        override fun getAliases(): List<String> {
            return listOf("lo")
        }

        override fun getUsageTranslationKey(source: CommandSource): String? {
            return null
        }

        override fun method_3279(
            minecraftServer: MinecraftServer,
            commandSource: CommandSource,
            args: Array<String>
        ) {
            val entity = commandSource.entity
            if (entity is ServerPlayerEntity) {
                ServerCommandCallbacks.LogoutCallback.EVENT.invoker.execute(
                    Player(
                        entity.uuid,
                        entity.networkHandler.connection.address
                    )
                ) {
                    commandSource.entity?.sendMessage(LiteralText(it))
                }
            }
        }
    }
}

fun auth(): AbstractCommand {
    return object : AbstractCommand() {
        override fun getCommandName(): String {
            return "auth"
        }

        override fun getUsageTranslationKey(source: CommandSource): String? {
            return null
        }

        override fun method_3279(
            minecraftServer: MinecraftServer,
            commandSource: CommandSource,
            args: Array<String>
        ) {
            args.size < 3 && return
            if (args[0].equals("ban", ignoreCase = true)
                && args[2].lowercase() in TimeUnit.entries.map { it.name.lowercase() }
            ) {
                minecraftServer.playerManager.getPlayer(args[1])?.also { player ->
                    player == commandSource.entity && return
                    ServerCommandCallbacks.BanCallback.EVENT.invoker.execute(
                        Player(player.uuid, player.networkHandler.connection.address),
                        TimeUnit.valueOf(args[2]),
                        args[3].toInt()
                    ) { send, disconnect ->
                        commandSource.entity?.sendMessage(LiteralText(send))
                        disconnect?.also {
                            player.networkHandler.disconnect(disconnect)
                        }
                    }
                }
            }
        }

        override fun method_10738(
            server: MinecraftServer,
            source: CommandSource,
            strings: Array<String>,
            pos: BlockPos?
        ): List<String> {
            if (strings.size == 1) {
                return listOf("ban")
            }

            if (strings.size == 2) {
                if (strings[0].equals("ban", ignoreCase = true)) {
                    return server.playerManager.playerNames.toList()
                }
            }

            if (strings.size == 3) {
                if (strings[0].equals("ban", ignoreCase = true)) {
                    return TimeUnit.entries.map { it.name }
                }
            }
            return emptyList()
        }

        override fun getPermissionLevel(): Int {
            return 4
        }
    }
}

fun initializer() {
    CommandRegistry.INSTANCE.register(register())
    CommandRegistry.INSTANCE.register(login())
    CommandRegistry.INSTANCE.register(logout())
    CommandRegistry.INSTANCE.register(auth())
}