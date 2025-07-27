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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Enaium
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At(value = "HEAD"), method = "onChatMessage", cancellable = true)
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (ServerPlayerCallbacks.ChatMessageCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()), packet.getChatMessage())) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "onPlayerAction", cancellable = true)
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        boolean cancel = ServerPlayerCallbacks.ActionCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()));
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "onPlayerMove", cancellable = true)
    public void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        boolean cancel = ServerPlayerCallbacks.MoveCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()));
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerInteractBlock", cancellable = true)
    public void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
        boolean cancel = ServerPlayerCallbacks.InteractBlockCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()));
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onHandSwing", cancellable = true)
    public void onPlayerInteractItem(HandSwingC2SPacket packet, CallbackInfo ci) {
        boolean cancel = ServerPlayerCallbacks.InteractItemCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()));
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerInteractEntity", cancellable = true)
    public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        boolean cancel = ServerPlayerCallbacks.InteractEntityCallback.Companion.getEVENT().getInvoker().cancel(new Player(player.getUuid(), player.networkHandler.getConnection().getAddress()));
        if (cancel) {
            ci.cancel();
        }
    }
}
