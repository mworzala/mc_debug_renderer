package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A line connected with multiple points.
 *
 * @param points    The points the line should go through.
 * @param lineWidth The thickness of the line.
 * @param color     The color of the line, in ARGB format.
 * @param layer     The layer of the line.
 */
public record LineShape(
        @NotNull Type type,
        @NotNull List<Point> points,
        int color,
        @NotNull Layer layer,
        float lineWidth
) implements Shape {

    public enum Type {
        SINGLE,
        STRIP,
        LOOP
    }

    public static final NetworkBuffer.Type<LineShape> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.Enum(Type.class), LineShape::type,
            NetworkBuffer.VECTOR3D.list(), LineShape::points,
            NetworkBuffer.INT, LineShape::color,
            NetworkBuffer.Enum(Layer.class), LineShape::layer,
            NetworkBuffer.FLOAT, LineShape::lineWidth,
            LineShape::new);

    public static class Builder {


        private Type type = Type.SINGLE;
        private final List<Point> points = new ArrayList<>();
        private float lineWidth = 4f;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;

        public @NotNull Builder type(@NotNull Type type) {
            this.type = type;
            return this;
        }

        /**
         * Adds a point to the line.
         * Lines will be rendered in order from the first point to the last.
         * There must be at least 2 points to render a line.
         *
         * @param point The point.
         * @return The builder.
         */
        public @NotNull Builder point(@NotNull Point point) {
            points.add(point);
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
        public @NotNull Builder color(int color) {
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
        public @NotNull Builder layer(@NotNull Layer layer) {
            this.layer = layer;
            return this;
        }

        public @NotNull Builder lineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        /**
         * @return A {@link LineShape} constructed from the builder parameters.
         */
        public @NotNull LineShape build() {
            Check.argCondition(points.size() < 2, "Line must have at least 2 points");
            return new LineShape(type, points, color, layer, lineWidth);
        }

    }

}
