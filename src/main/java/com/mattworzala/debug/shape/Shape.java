package com.mattworzala.debug.shape;

import com.mattworzala.debug.render.DebugRenderContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Shape {

    void render(@NotNull DebugRenderContext context);

    double distanceTo(@NotNull Vec3d pos);

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

}
