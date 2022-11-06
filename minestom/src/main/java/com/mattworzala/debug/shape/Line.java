package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Line(
        List<Point> points,
        float thickness,
        int color,
        Layer layer
) implements Shape {
    private static final int ID = 1;

    @Override
    public void write(@NotNull BinaryWriter buffer) {
        buffer.writeVarInt(ID);
        buffer.writeVarInt(points.size());
        for (Point point : points) {
            buffer.writeDouble(point.x());
            buffer.writeDouble(point.y());
            buffer.writeDouble(point.z());
        }
        buffer.writeFloat(thickness);
        buffer.writeInt(color);
        buffer.writeVarInt(layer.ordinal());
    }

    public static class Builder {
        private final List<Point> points = new ArrayList<>();
        private float thickness = 0.1f;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;

        public Builder point(Point point) {
            points.add(point);
            return this;
        }

        public Builder thickness(float thickness) {
            this.thickness = thickness;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder layer(Layer layer) {
            this.layer = layer;
            return this;
        }

        public Line build() {
            Check.argCondition(points.size() < 2, "Line must have at least 2 points");
            return new Line(points, thickness, color, layer);
        }
    }
}
