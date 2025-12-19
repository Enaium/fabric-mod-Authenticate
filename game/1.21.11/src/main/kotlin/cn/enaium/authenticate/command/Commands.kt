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
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.EntityArgumentType.getPlayer
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.command.permission.Permission
import net.minecraft.command.permission.PermissionLevel
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

/**
 * @author Enaium
 */
fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
        literal("r").redirect(
            dispatcher.register(
                literal("register")
                    .then(
                        argument("password", string())
                            .then(
                                argument("confirm", string())
                                    .executes { context: CommandContext<ServerCommandSource> ->
                                        val uuid = context.source.player?.uuid
                                        val connectionAddress = context.source.player?.networkHandler?.connectionAddress
                                        ServerCommandCallbacks.RegisterCallback.EVENT.invoker.execute(
                                            Player(
                                                uuid ?: return@executes Command.SINGLE_SUCCESS,
                                                connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                                            ),
                                            getString(context, "password"),
                                            getString(context, "confirm"),
                                        ) {
                                            context.source.player?.sendMessage(Text.literal(it))
                                        }
                                        Command.SINGLE_SUCCESS
                                    })
                    )
            )
        )
    )
}

fun login(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
        literal("l").redirect(
            dispatcher.register(
                literal("login")
                    .then(
                        argument("password", string())
                            .executes { context: CommandContext<ServerCommandSource> ->
                                val uuid = context.source.player?.uuid
                                val connectionAddress = context.source.player?.networkHandler?.connectionAddress
                                ServerCommandCallbacks.LoginCallback.EVENT.invoker.execute(
                                    Player(
                                        uuid ?: return@executes Command.SINGLE_SUCCESS,
                                        connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                                    ),
                                    getString(context, "password"),
                                ) {
                                    context.source.player?.sendMessage(Text.literal(it))
                                }
                                Command.SINGLE_SUCCESS
                            }
                    )
            )
        )
    )
}

fun logout(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
        literal("lo").redirect(
            dispatcher.register(
                literal("logout")
                    .executes { context: CommandContext<ServerCommandSource> ->
                        val uuid = context.source.player?.uuid
                        val connectionAddress = context.source.player?.networkHandler?.connectionAddress
                        ServerCommandCallbacks.LogoutCallback.EVENT.invoker.execute(
                            Player(
                                uuid ?: return@executes Command.SINGLE_SUCCESS,
                                connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                            )
                        ) {
                            context.source.player?.sendMessage(Text.literal(it))
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
        )
    )
}

val ROOT: LiteralArgumentBuilder<ServerCommandSource> = literal("auth")
    .requires { source: ServerCommandSource -> source.permissions.hasPermission(Permission.Level(PermissionLevel.OWNERS)) }

fun ban(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
        ROOT.then(
            literal("ban")
                .then(
                    argument("player", player())?.also {
                        for (unit in TimeUnit.entries) {
                            it.then(
                                literal(unit.name).then(
                                    argument("amount", integer())
                                        .executes { context: CommandContext<ServerCommandSource> ->
                                            val player = getPlayer(context, "player")
                                            player == context.source.player && return@executes Command.SINGLE_SUCCESS
                                            ServerCommandCallbacks.BanCallback.EVENT.invoker.execute(
                                                Player(
                                                    player.uuid ?: return@executes Command.SINGLE_SUCCESS,
                                                    player.networkHandler.connectionAddress
                                                        ?: return@executes Command.SINGLE_SUCCESS
                                                ),
                                                unit,
                                                getInteger(context, "amount"),
                                            ) { send, disconnect ->
                                                context.source.player?.sendMessage(Text.literal(send))
                                                disconnect?.also {
                                                    player.networkHandler.disconnect(Text.literal(disconnect))
                                                }
                                            }
                                            Command.SINGLE_SUCCESS
                                        }
                                )
                            )
                        }
                    }
                )
        )
    )
}

fun initializer() {
    CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource>, registryAccess: CommandRegistryAccess, environment: CommandManager.RegistrationEnvironment ->
        register(dispatcher)
        login(dispatcher)
        logout(dispatcher)
        ban(dispatcher)
    })
}