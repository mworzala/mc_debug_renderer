package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.audience.Audience;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.PluginMessagePacket;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.binary.BinaryWriter;

import java.util.ArrayList;
import java.util.List;

public record DebugMessage(
        List<Operation> ops
) {
    public static Builder builder() {
        return new Builder();
    }

    public void sendTo(Audience audience) {
        PacketUtils.sendPacket(audience, getPacket());
    }

    public PluginMessagePacket getPacket() {
        BinaryWriter writer = new BinaryWriter(1024);
        writer.writeVarInt(ops.size());
        for (Operation op : ops) {
            op.write(writer);
        }
        return new PluginMessagePacket("debug:shapes", writer.toByteArray());
    }


    public static class Builder {
        private final List<Operation> ops = new ArrayList<>();

        public Builder set(String namespaceId, Shape shape) {
            return set(NamespaceID.from(namespaceId), shape);
        }

        public Builder set(NamespaceID id, Shape shape) {
            ops.add(new Operation.Set(id, shape));
            return this;
        }

        public Builder remove(String namespaceId) {
            return remove(NamespaceID.from(namespaceId));
        }

        public Builder remove(NamespaceID id) {
            ops.add(new Operation.Remove(id));
            return this;
        }

        public Builder clear(String namespace) {
            ops.add(new Operation.ClearNS(namespace));
            return this;
        }

        public Builder clear() {
            ops.add(new Operation.Clear());
            return this;
        }

        public DebugMessage build() {
            return new DebugMessage(ops);
        }
    }
}
