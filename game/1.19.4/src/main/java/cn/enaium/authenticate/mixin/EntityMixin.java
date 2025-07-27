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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

/**
 * @author Enaium
 */
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    protected UUID uuid;

    @ModifyReturnValue(at = @At("RETURN"), method = "isInvisible")
    public boolean isInvisible(boolean original) {
        return original;
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isInvulnerable")
    public boolean isInvulnerable(boolean original) {
        return original;
    }
}
