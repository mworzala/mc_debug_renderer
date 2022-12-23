package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

/**
 * Text.
 *
 * @param position The position of the text.
 * @param content  What the text should say.
 * @param color    The color of the cube, in ARGB format.
 * @param size     The size of text, in arbitrary units.
 * @param layer    The layer of the cube.
 */
public record Text(
        Point position,
        String content,
        int color,
        float size,
        Layer layer
) implements Shape {

    private static final int ID = 2;

    @Override
    public void write(@NotNull BinaryWriter buffer) {
        buffer.writeVarInt(ID);
        buffer.writeDouble(position.x());
        buffer.writeDouble(position.y());
        buffer.writeDouble(position.z());
        buffer.writeSizedString(content);
        buffer.writeInt(color);
        buffer.writeFloat(size);
        buffer.writeVarInt(layer.ordinal());
    }

    public static class Builder {

        private Point position;
        private String content;
        private int color = 0xFFFFFFFF;
        private float size = 0.02f;
        private Layer layer = Layer.INLINE;

        /**
         * The position to render the text. Must be set.
         *
         * @param position The position.
         * @return The builder.
         */
        public Builder position(Point position) {
            this.position = position;
            return this;
        }

        /**
         * The text to show. Must be set.
         *
         * @param content The text.
         * @return The builder.
         */
        public Builder content(String content) {
            this.content = content;
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
         * The size of the text, in arbitrary units.
         * <p>
         * Defaults to 0.02 if not set.
         *
         * @param size The size.
         * @return The builder.
         */
        public Builder size(float size) {
            this.size = size;
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
         * @return A {@link Text} constructed from the builder parameters.
         */
        public Text build() {
            Check.notNull(position, "position");
            Check.notNull(content, "content");
            return new Text(position, content, color, size, layer);
        }

    }

}
