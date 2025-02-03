package com.mattworzala.debug;

import com.mattworzala.debug.shape.*;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

// Super simple packet registry for packet writing, so we don't have to use the more complex one.
sealed interface SimplePacketRegistry<T> {
    <E extends T> Entry<E> getEntry(T type);

    // Since we are wrapping the registry we can also force serializers to be implemented.
    default NetworkBuffer.Type<T> serializer() {
        return new NetworkBuffer.Type<>() {
            @Override
            public void write(@NotNull NetworkBuffer buffer, T value) {
                var entry = getEntry(value);
                buffer.write(NetworkBuffer.VAR_INT, entry.id());
                buffer.write(entry.type(), value);
            }

            @Override
            public T read(@NotNull NetworkBuffer buffer) {
                throw new UnsupportedOperationException("Reading is unsupported in the SimplePacketRegistry");
            }
        };
    }

    // Packet ID's are VAR_INT
    record Entry<T>(int id, NetworkBuffer.Type<T> type) {}

    // Registry Impl
    SimplePacketRegistry<Operation> OPERATION_REGISTRY = new OperationRegistry();
    SimplePacketRegistry<Shape> SHAPE_REGISTRY = new ShapeRegistry();

    // Serializers
    NetworkBuffer.Type<Operation> OPERATION_SERIALIZER = OPERATION_REGISTRY.serializer();
    NetworkBuffer.Type<Shape> SHAPE_SERIALIZER = SHAPE_REGISTRY.serializer();

    //TODO: Update semantics to JDK22 when available.
    record OperationRegistry() implements SimplePacketRegistry<Operation> {
        @Override
        @SuppressWarnings("unchecked")
        public <E extends Operation> Entry<E> getEntry(Operation operation) {
            return (Entry<E>) switch (operation) {
                case Operation.Set ignored -> new Entry<>(0, Operation.Set.SERIALIZER);
                case Operation.Remove ignored -> new Entry<>(1, Operation.Remove.SERIALIZER);
                case Operation.ClearNS ignored -> new Entry<>(2, Operation.ClearNS.SERIALIZER);
                case Operation.Clear ignored -> new Entry<>(3, Operation.Clear.SERIALIZER);
                default -> throw new UnsupportedOperationException("Unsupported operation: " + operation);
            };
        }
    }

    record ShapeRegistry() implements SimplePacketRegistry<Shape> {
        @Override
        @SuppressWarnings("unchecked")
        public <E extends Shape> Entry<E> getEntry(Shape shape) {
            return (Entry<E>) switch (shape) {
                case LineShape ignored -> new Entry<>(0, LineShape.SERIALIZER);
                case SplineShape ignored -> new Entry<>(1, SplineShape.SERIALIZER);
                case QuadShape ignored -> new Entry<>(2, QuadShape.SERIALIZER);
                case BoxShape ignored -> new Entry<>(3, BoxShape.SERIALIZER);
                default -> throw new UnsupportedOperationException("Unsupported shape: " + shape);
            };
        }
    }
}
