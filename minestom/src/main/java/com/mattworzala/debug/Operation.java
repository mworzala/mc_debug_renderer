package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public interface Operation {

    void write(@NotNull BinaryWriter buffer);

    record Set(
            NamespaceID id,
            Shape shape
    ) implements Operation {
        private static final int ID = 0;

        @Override
        public void write(@NotNull BinaryWriter buffer) {
            buffer.writeVarInt(ID);
            buffer.writeSizedString(id.asString());
            shape.write(buffer);
        }
    }

    record Remove(
            @NotNull NamespaceID id
    ) implements Operation {
        private static final int ID = 1;

        @Override
        public void write(@NotNull BinaryWriter buffer) {
            buffer.writeVarInt(ID);
            buffer.writeSizedString(id.asString());
        }
    }

    record ClearNS(
            @NotNull String namespace
    ) implements Operation {
        private static final int ID = 2;

        @Override
        public void write(@NotNull BinaryWriter buffer) {
            buffer.writeVarInt(ID);
            buffer.writeSizedString(namespace);
        }
    }

    final class Clear implements Operation {
        private static final int ID = 3;

        @Override
        public void write(@NotNull BinaryWriter buffer) {
            buffer.writeVarInt(ID);
        }
    }


}
