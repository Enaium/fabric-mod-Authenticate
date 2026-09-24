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
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument.getPlayer
import net.minecraft.commands.arguments.EntityArgument.player
import net.minecraft.network.chat.Component

/**
 * @author Enaium
 */
fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
    dispatcher.register(
        literal("r").redirect(
            dispatcher.register(
                literal("register")
                    .then(
                        argument("password", string())
                            .then(
                                argument("confirm", string())
                                    .executes { context: CommandContext<CommandSourceStack> ->
                                        val uuid = context.source.player?.uuid
                                        val connectionAddress = context.source.player?.connection?.remoteAddress
                                        ServerCommandCallbacks.RegisterCallback.EVENT.invoker.execute(
                                            Player(
                                                uuid ?: return@executes Command.SINGLE_SUCCESS,
                                                connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                                            ),
                                            getString(context, "password"),
                                            getString(context, "confirm"),
                                        ) {
                                            context.source.player?.sendSystemMessage(Component.literal(it))
                                        }
                                        Command.SINGLE_SUCCESS
                                    })
                    )
            )
        )
    )
}

fun login(dispatcher: CommandDispatcher<CommandSourceStack>) {
    dispatcher.register(
        literal("l").redirect(
            dispatcher.register(
                literal("login")
                    .then(
                        argument("password", string())
                            .executes { context: CommandContext<CommandSourceStack> ->
                                val uuid = context.source.player?.uuid
                                val connectionAddress = context.source.player?.connection?.remoteAddress
                                ServerCommandCallbacks.LoginCallback.EVENT.invoker.execute(
                                    Player(
                                        uuid ?: return@executes Command.SINGLE_SUCCESS,
                                        connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                                    ),
                                    getString(context, "password"),
                                ) {
                                    context.source.player?.sendSystemMessage(Component.literal(it))
                                }
                                Command.SINGLE_SUCCESS
                            }
                    )
            )
        )
    )
}

fun logout(dispatcher: CommandDispatcher<CommandSourceStack>) {
    dispatcher.register(
        literal("lo").redirect(
            dispatcher.register(
                literal("logout")
                    .executes { context: CommandContext<CommandSourceStack> ->
                        val uuid = context.source.player?.uuid
                        val connectionAddress = context.source.player?.connection?.remoteAddress
                        ServerCommandCallbacks.LogoutCallback.EVENT.invoker.execute(
                            Player(
                                uuid ?: return@executes Command.SINGLE_SUCCESS,
                                connectionAddress ?: return@executes Command.SINGLE_SUCCESS
                            )
                        ) {
                            context.source.player?.sendSystemMessage(Component.literal(it))
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
        )
    )
}

val ROOT: LiteralArgumentBuilder<CommandSourceStack> = literal("auth")
    .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))

fun ban(dispatcher: CommandDispatcher<CommandSourceStack>) {
    dispatcher.register(
        ROOT.then(
            literal("ban")
                .then(
                    argument("player", player()).also {
                        for (unit in TimeUnit.entries) {
                            it.then(
                                literal(unit.name).then(
                                    argument("amount", integer())
                                        .executes { context: CommandContext<CommandSourceStack> ->
                                            val player = getPlayer(context, "player")
                                            player == context.source.player && return@executes Command.SINGLE_SUCCESS
                                            ServerCommandCallbacks.BanCallback.EVENT.invoker.execute(
                                                Player(
                                                    player.uuid,
                                                    player.connection.remoteAddress
                                                ),
                                                unit,
                                                getInteger(context, "amount"),
                                            ) { send, disconnect ->
                                                context.source.player?.sendSystemMessage(Component.literal(send))
                                                disconnect?.also {
                                                    player.connection.disconnect(Component.literal(disconnect))
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
    CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<CommandSourceStack>, buildContext: CommandBuildContext, selection: Commands.CommandSelection ->
        register(dispatcher)
        login(dispatcher)
        logout(dispatcher)
        ban(dispatcher)
    })
}