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
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Enaium
 */
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends EntityMixin {
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Override
    public boolean isInvulnerable(boolean original) {
        return ServerPlayerCallbacks.InvulnerableCallback.Companion.getEVENT().getInvoker().value(new Player(uuid, networkHandler.getConnectionAddress()));
    }

    @Override
    public boolean isInvisible(boolean original) {
        return ServerPlayerCallbacks.InvisibleCallback.Companion.getEVENT().getInvoker().value(new Player(uuid, networkHandler.getConnectionAddress()));
    }
}
