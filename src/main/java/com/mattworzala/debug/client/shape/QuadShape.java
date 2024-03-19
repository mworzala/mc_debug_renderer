package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.DebugRenderContext;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.RenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record QuadShape(
        @NotNull Vec3d a,
        @NotNull Vec3d b,
        @NotNull Vec3d c,
        @NotNull Vec3d d,
        int color,
        @NotNull RenderLayer renderLayer
) implements Shape {

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

    private void render0(@NotNull DebugRenderContext context) {
        context.color(color);
        context.vertex(a);
        context.vertex(b);
        context.vertex(c);
        context.vertex(d);
    }
}
