package com.mattworzala.debug.shape;

import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

/**
 * A shape that can be rendered.
 * New shapes cannot be added without a rendered also being added to the client-side mod.
 */
public interface Shape {

    /**
     * @return A new {@link Box.Builder}.
     */
    static Box.Builder box() {
        return new Box.Builder();
    }

    /**
     * @return A new {@link Line.Builder}.
     */
    static Line.Builder line() {
        return new Line.Builder();
    }

    /**
     * @return A new {@link Text.Builder}.
     */
    static Text.Builder text() {
        return new Text.Builder();
    }

    void write(@NotNull BinaryWriter buffer);

}
