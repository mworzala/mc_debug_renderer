package com.mattworzala.debug.client.render.shape;

import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.Shape;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.network.PacketByteBuf;

public class BoxShape extends Shape {
    public final double x1;
    public final double y1;
    public final double z1;
    public final double x2;
    public final double y2;
    public final double z2;

    public BoxShape(PacketByteBuf buffer) {
        x1 = buffer.readDouble();
        y1 = buffer.readDouble();
        z1 = buffer.readDouble();
        x2 = buffer.readDouble();
        y2 = buffer.readDouble();
        z2 = buffer.readDouble();
        argb = buffer.readInt();
        layer = RenderLayer.values()[buffer.readVarInt()];
    }

    public BoxShape(double x1, double y1, double z1, double x2, double y2, double z2, int argb, RenderLayer layer) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
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
        var box = new net.minecraft.util.math.Box(x1, y1, z1, x2, y2, z2)
                .offset(-cameraX, -cameraY, -cameraZ);
        DebugRenderer.drawBox(box, r() / 255f, g() / 255f, b() / 255f, a() / 255f);
    }
}
