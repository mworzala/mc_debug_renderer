package com.mattworzala.debug.shape;

import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * A shape that can be rendered.
 * New shapes cannot be added without a rendered also being added to the client-side mod.
 */
@SuppressWarnings("UnstableApiUsage")
public interface Shape {

    /**
     * @return A new {@link BoxShape.Builder}.
     */
    static BoxShape.Builder box() {
        return new BoxShape.Builder();
    }

    /**
     * @return A new {@link LineShape.Builder}.
     */
    static LineShape.Builder line() {
        return new LineShape.Builder();
    }

    static QuadShape.Builder quad() {
        return new QuadShape.Builder();
    }

    static SplineShape.Builder spline() {
        return new SplineShape.Builder();
    }

    int id();

    void write(@NotNull NetworkBuffer buffer);

}
