package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

public record OutlineBox(
        Vec start,
        Vec end,
        int color,
        Layer layer,
        int colorLine,
        Layer layerLine,
        String text,
        int colorText
) implements Shape {

    private static final int ID = 3;

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
        buffer.writeInt(colorLine);
        buffer.writeVarInt(layerLine.ordinal());
        buffer.writeBoolean(text != null);
        if (text != null) {
            buffer.writeSizedString(text);
            buffer.writeInt(colorText);
        }
    }

    public static class Builder {

        private Vec start;
        private Vec end;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;
        private int colorLine = 0xFFFFFFFF;
        private Layer layerLine = Layer.INLINE;
        private String text = null;
        private int colorText = 0xFFFFFFFF;

        /**
         * The starting corner of the box. Must be set.
         *
         * @param start The position.
         * @return The builder.
         */
        public Builder start(Vec start) {
            this.start = start;
            return this;
        }

        /**
         * The ending corner of the box. Must be set.
         *
         * @param end The position.
         * @return The builder.
         */
        public Builder end(Vec end) {
            this.end = end;
            return this;
        }

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

        public Builder colorLine(int color) {
            this.colorLine = color;
            return this;
        }

        public Builder layerLine(Layer layer) {
            this.layerLine = layer;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder colorText(int color) {
            this.colorText = color;
            return this;
        }

        public OutlineBox build() {
            Check.notNull(start, "start");
            Check.notNull(end, "end");
            return new OutlineBox(start, end, color, layer, colorLine, layerLine, text, colorText);
        }

    }

}
