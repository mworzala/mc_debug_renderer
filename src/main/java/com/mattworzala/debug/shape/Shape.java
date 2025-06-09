package com.mattworzala.debug.shape;

import me.x150.renderer.render.WorldRenderContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Shape {

    
    void render(@NotNull MatrixStack matrices, @NotNull WorldRenderContext context, @NotNull VertexConsumerProvider vcp);

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