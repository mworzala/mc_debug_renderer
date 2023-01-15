package com.mattworzala.debug.client.shape;

import com.mattworzala.debug.client.render.DebugRenderContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Shape {

    enum Type {
        LINE(LineShape::new),
        SPLINE(SplineShape::new),
        QUAD(QuadShape::new),
        BOX(BoxShape::new);

        private final Function<PacketByteBuf, Shape> deserializer;

        Type(@NotNull Function<PacketByteBuf, Shape> deserializer) {
            this.deserializer = deserializer;
        }

        public @NotNull Shape deserialize(@NotNull PacketByteBuf buffer) {
            return deserializer.apply(buffer);
        }
    }

    void render(@NotNull DebugRenderContext context);

}
