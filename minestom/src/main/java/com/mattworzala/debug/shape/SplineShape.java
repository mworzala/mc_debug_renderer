package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public record SplineShape(
        @NotNull Type type,
        @NotNull List<Point> points,
        boolean loop,
        int color,
        @NotNull Layer layer,
        float lineWidth
) implements Shape {
    public enum Type {
        CATMULL_ROM,
        BEZIER,
    }

    public static final NetworkBuffer.Type<SplineShape> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.Enum(Type.class), SplineShape::type,
            NetworkBuffer.VECTOR3D.list(), SplineShape::points,
            NetworkBuffer.BOOLEAN, SplineShape::loop,
            NetworkBuffer.INT, SplineShape::color,
            NetworkBuffer.Enum(Layer.class), SplineShape::layer,
            NetworkBuffer.FLOAT, SplineShape::lineWidth,
            SplineShape::new
    );

    public static class Builder {
        private Type type = Type.CATMULL_ROM;
        private final List<Point> points = new ArrayList<>();
        private boolean loop = false;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;
        private float lineWidth = 3.0f;

        public @NotNull Builder type(@NotNull Type type) {
            this.type = type;
            return this;
        }

        public @NotNull Builder point(@NotNull Point point) {
            this.points.add(point);
            return this;
        }

        public @NotNull Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public @NotNull Builder color(int color) {
            this.color = color;
            return this;
        }

        public @NotNull Builder layer(@NotNull Layer layer) {
            this.layer = layer;
            return this;
        }

        public @NotNull Builder lineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public @NotNull SplineShape build() {
            return new SplineShape(type, points, loop, color, layer, lineWidth);
        }
    }
}
