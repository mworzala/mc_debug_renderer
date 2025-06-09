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
    public void render(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, @NotNull VertexConsumerProvider vcp) {
        
        if ((faceColor & 0xFF000000) != 0) {
            float w = (float)(max.x - min.x);
            float h = (float)(max.y - min.y);
            float d = (float)(max.z - min.z);
            Vec3d center = min.add(w / 2f, h / 2f, d / 2f);

            if (faceRenderLayer == RenderLayer.INLINE || faceRenderLayer == RenderLayer.MIXED) {
                context.drawFilledCube(matrices, CustomRenderLayers.POS_COL_QUADS_WITH_DEPTH_TEST, center, w, h, d, new Color(faceColor));
            }
            if (faceRenderLayer == RenderLayer.TOP || faceRenderLayer == RenderLayer.MIXED) {
                int transparentFaceColor = (faceColor & 0x00FFFFFF) | ((int) (((faceColor >> 24) & 0xFF) * 0.2f) << 24);
                context.drawFilledCube(matrices, CustomRenderLayers.POS_COL_QUADS_NO_DEPTH_TEST, center, w, h, d, new Color(transparentFaceColor));
            }
        }

        
        if ((edgeColor & 0xFF000000) != 0) {
            if (edgeRenderLayer == RenderLayer.INLINE || edgeRenderLayer == RenderLayer.MIXED) {
                
                renderEdges(matrices, context, net.minecraft.client.render.RenderLayer.getLines(), new Color(edgeColor));
            }
            if (edgeRenderLayer == RenderLayer.TOP || edgeRenderLayer == RenderLayer.MIXED) {
                var layer = CustomRenderLayers.LINES_NO_DEPTH_TEST.apply((double) edgeWidth);
                int transparentEdgeColor = (edgeColor & 0x00FFFFFF) | ((int) (((edgeColor >> 24) & 0xFF) * 0.2f) << 24);
                renderEdges(matrices, context, layer, new Color(transparentEdgeColor));
            }
        }
    }

    
    private void renderEdges(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, net.minecraft.client.render.RenderLayer layer, Color color) {
        
        Vec3d p0 = new Vec3d(min.x, min.y, min.z);
        Vec3d p1 = new Vec3d(max.x, min.y, min.z);
        Vec3d p2 = new Vec3d(max.x, min.y, max.z);
        Vec3d p3 = new Vec3d(min.x, min.y, max.z);
        Vec3d p4 = new Vec3d(min.x, max.y, min.z);
        Vec3d p5 = new Vec3d(max.x, max.y, min.z);
        Vec3d p6 = new Vec3d(max.x, max.y, max.z);
        Vec3d p7 = new Vec3d(min.x, max.y, max.z);

        
        context.drawLine(matrices, layer, p0, p1, color);
        context.drawLine(matrices, layer, p1, p2, color);
        context.drawLine(matrices, layer, p2, p3, color);
        context.drawLine(matrices, layer, p3, p0, color);

        
        context.drawLine(matrices, layer, p4, p5, color);
        context.drawLine(matrices, layer, p5, p6, color);
        context.drawLine(matrices, layer, p6, p7, color);
        context.drawLine(matrices, layer, p7, p4, color);

        
        context.drawLine(matrices, layer, p0, p4, color);
        context.drawLine(matrices, layer, p1, p5, color);
        context.drawLine(matrices, layer, p2, p6, color);
        context.drawLine(matrices, layer, p3, p7, color);
    }

    @Override
    public double distanceTo(@NotNull Vec3d pos) {
        var center = min.add(max).multiply(0.5);
        return pos.squaredDistanceTo(center);
    }
}