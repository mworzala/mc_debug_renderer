package com.mattworzala.debug.client.render.shape;

import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.Shape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class LineShape extends Shape {
    public final List<Vec3d> points;
    public final float lineWidth;
    public final int argb;

    public LineShape(PacketByteBuf buffer) {
        points = buffer.readList(b -> new Vec3d(b.readDouble(), b.readDouble(), b.readDouble()));
        lineWidth = buffer.readFloat();
        argb = buffer.readInt();
        layer = com.mattworzala.debug.client.render.RenderLayer.values()[buffer.readVarInt()];
    }

    public LineShape(List<Vec3d> points, float lineWidth, int argb, RenderLayer layer) {
        this.points = points;
        this.lineWidth = lineWidth;
        this.argb = argb;
        this.layer = layer;
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
