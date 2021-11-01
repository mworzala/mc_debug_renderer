package com.mattworzala.debug.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public abstract class Shape {

    public void render(double cameraX, double cameraY, double cameraZ) {
        // Setup
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
//        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableTexture();

        render0(cameraX, cameraY, cameraZ);

        // Cleanup
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public abstract void render0(double cameraX, double cameraY, double cameraZ);





    public static class Box extends Shape {
        public final double x1;
        public final double y1;
        public final double z1;
        public final double x2;
        public final double y2;
        public final double z2;

        public final int argb;

        public Box(PacketByteBuf buffer) {
            x1 = buffer.readDouble();
            y1 = buffer.readDouble();
            z1 = buffer.readDouble();
            x2 = buffer.readDouble();
            y2 = buffer.readDouble();
            z2 = buffer.readDouble();
            argb = buffer.readInt();
        }

        public Box(double x1, double y1, double z1, double x2, double y2, double z2, int argb) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
            this.argb = argb;
        }

        public int r() {
            return (argb >> 16) & 0xFF;
        }

        public int g() {
            return (argb >> 8) & 0xFF;
        }

        public int b() {
            return argb & 0xFF;
        }

        public int a() {
            return (argb >> 24) & 0xFF;
        }

        @Override
        public void render0(double cameraX, double cameraY, double cameraZ) {
            var box = new net.minecraft.util.math.Box(x1, y1, z1, x2, y2, z2)
                    .offset(-cameraX, -cameraY, -cameraZ);
            DebugRenderer.drawBox(box, r() / 255f, g() / 255f, b() / 255f, a() / 255f);
        }
    }

    public static class Line extends Shape {
        public final List<Vec3d> points;
        public final float lineWidth;
        public final int argb;

        public Line(PacketByteBuf buffer) {
            points = buffer.readList(b -> new Vec3d(b.readDouble(), b.readDouble(), b.readDouble()));
            lineWidth = buffer.readFloat();
            argb = buffer.readInt();
        }

        public Line(List<Vec3d> points, float lineWidth, int argb) {
            this.points = points;
            this.lineWidth = lineWidth;
            this.argb = argb;
        }

        public int r() {
            return (argb >> 16) & 0xFF;
        }

        public int g() {
            return (argb >> 8) & 0xFF;
        }

        public int b() {
            return argb & 0xFF;
        }

        public int a() {
            return (argb >> 24) & 0xFF;
        }

        @Override
        public void render0(double cameraX, double cameraY, double cameraZ) {
            RenderSystem.defaultBlendFunc();
            RenderSystem.lineWidth(lineWidth);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            bufferBuilder.fixedColor(r(), g(), b(), a());

            for (Vec3d point : points) {
                bufferBuilder.vertex(point.x - cameraX, point.y - cameraY, point.z - cameraZ).next();
            }

            bufferBuilder.unfixColor();
            tessellator.draw();
        }
    }
}
