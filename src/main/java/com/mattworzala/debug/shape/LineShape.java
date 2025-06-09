package com.mattworzala.debug.shape;

import com.mattworzala.debug.render.RenderLayer;
import me.x150.renderer.render.CustomRenderLayers;
import me.x150.renderer.render.WorldRenderContext;
import me.x150.renderer.util.Color;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LineShape implements Shape {
    private final Type type;
    private final List<Vec3d> points;
    private final int color;
    private final RenderLayer renderLayer;
    private final float lineWidth;
    private Vec3d averagePoint;

    protected LineShape(Type type, @NotNull List<Vec3d> points, int color, RenderLayer renderLayer, float lineWidth) {
        this.type = type;
        this.points = points;
        this.color = color;
        this.renderLayer = renderLayer;
        this.lineWidth = lineWidth;
        this.averagePoint = new Vec3d(0, 0, 0);
        for (Vec3d point : points) {
            this.averagePoint = this.averagePoint.add(point);
        }
        if (!points.isEmpty()) {
            this.averagePoint = this.averagePoint.multiply(1.0 / points.size());
        }
    }

    
    public LineShape(@NotNull PacketByteBuf buffer) {
        this(
                buffer.readEnumConstant(Type.class),
                buffer.readList(buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())),
                buffer.readInt(),
                buffer.readEnumConstant(RenderLayer.class),
                buffer.readFloat()
        );
    }


    @Override
    public void render(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, @NotNull VertexConsumerProvider vcp) {
        if (renderLayer == RenderLayer.INLINE || renderLayer == RenderLayer.MIXED) {
            
            renderWithLayer(matrices, context, net.minecraft.client.render.RenderLayer.getLines(), new Color(color));
        }

        if (renderLayer == RenderLayer.TOP || renderLayer == RenderLayer.MIXED) {
            var topLayer = CustomRenderLayers.LINES_NO_DEPTH_TEST.apply((double) lineWidth);
            int transparentColor = (color & 0x00FFFFFF) | ((int) (((color >> 24) & 0xFF) * 0.2f) << 24);
            renderWithLayer(matrices, context, topLayer, new Color(transparentColor));
        }
    }

    private void renderWithLayer(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, net.minecraft.client.render.RenderLayer layer, Color renderColor) {
        if (points.isEmpty()) return;

        switch (type) {
            case SINGLE -> {
                for (int i = 0; i < points.size() - 1; i += 2) {
                    context.drawLine(matrices, layer, points.get(i), points.get(i + 1), renderColor);
                }
            }
            case STRIP -> {
                for (int i = 0; i < points.size() - 1; i++) {
                    context.drawLine(matrices, layer, points.get(i), points.get(i + 1), renderColor);
                }
            }
            case LOOP -> {
                for (int i = 0; i < points.size() - 1; i++) {
                    context.drawLine(matrices, layer, points.get(i), points.get(i + 1), renderColor);
                }
                if (points.size() > 1) {
                    context.drawLine(matrices, layer, points.get(points.size() - 1), points.get(0), renderColor);
                }
            }
        }
    }

    @Override
    public double distanceTo(@NotNull Vec3d pos) {
        return pos.squaredDistanceTo(averagePoint);
    }

    public enum Type {
        SINGLE, STRIP, LOOP
    }
}