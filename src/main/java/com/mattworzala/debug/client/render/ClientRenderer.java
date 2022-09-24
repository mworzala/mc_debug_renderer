package com.mattworzala.debug.client.render;

import com.mattworzala.debug.client.render.shape.LineShape;
import com.mattworzala.debug.client.render.shape.OutlineBoxShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRenderer implements DebugRenderer.Renderer {
    private static ClientRenderer instance;

    public static ClientRenderer getInstance() {
        if (instance == null) {
            instance = new ClientRenderer();
        }
        return instance;
    }

    private final Map<Identifier, Shape> shapes = new ConcurrentHashMap<>();

    public void addShape(Identifier id, Shape shape) {
        shapes.put(id, shape);
    }

    public void removeShape(Identifier id) {
        shapes.remove(id);
    }

    // Remove all shapes in namespace
    public void removeShapes(String namespace) {
        shapes.entrySet().removeIf(entry -> entry.getKey().getNamespace().equals(namespace));
    }

    public void removeAllShapes() {
        shapes.clear();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        // Polygon offset makes our geometry render over other geometry with the same depth
        // See: https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glPolygonOffset.xhtml
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.0f, -1.0f);

        shapes.values().forEach(shape -> shape.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ));

//        new OutlineBoxShape(0, -60, 0, 3, -55, 5, 0x22FFFFFF, RenderLayer.INLINE, 0xFFFF6666, RenderLayer.INLINE, "12345678901234567890", 0xFFFF6666)
//                .render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
//        new OutlineBoxShape(0, -60, 0, 10, -55, 5, 0x22FFFFFF, RenderLayer.INLINE, 0xFFFF6666, RenderLayer.INLINE, "12345678901234567890", 0xFFFF6666)
//                .render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);

        RenderSystem.disablePolygonOffset();
    }

}
