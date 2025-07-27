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
import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

/**
 * @author Enaium
 */
@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(at = @At(value = "HEAD"), method = "run", cancellable = true)
    public void onChatMessage(CommandSource sender, Command command, int permissionLevel, String label, Object[] args, CallbackInfo ci) {
        final Entity player = sender.getEntity();
        if (player instanceof ServerPlayerEntity) {
            final String join = String.join(" ", Arrays.stream(args).map(Object::toString).toArray(String[]::new));
            if (ServerPlayerCallbacks.ExecuteCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), ((ServerPlayerEntity) player).networkHandler.connection.getAddress()), String.format("%s %s", command.getCommandName(), join))) {
                ci.cancel();
            }
        }
    }
}
