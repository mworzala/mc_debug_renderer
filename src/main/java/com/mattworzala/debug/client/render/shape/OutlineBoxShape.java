package com.mattworzala.debug.client.render.shape;

import com.mattworzala.debug.client.render.DebugRenderLayer;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.Shape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.*;

public class OutlineBoxShape extends Shape {
    public final double x1;
    public final double y1;
    public final double z1;
    public final double x2;
    public final double y2;
    public final double z2;

    public final int argbLine;
    public final RenderLayer layerLine;
    public final String text;
    public final int argbText;

    public OutlineBoxShape(double x1, double y1, double z1, double x2, double y2, double z2, int argb, RenderLayer layer, int argbLine, RenderLayer layerLine, String text, int argbText) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.argb = argb;
        this.layer = layer;
        this.argbLine = argbLine;
        this.layerLine = layerLine;
        this.text = text;
        this.argbText = argbText;
    }

    public OutlineBoxShape(PacketByteBuf buffer) {
        x1 = buffer.readDouble();
        y1 = buffer.readDouble();
        z1 = buffer.readDouble();
        x2 = buffer.readDouble();
        y2 = buffer.readDouble();
        z2 = buffer.readDouble();
        argb = buffer.readInt();
        layer = RenderLayer.values()[buffer.readVarInt()];
        argbLine = buffer.readInt();
        layerLine = RenderLayer.values()[buffer.readVarInt()];
        if (buffer.readBoolean()) {
            text = buffer.readString();
            argbText = buffer.readInt();
        } else {
            text = null;
            argbText = 0;
        }
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

    public int rLine() {
        return (argbLine >> 16) & 0xFF;
    }

    public int gLine() {
        return (argbLine >> 8) & 0xFF;
    }

    public int bLine() {
        return argbLine & 0xFF;
    }

    public int aLine() {
        return (argbLine >> 24) & 0xFF;
    }

    @Override
    public void render0(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {

        // Draw solid box
        var box0 = new Box(x1, y1, z1, x2, y2, z2);
        var box = box0.offset(-cameraX, -cameraY, -cameraZ);
        DebugRenderer.drawBox(box, r() / 255f, g() / 255f, b() / 255f, a() / 255f);

        // Draw outline box
        matrices.push();
        matrices.translate(-cameraX, -cameraY, -cameraZ);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(DebugRenderLayer.LINES);

        WorldRenderer.drawBox(matrices, vertexConsumer, x1, y1, z1, x2, y2, z2, rLine() / 255f, gLine() / 255f, bLine() / 255f, aLine() / 255f);

        // Draw text
        if (this.text != null) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            var pos = box0.getCenter();
            int width = textRenderer.getWidth(text);
            float scale = 0.010416667F * (float) Math.min(box0.getXLength(), Math.min(box0.getYLength(), box0.getZLength()));

            // +x
            matrices.loadIdentity();
            matrices.translate(pos.x, pos.y, 0);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

            // -x
            matrices.loadIdentity();
            matrices.translate(pos.x, pos.y, box0.maxZ);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 180, 0)));
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

            // +z
            matrices.loadIdentity();
            matrices.translate(0, pos.y, pos.z);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 90, 0)));
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

            // -z
            matrices.loadIdentity();
            matrices.translate(box0.maxX, pos.y, pos.z);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 270, 0)));
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

            // +y
            matrices.loadIdentity();
            matrices.translate(pos.x, box0.maxY, pos.z);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(90, 0, 0)));
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

            // -y
            matrices.loadIdentity();
            matrices.translate(pos.x, box0.minY, pos.z);
            matrices.translate(-cameraX, -cameraY, -cameraZ);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(270, 0, 0)));
            matrices.scale(-scale, -scale, scale);
            textRenderer.draw(matrices, text, -width / 2f, -textRenderer.fontHeight / 2f, argbLine);

        }

        matrices.pop();
    }
}
