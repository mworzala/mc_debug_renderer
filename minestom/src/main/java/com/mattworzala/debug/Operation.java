package com.mattworzala.debug;

import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.key.Key;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public interface Operation {

    record Set(
            Key key,
            Shape shape
    ) implements Operation {
        public static final NetworkBuffer.Type<Set> SERIALIZER = NetworkBufferTemplate.template(
                KeyType.KEY, Set::key,
                SimplePacketRegistry.SHAPE_SERIALIZER, Set::shape,
                Set::new
        );

        @Deprecated
        public Set(@NotNull NamespaceID namespace, Shape shape) {
            this(namespace.key(), shape);
        }

        @Deprecated
        public NamespaceID id() {
            return NamespaceID.from(key);
        }
    }

    record Remove(
            @NotNull Key key
    ) implements Operation {
        public static final NetworkBuffer.Type<Remove> SERIALIZER = NetworkBufferTemplate.template(
                KeyType.KEY, Remove::key,
                Remove::new
        );

        @Deprecated
        public Remove(@NotNull NamespaceID namespace) {
            this(namespace.key());
        }

        @Deprecated
        public NamespaceID id() {
            return NamespaceID.from(key);
        }
    }

    record ClearNS(
            @NotNull Key key
    ) implements Operation {
        public static final NetworkBuffer.Type<ClearNS> SERIALIZER = NetworkBufferTemplate.template(
                KeyType.KEY, ClearNS::key,
                ClearNS::new
        );

        @Deprecated
        public ClearNS(@NotNull String namespace) {
            this(Key.key(namespace));
        }

        @Deprecated
        public @NotNull String namespace() {
            return key.asString();
        }
    }

    record Clear() implements Operation {
        public static final NetworkBuffer.Type<Clear> SERIALIZER = NetworkBufferTemplate.template(Clear::new);
    }
}
