package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class DebugRenderContext {
    private RenderType renderType = null;
    private BufferBuilder builder = null;

    private Vec3d cameraPos;

    private int a = 0, r = 0, g = 0, b = 0;

    // Line state
    private boolean hasLast = false;
    private double lastX, lastY, lastZ;


    public void begin(@NotNull RenderType renderType) {
        if (this.renderType != null) {
            flush();
        }

        if (renderType == RenderType.QUADS) {
            RenderSystem.enableCull();
        } else {
            RenderSystem.disableCull();
        }

        this.renderType = renderType;
        this.builder = Tessellator.getInstance().getBuffer();
        this.builder.begin(renderType.drawMode(), renderType.vertexFormat());
    }

    public void end() {
        //todo probably can delete this, though i wouldnt mind min sanity check here
    }

    public void layer(@NotNull RenderLayer layer) {
        if (layer == RenderLayer.TOP) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }
    }

    public void color(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
    }

    public void vertex(@NotNull Vec3d point) {
        vertex(point.x, point.y, point.z);
    }

    public void vertex(double x, double y, double z) {
        x -= cameraPos.x;
        y -= cameraPos.y;
        z -= cameraPos.z;

        if (renderType == RenderType.QUADS) {
            builder.vertex(x, y, z)
                    .color(r, g, b, a)
                    .next();
        } else if (renderType == RenderType.LINES) {
            if (!hasLast) {
                lastX = x;
                lastY = y;
                lastZ = z;
                hasLast = true;
                return;
            }

            var normal = computeNormal(lastX, lastY, lastZ, x, y, z);

            builder.vertex(lastX, lastY, lastZ)
                    .color(r, g, b, a)
                    .normal((float) normal.x, (float) normal.y, (float) normal.z)
                    .next();

            builder.vertex(x, y, z)
                    .color(r, g, b, a)
                    .normal((float) normal.x, (float) normal.y, (float) normal.z)
                    .next();

            hasLast = false;
        } else {
            throw new IllegalStateException("Cannot render vertex with render type " + renderType);
        }
    }


    // Internal details

    void init(@NotNull Vec3d cameraPos) {
        this.cameraPos = cameraPos;
    }

    void flush() {
        if (renderType == null) {
            return;
        }
        try {
            RenderSystem.setShader(renderType.shader());
            Tessellator.getInstance().draw();
        } finally {
            renderType = null;
            builder = null;
        }
    }

    private Vec3d computeNormal(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return new Vec3d(dx / length, dy / length, dz / length);
    }

}
