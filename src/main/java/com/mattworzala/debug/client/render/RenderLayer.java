package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

public enum RenderLayer {
    INLINE(() -> {}),
    TOP(RenderSystem::disableDepthTest),
    GHOST(() -> RenderSystem.depthMask(false));

    private final Runnable setup;
    RenderLayer(Runnable setup) { this.setup = setup; }

    public void setup() {
        setup.run();
    }
}
