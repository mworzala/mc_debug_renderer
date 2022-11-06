package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

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

        public Builder position(Point position) {
            this.position = position;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder size(float size) {
            this.size = size;
            return this;
        }

        public Builder layer(Layer layer) {
            this.layer = layer;
            return this;
        }

        public Text build() {
            Check.notNull(position, "position");
            Check.notNull(content, "content");
            return new Text(position, content, color, size, layer);
        }
    }
}
