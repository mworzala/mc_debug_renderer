package com.mattworzala.debug.client.render.shape;

import com.mattworzala.debug.client.render.DebugRenderLayer;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.Shape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class LineShape extends Shape {
    public final List<Vec3d> points;
    public final float lineWidth;

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
    public void render0(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        //todo renderlayer top is not supported currently for better lines, not sure how to force them on top.
        if (layer == RenderLayer.INLINE) {
            matrices.push();
            matrices.translate(-cameraX, -cameraY, -cameraZ);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(DebugRenderLayer.LINES);

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            Matrix3f matrix3f = matrices.peek().getNormalMatrix();

//            for (var point : points) {
//                var normal = point.normalize();
//                vertexConsumer.vertex(matrix4f, (float) point.x, (float) point.y, (float) point.z)
//                        .color(r() / 255f, g() / 255f, b() / 255f, a() / 255f)
//                        .normal(matrix3f, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
//
//            }
//
            for (int i = 0; i < points.size() - 1; i++) {
                // Dunno about these normals, but they seem to work.
                var point1 = points.get(i);
                var normal = point1.normalize();
                vertexConsumer.vertex(matrix4f, (float) point1.x, (float) point1.y, (float) point1.z)
                        .color(r() / 255f, g() / 255f, b() / 255f, a() / 255f)
                        .normal(matrix3f, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
                var point2 = points.get(i + 1);
                normal = (point2.subtract(point1)).normalize();
                vertexConsumer.vertex(matrix4f, (float) point2.x, (float) point2.y, (float) point2.z)
                        .color(r() / 255f, g() / 255f, b() / 255f, a() / 255f)
                        .normal(matrix3f, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
            }

            matrices.pop();
        } else {
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
