package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.shape.util.CatmullRomSpline;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SplineShape extends LineShape {

    public enum Type {
        CATMULL_ROM,
    }

    public SplineShape(@NotNull Type type, @NotNull List<Vec3d> points, boolean loop, int color,
                       @NotNull RenderLayer renderLayer, float lineWidth) {
        super(LineShape.Type.STRIP, createPoints(type, points, loop), color, renderLayer, lineWidth);
    }

    public SplineShape(@NotNull PacketByteBuf buffer) {
        super(LineShape.Type.STRIP, createPointsFromBuffer(buffer), buffer.readInt(),
                buffer.readEnumConstant(RenderLayer.class), buffer.readFloat());
    }

    private static @NotNull List<Vec3d> createPointsFromBuffer(@NotNull PacketByteBuf buffer) {
        var type = buffer.readEnumConstant(Type.class);
        var points = buffer.readList(buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
        var loop = buffer.readBoolean();

        return createPoints(type, points, loop);
    }

    private static @NotNull List<Vec3d> createPoints(@NotNull Type type, @NotNull List<Vec3d> points, boolean loop) {
        return switch (type) {
            case CATMULL_ROM -> CatmullRomSpline.getCatmullRomChain(points, loop);
        };
    }
}
