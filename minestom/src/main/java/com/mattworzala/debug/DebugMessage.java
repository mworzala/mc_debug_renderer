package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.common.PluginMessagePacket;

import java.util.ArrayList;
import java.util.List;

import static net.minestom.server.network.NetworkBuffer.STRING;
import static net.minestom.server.network.NetworkBuffer.VAR_INT;


public record DebugMessage(
        List<Operation> ops
) {


    public static Builder builder() {
        return new Builder();
    }


    public void sendTo(Player player) {
        player.sendPacket(getPacket());
    }


    public PluginMessagePacket getPacket() {
        byte[] bytes = NetworkBuffer.makeArray(buffer -> {
            buffer.write(VAR_INT, ops.size());
            for (Operation op : ops) {
                op.write(buffer);
            }
        });
        return new PluginMessagePacket("debug:shapes", bytes);
    }

    public static class Builder {

        private final List<Operation> ops = new ArrayList<>();

        public Builder set(String namespaceId, Shape shape) {
            return set(Key.key(namespaceId), shape);
        }

        public Builder set(Key key, Shape shape) {
            ops.add(new Operation.Set(key, shape));
            return this;
        }

        public Builder remove(String namespaceId) {
            return remove(Key.key(namespaceId));
        }

        public Builder remove(Key key) {
            ops.add(new Operation.Remove(key));
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

    public sealed interface Operation {
        void write(NetworkBuffer buffer);

        record Set(Key id, Shape shape) implements Operation {
            @Override
            public void write(NetworkBuffer buffer) {
                buffer.write(VAR_INT, 0);
                buffer.write(STRING, id.asString());

                shape.write(buffer);
            }
        }

        record Remove(Key id) implements Operation {
            @Override
            public void write(NetworkBuffer buffer) {
                buffer.write(VAR_INT, 1);
                buffer.write(STRING, id.asString());
            }
        }

        record ClearNS(String namespace) implements Operation {
            @Override
            public void write(NetworkBuffer buffer) {
                buffer.write(VAR_INT, 2);
                buffer.write(STRING, namespace);
            }
        }

        record Clear() implements Operation {
            @Override
            public void write(NetworkBuffer buffer) {
                buffer.write(VAR_INT, 3);
            }
        }
    }
}