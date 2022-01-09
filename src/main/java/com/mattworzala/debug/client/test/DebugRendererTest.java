package com.mattworzala.debug.client.test;

import com.mattworzala.debug.client.render.*;
import com.mattworzala.debug.client.render.shape.BoxShape;
import com.mattworzala.debug.client.render.shape.LineShape;
import com.mattworzala.debug.client.render.shape.TextShape;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class DebugRendererTest {

    private static final DebugRendererTest instance = new DebugRendererTest();

    public static DebugRendererTest getInstance() {
        return instance;
    }

    private DebugRendererTest() {}

    public void onWorldLoad() {
        if (false) return;

        // Box
        ClientRenderer.getInstance().addShape(
                new Identifier("test", "box"),
                new BoxShape(
                        20.9, -59.1, 20.9,
                        22.1, -57.9, 22.1,
                        0x80FF0000,
                        RenderLayer.INLINE
                ));

        // Line
        ClientRenderer.getInstance().addShape(
                new Identifier("test", "line"),
                new LineShape(
                        List.of(
                                new Vec3d(17, -60, 20),
                                new Vec3d(14, -57, 23),
                                new Vec3d(15, -59, 21)
                        ),
                        2f,
                        0xFF00FF00,
                        RenderLayer.TOP
                ));

        // Text
        ClientRenderer.getInstance().addShape(
                new Identifier("test", "text"),
                new TextShape(
                        9.5, -59, 21.5,
                        "Hello, world!",
                        0xFF0000FF,
                        0.04f,
                        RenderLayer.INLINE
                )
        );
    }
}
