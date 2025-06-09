package com.mattworzala.debug.render;

import com.mattworzala.debug.shape.Shape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import me.x150.renderer.render.WorldRenderContext;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRenderer {

    private final Map<Identifier, Shape> shapes = new ConcurrentHashMap<>();

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

    public void render(MatrixStack matrices, Camera camera) {
        VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(new BufferAllocator(16 * 1024));
        WorldRenderContext context = new WorldRenderContext(MinecraftClient.getInstance(), vcp);

        matrices.push();
        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        var ordered = new ArrayList<>(shapes.values());
        ordered.sort((a, b) -> {
            var aDist = a.distanceTo(camera.getPos());
            var bDist = b.distanceTo(camera.getPos());
            return Double.compare(bDist, aDist);
        });

        for (var shape : ordered) {
            
            shape.render(matrices, context, vcp);
        }

        matrices.pop();
        vcp.draw();
    }
}