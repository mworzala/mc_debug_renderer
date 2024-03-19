package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public class DebugRenderContext {
    private final Matrix4f positionMat;
    private final Matrix3f normalMat;

    private RenderType renderType = null;
    private BufferBuilder builder = null;

    private float a = 1f, r = 1f, g = 1f, b = 1f;

    // Line state
    private boolean hasLast = false;
    private float lastX, lastY, lastZ;

    public DebugRenderContext(Matrix4f positionMat, Matrix3f normalMat) {
        this.positionMat = positionMat;
        this.normalMat = normalMat;
    }

    public void submit(@NotNull Consumer<DebugRenderContext> func, @NotNull RenderType renderType, @NotNull RenderLayer layer) {
        begin(renderType);
        func.accept(this);

        if (layer != RenderLayer.TOP) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.setShaderColor(1.0f, 1f, 1f, 1f);
        BufferRenderer.drawWithGlobalProgram(this.builder.end());

        if (layer == RenderLayer.MIXED) {
            begin(renderType);
            func.accept(this);
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.2f);
            BufferRenderer.drawWithGlobalProgram(this.builder.end());
        }

        RenderSystem.enableDepthTest();
    }

    public void begin(@NotNull RenderType renderType) {
        this.renderType = renderType;
        this.builder = Tessellator.getInstance().getBuffer();
        this.builder.begin(renderType.drawMode(), renderType.vertexFormat());
        RenderSystem.setShader(renderType.shader());

        if (renderType == RenderType.QUADS) {
            RenderSystem.enableCull();
        } else {
            RenderSystem.disableCull();
        }
    }

    public void color(int argb) {
        this.a = ((argb >> 24) & 0xFF) / 255f;
        this.r = ((argb >> 16) & 0xFF) / 255f;
        this.g = ((argb >> 8) & 0xFF) / 255f;
        this.b = (argb & 0xFF) / 255f;
    }

    public void vertex(@NotNull Vec3d point) {
        vertex((float) point.x, (float) point.y, (float) point.z);
    }

    public void vertex(float x, float y, float z) {
        if (renderType == RenderType.QUADS) {
            builder.vertex(positionMat, x, y, z)
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

            float dx = x - lastX;
            float dy = y - lastY;
            float dz = z - lastZ;
            float distanceInv = 1.0f / (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
            this.builder.vertex(positionMat, lastX, lastY, lastZ)
                    .color(r, g, b, a)
                    .normal(normalMat, dx *= distanceInv, dy *= distanceInv, dz *= distanceInv)
                    .next();
            this.builder.vertex(positionMat, x, y, z)
                    .color(r, g, b, a)
                    .normal(normalMat, dx, dy, dz)
                    .next();

            hasLast = false;
        } else {
            throw new IllegalStateException("Cannot render vertex with render type " + renderType);
        }
    }

}
