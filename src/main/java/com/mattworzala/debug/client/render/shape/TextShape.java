package com.mattworzala.debug.client.render.shape;

import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.Shape;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class TextShape extends Shape {
    public final Vec3d position;
    public final String content;
    public final float size;

    public TextShape(PacketByteBuf buffer) {
        position = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        content = buffer.readString(32767);
        argb = buffer.readInt();
        size = buffer.readFloat();
        layer = RenderLayer.values()[buffer.readVarInt()];
    }

    public TextShape(double x, double y, double z, String content, int argb, float size, RenderLayer layer) {
        this.position = new Vec3d(x, y, z);
        this.content = content;
        this.argb = argb;
        this.size = size;
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
        DebugRenderer.drawString(content, position.x, position.y, position.z, argb, size, true, 0f, layer == RenderLayer.TOP);
    }
}
