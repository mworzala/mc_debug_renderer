package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

/**
 * A solid cube.
 *
 * @param start The starting corner of the cube.
 * @param end   The ending corner of the cube.
 * @param color The color of the cube, in ARGB format.
 * @param layer The layer of the cube.
 */
public record Box(
        Point start,
        Point end,
        int color,
        Layer layer
) implements Shape {

    private static final int ID = 0;

    @Override
    public void write(@NotNull BinaryWriter buffer) {
        buffer.writeVarInt(ID);
        buffer.writeDouble(start.x());
        buffer.writeDouble(start.y());
        buffer.writeDouble(start.z());
        buffer.writeDouble(end.x());
        buffer.writeDouble(end.y());
        buffer.writeDouble(end.z());
        buffer.writeInt(color);
        buffer.writeVarInt(layer.ordinal());
    }

    public static class Builder {

        private Point start;
        private Point end;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;

        /**
         * The starting corner of the box. Must be set.
         *
         * @param start The position.
         * @return The builder.
         */
        public Builder start(Point start) {
            this.start = start;
            return this;
        }

        /**
         * The ending corner of the box. Must be set.
         *
         * @param end The position.
         * @return The builder.
         */
        public Builder end(Point end) {
            this.end = end;
            return this;
        }

        /**
         * The color of the box in ARGB format.
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
         * The {@link Layer} of the box.
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
         * @return A {@link Box} constructed from the builder parameters.
         */
        public Box build() {
            Check.notNull(start, "start");
            Check.notNull(end, "end");
            return new Box(start, end, color, layer);
        }

    }

}
