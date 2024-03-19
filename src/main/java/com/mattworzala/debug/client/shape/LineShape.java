package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.DebugRenderContext;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.RenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LineShape implements Shape {

    public enum Type {
        SINGLE,
        STRIP,
        LOOP
    }

    private final Type type;
    private final List<Vec3d> points;
    private final int color;
    private final RenderLayer renderLayer;
    private final float lineWidth;

    public LineShape(Type type, @NotNull List<Vec3d> points, int color, RenderLayer renderLayer, float lineWidth) {
        this.type = type;
        this.points = points;
        this.color = color;
        this.renderLayer = renderLayer;
        this.lineWidth = lineWidth;
    }

    public LineShape(@NotNull PacketByteBuf buffer) {
        this.type = buffer.readEnumConstant(Type.class);
        this.points = buffer.readList(buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
        this.color = buffer.readInt();
        this.renderLayer = buffer.readEnumConstant(RenderLayer.class);
        this.lineWidth = buffer.readFloat();
    }

    @Override
    public void render(@NotNull DebugRenderContext context) {
        context.submit(this::render0, RenderType.LINES, renderLayer);
    }

    private void render0(@NotNull DebugRenderContext context) {
        context.color(color);
        RenderSystem.lineWidth(lineWidth);
        switch (type) {
            case SINGLE -> {
                for (var point : points) {
                    context.vertex(point);
                }
            }
            case STRIP -> {
                for (int i = 0; i < points.size() - 1; i++) {
                    context.vertex(points.get(i));
                    context.vertex(points.get(i + 1));
                }
            }
            case LOOP -> {
                for (int i = 0; i < points.size() - 1; i++) {
                    context.vertex(points.get(i));
                    context.vertex(points.get(i + 1));
                }
                context.vertex(points.get(points.size() - 1));
                context.vertex(points.get(0));
            }
        }
    }
}
