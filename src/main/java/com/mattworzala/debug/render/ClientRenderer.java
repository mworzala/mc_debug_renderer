package com.mattworzala.debug.render;

import com.mattworzala.debug.shape.Shape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

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
        matrices.push();
        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Polygon offset makes our geometry render over other geometry with the same depth
        // See: https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glPolygonOffset.xhtml
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.0f, -1.0f);

        Matrix4f pose = matrices.peek().getPositionMatrix();
        Matrix3f normal = matrices.peek().getNormalMatrix();
        var context = new DebugRenderContext(pose, normal);

        var ordered = new ArrayList<>(shapes.values());
        ordered.sort((a, b) -> {
            var aDist = a.distanceTo(camera.getPos());
            var bDist = b.distanceTo(camera.getPos());
            return Double.compare(bDist, aDist);
        });
        for (var shape : ordered) {
            shape.render(context);
        }

        RenderSystem.disablePolygonOffset();
        RenderSystem.enableCull();

        matrices.pop();
    }

}
