package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Shape {

    public RenderLayer layer = RenderLayer.INLINE;
    public int argb = 0xFFFFFFFF;

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        // Setup
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableTexture();
        RenderSystem.enableDepthTest();

        if (layer == RenderLayer.INLINE) {
            int alpha = (argb >> 24) & 0xFF;
            if (alpha < 240) {
                RenderSystem.depthMask(false);
            }
        } else if (layer == RenderLayer.TOP) {
            RenderSystem.disableDepthTest();
        }

        render0(matrices, vertexConsumers, cameraX, cameraY, cameraZ);

        // Cleanup
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public abstract void render0(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ);

}
