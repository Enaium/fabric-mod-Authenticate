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

package cn.enaium.authenticate.mixin;

import cn.enaium.authenticate.event.Player;
import cn.enaium.authenticate.event.ServerPlayerCallbacks;
import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Enaium
 */
@Mixin(Commands.class)
public class CommandsMixin {
    @Inject(at = @At(value = "HEAD"), method = "performCommand", cancellable = true)
    public void onChatMessage(ParseResults<CommandSourceStack> command, String commandString, CallbackInfo ci) {
        final ServerPlayer player = command.getContext().getSource().getPlayer();
        if (player != null) {
            if (ServerPlayerCallbacks.ExecuteCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUUID(), player.connection.getRemoteAddress()), commandString)) {
                ci.cancel();
            }
        }
    }
}
