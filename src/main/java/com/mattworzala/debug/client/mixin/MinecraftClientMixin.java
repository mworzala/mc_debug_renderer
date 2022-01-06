package com.mattworzala.debug.client.mixin;

import com.mattworzala.debug.client.render.ClientRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "joinWorld", at = @At("HEAD"))
    public void joinWorld(ClientWorld world, CallbackInfo ci) {
        ClientRenderer.getInstance().removeAllShapes();
    }
}
