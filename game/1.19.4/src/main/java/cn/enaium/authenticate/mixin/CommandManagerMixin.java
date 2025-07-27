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
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Enaium
 */
@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(at = @At(value = "HEAD"), method = "execute", cancellable = true)
    public void onChatMessage(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfoReturnable<Integer> cir) {
        final ServerPlayerEntity player = parseResults.getContext().getSource().getPlayer();
        if (player != null) {
            if (ServerPlayerCallbacks.ExecuteCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnectionAddress()), command)) {
                cir.setReturnValue(0);
            }
        }
    }
}
