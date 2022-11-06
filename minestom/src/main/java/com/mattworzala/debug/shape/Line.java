package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A line connected with multiple points.
 *
 * @param points    The points the line should go through.
 * @param thickness The thickness of the line.
 * @param color     The color of the line, in ARGB format.
 * @param layer     The layer of the line.
 */
public record Line(
        List<Vec> points,
        float thickness,
        int color,
        Layer layer
) implements Shape {

    private static final int ID = 1;

    @Override
    public void write(@NotNull BinaryWriter buffer) {
        buffer.writeVarInt(ID);
        buffer.writeVarInt(points.size());
        for (Vec point : points) {
            buffer.writeDouble(point.x());
            buffer.writeDouble(point.y());
            buffer.writeDouble(point.z());
        }
        buffer.writeFloat(thickness);
        buffer.writeInt(color);
        buffer.writeVarInt(layer.ordinal());
    }

    public static class Builder {

        private final List<Vec> points = new ArrayList<>();
        private float thickness = 0.1f;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;

        /**
         * Adds a point to the line.
         * Lines will be rendered in order from the first point to the last.
         * There must be at least 2 points to render a line.
         *
         * @param point The point.
         * @return The builder.
         */
        public Builder point(Vec point) {
            points.add(point);
            return this;
        }

        public Builder thickness(float thickness) {
            this.thickness = thickness;
            return this;
        }

        /**
         * The color of the line in ARGB format.
         * <p>
         * Defaults to pure white if not set.
         *
         * @param color The color.
         * @return The builder.
         */
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * The {@link Layer} of the line.
         * <p>
         * Defaults to {@link Layer#INLINE} if not set.
         *
         * @param layer The layer.
         * @return The builder.
         */
        public Builder layer(Layer layer) {
            this.layer = layer;
            return this;
        }

        /**
         * @return A {@link Line} constructed from the builder parameters.
         */
        public Line build() {
            Check.argCondition(points.size() < 2, "Line must have at least 2 points");
            return new Line(points, thickness, color, layer);
        }

    }

}
