package com.mattworzala.debug.client.render;

import com.mattworzala.debug.client.shape.Shape;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL32;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRenderer {

    private final Map<Identifier, Shape> shapes = new ConcurrentHashMap<>();

    private final DebugRenderContext context = new DebugRenderContext();

    public void add(@NotNull Identifier id, @NotNull Shape shape) {
        shapes.put(id, shape);
    }

    public void remove(@NotNull Identifier id) {
        shapes.remove(id);
    }

    public void remove(@NotNull String namespace) {
        shapes.keySet().removeIf(id -> id.getNamespace().equals(namespace));
    }

    public void clear() {
        shapes.clear();
    }

    public void render() {
        MinecraftClient.getInstance().getProfiler().push("debug_renderer");
        var oldShader = RenderSystem.getShader();
        try {
            // Setup
            RenderSystem.enableBlend();
//            RenderSystem.disableTexture();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.depthMask(true);
            // Polygon offset makes our geometry render over other geometry with the same depth
            // See: https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glPolygonOffset.xhtml
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(-1.0f, -1.0f);

            var cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
            context.init(cameraPos);

            // Rendering
            for (var shape : shapes.values()) {
                shape.render(context);
            }

            // Cleanup
            context.flush();
        } finally {
            RenderSystem.disablePolygonOffset();
            RenderSystem.depthFunc(GL32.GL_LEQUAL);
            RenderSystem.setShader(() -> oldShader);
//            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            MinecraftClient.getInstance().getProfiler().pop();
        }
    }

}
