package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.audience.Audience;
import net.minestom.server.network.packet.server.play.PluginMessagePacket;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.binary.BinaryWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * A message to send the client to show debug objects.
 *
 * @param ops The operations to perform.
 */
public record DebugMessage(
        List<Operation> ops
) {

    /**
     * @return A new {@link DebugMessage.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Sends this DebugMessage to an audience.
     */
    public void sendTo(Audience audience) {
        PacketUtils.sendPacket(audience, getPacket());
    }

    /**
     * @return The packet to send to an audience.
     */
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

        /**
         * Sets a shape with the specified namespace ID.
         *
         * @param namespaceId The namespace ID for this shape. If reused, the previous shape will be replaced.
         * @param shape       The shape to associate with the namespace ID.
         * @return The builder.
         */
        public Builder set(String namespaceId, Shape shape) {
            return set(NamespaceID.from(namespaceId), shape);
        }

        /**
         * Sets a shape with the specified namespace ID.
         *
         * @param id    The namespace ID for this shape. If reused, the previous shape will be replaced.
         * @param shape The shape to associate with the namespace ID.
         * @return The builder.
         */
        public Builder set(NamespaceID id, Shape shape) {
            ops.add(new Operation.Set(id, shape));
            return this;
        }

        /**
         * Removes a shape with a specified namespace ID.
         *
         * @param namespaceId The namespace ID to remove.
         * @return The builder.
         */
        public Builder remove(String namespaceId) {
            return remove(NamespaceID.from(namespaceId));
        }

        /**
         * Removes a shape with a specified namespace ID.
         *
         * @param id The namespace ID to remove.
         * @return The builder.
         */
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

        /**
         * @return Constructs a new {@link DebugMessage} with the provided builder parameters.
         */
        public DebugMessage build() {
            return new DebugMessage(ops);
        }

    }

}
