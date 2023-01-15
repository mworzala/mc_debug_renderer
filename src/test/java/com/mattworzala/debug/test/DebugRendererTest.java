package com.mattworzala.debug.test;

import com.mattworzala.debug.client.render.ClientRenderer;
import com.mattworzala.debug.client.render.RenderLayer;
import com.mattworzala.debug.client.shape.BoxShape;
import com.mattworzala.debug.client.shape.QuadShape;
import com.mattworzala.debug.client.shape.SplineShape;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class DebugRendererTest {
    private static ClientRenderer renderer;

    public static void init(ClientRenderer renderer) {
        DebugRendererTest.renderer = renderer;

        ClientPlayConnectionEvents.JOIN.register(DebugRendererTest::handleJoinGame);
    }

    private static void handleJoinGame(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {

        var pos1 = new Vec3d(0, 0, 0);
        var pos2 = new Vec3d(5, 5, 5);
        var pos3 = new Vec3d(-5, 5, 5);

        var lineShape = new SplineShape(SplineShape.Type.CATMULL_ROM, List.of(pos1, pos2, pos3), true,
                0xFFFF0000, RenderLayer.INLINE, 4f);
//            var lineShape = new LineShape(LineShape.Type.LOOP, List.of(start, end, end2));
//            var lineShape = new LineShape(LineShape.Type.STRIP, List.of(start, end, end2));
        renderer.add(new Identifier("debug", "test_line"), lineShape);

        var quadShape = new QuadShape(
                new Vec3d(0, 0, 0),
                new Vec3d(2, 0, 0),
                new Vec3d(2, 2, 0),
                new Vec3d(0, 2, 0),
                0xFFFF0000, RenderLayer.INLINE);
        renderer.add(new Identifier("debug", "test_quad"), quadShape);

        var boxShape = new BoxShape(
                new Vec3d(10, 10, 10),
                new Vec3d(20, 20, 20),
                0xFFFF0000, RenderLayer.INLINE,
                0xFF00FF00, RenderLayer.TOP, 4f);
        renderer.add(new Identifier("debug", "test_box"), boxShape);


        var bezierShape = new SplineShape(SplineShape.Type.BEZIER, List.of(pos1, pos2, pos3, pos1), true,
                0xFFFF0000, RenderLayer.INLINE, 4f);
        renderer.add(new Identifier("debug", "test_bezier"), bezierShape);
    }

}
