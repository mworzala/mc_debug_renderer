package com.mattworzala.debug;

import net.kyori.adventure.key.Key;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

// Used to serialize Key as there is no built-in packet type for it yet.
@ApiStatus.Internal
record KeyType() implements NetworkBuffer.Type<Key> {
    public static final NetworkBuffer.Type<Key> KEY = new KeyType();

    @Override
    public void write(@NotNull NetworkBuffer buffer, Key value) {
        buffer.write(NetworkBuffer.STRING, value.asString());
    }

    @Override
    public Key read(@NotNull NetworkBuffer buffer) {
        return Key.key(buffer.read(NetworkBuffer.STRING));
    }
}
