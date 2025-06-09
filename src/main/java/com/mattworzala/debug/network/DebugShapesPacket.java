package com.mattworzala.debug.network;

import com.mattworzala.debug.shape.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DebugShapesPacket(@NotNull List<Operation> operations) implements CustomPayload {
    public static final CustomPayload.Id<DebugShapesPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("debug", "shapes"));
    public static final PacketCodec<RegistryByteBuf, DebugShapesPacket> PACKET_CODEC = PacketCodec.of(DebugShapesPacket::write, DebugShapesPacket::new);

    public DebugShapesPacket(@NotNull PacketByteBuf buf) {
        this(buf.readList(DebugShapesPacket::readOperation));
    }

    private static @NotNull Operation readOperation(@NotNull PacketByteBuf buf) {
        int operationId = buf.readVarInt();
        return switch (operationId) {
            case 0 -> { 
                Identifier id = buf.readIdentifier();
                
                int shapeTypeId = buf.readVarInt();
                Shape shape = switch (shapeTypeId) {
                    case 0 -> new LineShape(buf);
                    case 1 -> new SplineShape(buf);
                    case 2 -> new QuadShape(buf);
                    case 3 -> new BoxShape(buf);
                    default -> throw new IllegalArgumentException("Unknown shape type ID: " + shapeTypeId);
                };
                yield new Set(id, shape);
            }
            case 1 -> new Remove(buf.readIdentifier());
            case 2 -> new ClearNamespace(buf.readString(32767));
            case 3 -> new Clear();
            default -> throw new IllegalArgumentException("Unknown operation type: " + operationId);
        };
    }

    public void write(@NotNull PacketByteBuf buf) {
        throw new UnsupportedOperationException("DebugShapesPacket is read-only!");
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public sealed interface Operation permits Set, Remove, ClearNamespace, Clear {}
    public record Set(@NotNull Identifier namespaceId, @NotNull Shape shape) implements Operation {}
    public record Remove(@NotNull Identifier namespaceId) implements Operation {}
    public record ClearNamespace(@NotNull String namespace) implements Operation {}
    public record Clear() implements Operation {}
}