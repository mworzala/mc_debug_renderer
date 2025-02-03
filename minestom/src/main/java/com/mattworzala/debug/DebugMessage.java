package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.common.PluginMessagePacket;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.PacketSendingUtils;

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
        PacketSendingUtils.sendPacket(audience, getPacket());
    }

    /**
     * @return The packet to send to an audience.
     */
    public PluginMessagePacket getPacket() {
        return new PluginMessagePacket("debug:shapes", NetworkBuffer.makeArray(buffer -> {
            buffer.write(SimplePacketRegistry.OPERATION_SERIALIZER.list(), ops);
        }));
    }


    public static class Builder {

        private final List<Operation> ops = new ArrayList<>();

        /**
         * Sets a shape with the specified key.
         *
         * @param key The key for this shape. If reused, the previous shape will be replaced.
         * @param shape       The shape to associate with the key.
         * @return The builder.
         */
        public Builder set(String key, Shape shape) {
            return set(Key.key(key), shape);
        }

        /**
         * Use {@link #set(Key, Shape)} instead.
         * Sets a shape with the specified namespace ID.
         *
         * @param id    The namespace ID for this shape. If reused, the previous shape will be replaced.
         * @param shape The shape to associate with the namespace ID.
         * @return The builder.
         */
        @Deprecated
        public Builder set(NamespaceID id, Shape shape) {
            ops.add(new Operation.Set(id, shape));
            return this;
        }

        /**
         * Sets a shape with the specified key.
         *
         * @param key    The key for this shape. If reused, the previous shape will be replaced.
         * @param shape The shape to associate with the key.
         * @return The builder.
         */
        public Builder set(Key key, Shape shape) {
            ops.add(new Operation.Set(key, shape));
            return this;
        }

        /**
         * Removes a shape with a specified key.
         *
         * @param key The key to remove.
         * @return The builder.
         */
        public Builder remove(String key) {
            return remove(Key.key(key));
        }

        /**
         * Use {@link #remove(Key)} instead.
         * Removes a shape with a specified namespace ID.
         *
         * @param id The namespace ID to remove.
         * @return The builder.
         */
        @Deprecated
        public Builder remove(NamespaceID id) {
            ops.add(new Operation.Remove(id));
            return this;
        }

        /**
         * Removes a shape with a specified namespace ID.
         *
         * @param key The key to remove.
         * @return The builder.
         */
        public Builder remove(Key key) {
            ops.add(new Operation.Remove(key));
            return this;
        }

        /**
         * Clears all shapes in a key.
         * @param key The key to clear.
         * @return The builder.
         */
        public Builder clear(Key key) {
            ops.add(new Operation.ClearNS(key));
            return this;
        }

        /**
         * Clears all shapes in a key.
         * @param key The key to clear.
         * @return The builder.
         */
        public Builder clear(String key) {
            ops.add(new Operation.ClearNS(Key.key(key)));
            return this;
        }

        /**
         * Clears all shapes.
         *
         * @return The builder.
         */
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
