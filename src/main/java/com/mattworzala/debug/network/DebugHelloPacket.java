package com.mattworzala.debug.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record DebugHelloPacket(int protocolVersion) implements CustomPayload {
    public static final CustomPayload.Id<DebugHelloPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("debug", "hello"));
    public static final PacketCodec<RegistryByteBuf, DebugHelloPacket> PACKET_CODEC = PacketCodec.of(DebugHelloPacket::write, DebugHelloPacket::new);

    public DebugHelloPacket(@NotNull RegistryByteBuf buf) {
        this(buf.readInt());
    }

    public void write(@NotNull RegistryByteBuf buf) {
        buf.writeInt(protocolVersion);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
