package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.systems.RenderSystem;

public enum RenderLayer {
    INLINE(RenderSystem::enableDepthTest),
    TOP(RenderSystem::disableDepthTest);

    private final Runnable setup;
    RenderLayer(Runnable setup) { this.setup = setup; }

    public void setup() {
        setup.run();
    }
}
