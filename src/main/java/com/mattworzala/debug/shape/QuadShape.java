package com.mattworzala.debug.shape;

import com.mattworzala.debug.render.DebugRenderContext;
import com.mattworzala.debug.render.RenderLayer;
import com.mattworzala.debug.render.RenderType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public final class QuadShape implements Shape {
    private final @NotNull Vec3d a;
    private final @NotNull Vec3d b;
    private final @NotNull Vec3d c;
    private final @NotNull Vec3d d;
    private final int color;
    private final @NotNull RenderLayer renderLayer;

    private final Vec3d averagePoint;

    public QuadShape(
            @NotNull Vec3d a,
            @NotNull Vec3d b,
            @NotNull Vec3d c,
            @NotNull Vec3d d,
            int color,
            @NotNull RenderLayer renderLayer
    ) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.color = color;
        this.renderLayer = renderLayer;

        this.averagePoint = a.add(b).add(c).add(d).multiply(0.25);
    }

    public QuadShape(@NotNull PacketByteBuf buffer) {
        this(
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                buffer.readInt(), buffer.readEnumConstant(RenderLayer.class)
        );
    }

    @Override
    public void render(@NotNull DebugRenderContext context) {
        context.submit(this::render0, RenderType.QUADS, renderLayer);
    }

    @Override
    public double distanceTo(@NotNull Vec3d pos) {
        return pos.squaredDistanceTo(averagePoint);
    }

    private void render0(@NotNull DebugRenderContext context) {
        context.color(color);
        context.vertex(a);
        context.vertex(b);
        context.vertex(c);
        context.vertex(d);
    }

    public @NotNull Vec3d a() {
        return a;
    }

    public @NotNull Vec3d b() {
        return b;
    }

    public @NotNull Vec3d c() {
        return c;
    }

    public @NotNull Vec3d d() {
        return d;
    }

    public int color() {
        return color;
    }

    public @NotNull RenderLayer renderLayer() {
        return renderLayer;
    }

}
