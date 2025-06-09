package com.mattworzala.debug.shape;

import com.mattworzala.debug.render.RenderLayer;
import me.x150.renderer.render.CustomRenderLayers;
import me.x150.renderer.render.WorldRenderContext;
import me.x150.renderer.util.Color;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
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
    public void render(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, @NotNull VertexConsumerProvider vcp) {
        if (renderLayer == RenderLayer.INLINE || renderLayer == RenderLayer.MIXED) {
            renderWithLayer(matrices, vcp, CustomRenderLayers.POS_COL_QUADS_WITH_DEPTH_TEST, new Color(color));
        }
        if (renderLayer == RenderLayer.TOP || renderLayer == RenderLayer.MIXED) {
            int transparentColor = (color & 0x00FFFFFF) | ((int) (((color >> 24) & 0xFF) * 0.2f) << 24);
            renderWithLayer(matrices, vcp, CustomRenderLayers.POS_COL_QUADS_NO_DEPTH_TEST, new Color(transparentColor));
        }
    }

    private void renderWithLayer(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, net.minecraft.client.render.RenderLayer layer, Color renderColor) {
        VertexConsumer buffer = vcp.getBuffer(layer);
        MatrixStack.Entry matrixEntry = matrices.peek();

        float red = renderColor.red() / 255f;
        float green = renderColor.green() / 255f;
        float blue = renderColor.blue() / 255f;
        float alpha = renderColor.alpha() / 255f;

        buffer.vertex(matrixEntry, (float)a.x, (float)a.y, (float)a.z).color(red, green, blue, alpha);
        buffer.vertex(matrixEntry, (float)b.x, (float)b.y, (float)b.z).color(red, green, blue, alpha);
        buffer.vertex(matrixEntry, (float)c.x, (float)c.y, (float)c.z).color(red, green, blue, alpha);
        buffer.vertex(matrixEntry, (float)d.x, (float)d.y, (float)d.z).color(red, green, blue, alpha);
    }

    @Override
    public double distanceTo(@NotNull Vec3d pos) {
        return pos.squaredDistanceTo(averagePoint);
    }

    
    public @NotNull Vec3d a() { return a; }
    public @NotNull Vec3d b() { return b; }
    public @NotNull Vec3d c() { return c; }
    public @NotNull Vec3d d() { return d; }
    public int color() { return color; }
    public @NotNull RenderLayer renderLayer() { return renderLayer; }
}