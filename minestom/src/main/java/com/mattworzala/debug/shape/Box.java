package com.mattworzala.debug.shape;

import com.mattworzala.debug.Layer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

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

        public Builder start(Point start) {
            this.start = start;
            return this;
        }

        public Builder end(Point end) {
            this.end = end;
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

        public Box build() {
            Check.notNull(start, "start");
            Check.notNull(end, "end");
            return new Box(start, end, color, layer);
        }
    }
}
