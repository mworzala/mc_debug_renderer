package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.DebugRenderContext;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.render.RenderType;
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
        if ((faceColor & 0xFF000000) != 0)
            context.submit(this::renderFaces, RenderType.QUADS, faceRenderLayer);
        if ((edgeColor & 0xFF000000) != 0)
            context.submit(this::renderEdges, RenderType.LINES, edgeRenderLayer);
    }

    private void renderFaces(@NotNull DebugRenderContext context) {
        context.color(faceColor);

        context.vertex((float) min.x, (float) min.y, (float) min.z);
        context.vertex((float) min.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) min.y, (float) min.z);

        context.vertex((float) max.x, (float) min.y, (float) max.z);
        context.vertex((float) max.x, (float) max.y, (float) max.z);
        context.vertex((float) min.x, (float) max.y, (float) max.z);
        context.vertex((float) min.x, (float) min.y, (float) max.z);

        context.vertex((float) min.x, (float) min.y, (float) max.z);
        context.vertex((float) min.x, (float) max.y, (float) max.z);
        context.vertex((float) min.x, (float) max.y, (float) min.z);
        context.vertex((float) min.x, (float) min.y, (float) min.z);

        context.vertex((float) max.x, (float) min.y, (float) min.z);
        context.vertex((float) max.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) max.y, (float) max.z);
        context.vertex((float) max.x, (float) min.y, (float) max.z);

        context.vertex((float) min.x, (float) min.y, (float) max.z);
        context.vertex((float) min.x, (float) min.y, (float) min.z);
        context.vertex((float) max.x, (float) min.y, (float) min.z);
        context.vertex((float) max.x, (float) min.y, (float) max.z);

        context.vertex((float) max.x, (float) max.y, (float) max.z);
        context.vertex((float) max.x, (float) max.y, (float) min.z);
        context.vertex((float) min.x, (float) max.y, (float) min.z);
        context.vertex((float) min.x, (float) max.y, (float) max.z);
    }

    private void renderEdges(@NotNull DebugRenderContext context) {
        context.color(edgeColor);

        context.vertex((float) min.x, (float) min.y, (float) min.z);
        context.vertex((float) min.x, (float) max.y, (float) min.z);

        context.vertex((float) min.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) max.y, (float) min.z);

        context.vertex((float) max.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) min.y, (float) min.z);

        context.vertex((float) max.x, (float) min.y, (float) min.z);
        context.vertex((float) min.x, (float) min.y, (float) min.z);

        context.vertex((float) min.x, (float) min.y, (float) max.z);
        context.vertex((float) min.x, (float) max.y, (float) max.z);

        context.vertex((float) min.x, (float) max.y, (float) max.z);
        context.vertex((float) max.x, (float) max.y, (float) max.z);

        context.vertex((float) max.x, (float) max.y, (float) max.z);
        context.vertex((float) max.x, (float) min.y, (float) max.z);

        context.vertex((float) max.x, (float) min.y, (float) max.z);
        context.vertex((float) min.x, (float) min.y, (float) max.z);

        context.vertex((float) min.x, (float) min.y, (float) min.z);
        context.vertex((float) min.x, (float) min.y, (float) max.z);

        context.vertex((float) min.x, (float) max.y, (float) min.z);
        context.vertex((float) min.x, (float) max.y, (float) max.z);

        context.vertex((float) max.x, (float) max.y, (float) min.z);
        context.vertex((float) max.x, (float) max.y, (float) max.z);

        context.vertex((float) max.x, (float) min.y, (float) min.z);
        context.vertex((float) max.x, (float) min.y, (float) max.z);
    }

}
