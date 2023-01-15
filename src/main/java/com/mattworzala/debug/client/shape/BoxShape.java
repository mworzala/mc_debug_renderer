package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.DebugRenderContext;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.RenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record BoxShape(
        @NotNull Vec3d min,
        @NotNull Vec3d max,
        int faceColor,
        @NotNull RenderLayer faceRenderLayer,
        int edgeColor,
        @NotNull RenderLayer edgeRenderLayer,
        float edgeWidth
) implements Shape {

    public BoxShape {
        min = new Vec3d(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z));
        max = new Vec3d(Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }

    public BoxShape(@NotNull PacketByteBuf buffer) {
        this(
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                buffer.readInt(), buffer.readEnumConstant(RenderLayer.class),
                buffer.readInt(), buffer.readEnumConstant(RenderLayer.class),
                buffer.readFloat()
        );
    }

    @Override
    public void render(@NotNull DebugRenderContext context) {
        renderFaces(context);
        if ((edgeColor & 0xFF000000) != 0)
            renderEdges(context);
        if ((faceColor & 0xFF000000) != 0)
            renderEdges(context);
    }

    private void renderFaces(@NotNull DebugRenderContext context) {
        context.begin(RenderType.QUADS);
        try {
            context.layer(faceRenderLayer);
            context.color(faceColor);
            RenderSystem.lineWidth(edgeWidth);

            context.vertex(min.x, min.y, min.z);
            context.vertex(min.x, max.y, min.z);
            context.vertex(max.x, max.y, min.z);
            context.vertex(max.x, min.y, min.z);

            context.vertex(max.x, min.y, max.z);
            context.vertex(max.x, max.y, max.z);
            context.vertex(min.x, max.y, max.z);
            context.vertex(min.x, min.y, max.z);

            context.vertex(min.x, min.y, max.z);
            context.vertex(min.x, max.y, max.z);
            context.vertex(min.x, max.y, min.z);
            context.vertex(min.x, min.y, min.z);

            context.vertex(max.x, min.y, min.z);
            context.vertex(max.x, max.y, min.z);
            context.vertex(max.x, max.y, max.z);
            context.vertex(max.x, min.y, max.z);

            context.vertex(min.x, min.y, max.z);
            context.vertex(min.x, min.y, min.z);
            context.vertex(max.x, min.y, min.z);
            context.vertex(max.x, min.y, max.z);

            context.vertex(max.x, max.y, max.z);
            context.vertex(max.x, max.y, min.z);
            context.vertex(min.x, max.y, min.z);
            context.vertex(min.x, max.y, max.z);
        } finally {
            context.end();
        }
    }

    private void renderEdges(@NotNull DebugRenderContext context) {
        context.begin(RenderType.LINES);
        try {
            context.layer(edgeRenderLayer);
            context.color(edgeColor);

            context.vertex(min.x, min.y, min.z);
            context.vertex(min.x, max.y, min.z);

            context.vertex(min.x, max.y, min.z);
            context.vertex(max.x, max.y, min.z);

            context.vertex(max.x, max.y, min.z);
            context.vertex(max.x, min.y, min.z);

            context.vertex(max.x, min.y, min.z);
            context.vertex(min.x, min.y, min.z);

            context.vertex(min.x, min.y, max.z);
            context.vertex(min.x, max.y, max.z);

            context.vertex(min.x, max.y, max.z);
            context.vertex(max.x, max.y, max.z);

            context.vertex(max.x, max.y, max.z);
            context.vertex(max.x, min.y, max.z);

            context.vertex(max.x, min.y, max.z);
            context.vertex(min.x, min.y, max.z);

            context.vertex(min.x, min.y, min.z);
            context.vertex(min.x, min.y, max.z);

            context.vertex(min.x, max.y, min.z);
            context.vertex(min.x, max.y, max.z);

            context.vertex(max.x, max.y, min.z);
            context.vertex(max.x, max.y, max.z);

            context.vertex(max.x, min.y, min.z);
            context.vertex(max.x, min.y, max.z);
        } finally {
            context.end();
        }
    }

}
