package com.mattworzala.debug.shape;

import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public interface Shape {

    static Box.Builder box() {
        return new Box.Builder();
    }

    static Line.Builder line() {
        return new Line.Builder();
    }

    static Text.Builder text() {
        return new Text.Builder();
    }

    void write(@NotNull BinaryWriter buffer);

}
