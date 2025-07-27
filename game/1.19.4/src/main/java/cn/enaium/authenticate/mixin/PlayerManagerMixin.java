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
import kotlin.Unit;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Enaium
 */
@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ServerPlayerCallbacks.ConnectCallback.Companion.getEVENT().getInvoker().connect(new Player(player.getUuid(), player.networkHandler.getConnectionAddress()), (send, disconnect) -> {
            if (send != null) {
                player.sendMessage(Text.literal(send));
            }

            if (disconnect != null) {
                player.networkHandler.disconnect(Text.literal(disconnect));
            }
            return Unit.INSTANCE;
        });
    }
}
