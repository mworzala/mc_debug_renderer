package com.mattworzala.debug.network;

import com.mattworzala.debug.shape.Shape;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DebugShapesPacket(@NotNull List<Operation> operations) implements CustomPayload {
    public static final CustomPayload.Id<DebugShapesPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("debug", "shapes"));
    public static final PacketCodec<RegistryByteBuf, DebugShapesPacket> PACKET_CODEC = PacketCodec.of(DebugShapesPacket::write, DebugShapesPacket::new);

    public DebugShapesPacket(@NotNull PacketByteBuf buf) {
        this(buf.readList(DebugShapesPacket::readOperation));
    }

    private static @NotNull Operation readOperation(@NotNull PacketByteBuf buf) {
        return switch (buf.readVarInt()) {
            case 0 -> new Set(buf.readIdentifier(), buf.readEnumConstant(Shape.Type.class).deserialize(buf));
            case 1 -> new Remove(buf.readIdentifier());
            case 2 -> new ClearNamespace(buf.readString(32767));
            case 3 -> new Clear();
            default -> {
                throw new IllegalArgumentException("Unknown operation type");
            }
        };
    }

    public void write(@NotNull PacketByteBuf buf) {
        throw new UnsupportedOperationException("DebugShapesPacket is read-only!");
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public sealed interface Operation permits Set, Remove, ClearNamespace, Clear {
    }

    public record Set(@NotNull Identifier namespaceId, @NotNull Shape shape) implements Operation {
    }

    public record Remove(@NotNull Identifier namespaceId) implements Operation {
    }

    public record ClearNamespace(@NotNull String namespace) implements Operation {
    }

    public record Clear() implements Operation {
    }
}
