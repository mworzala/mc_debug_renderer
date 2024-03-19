package com.mattworzala.debug.demo;

import com.mattworzala.debug.DebugMessage;
import com.mattworzala.debug.Layer;
import com.mattworzala.debug.shape.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.intellij.lang.annotations.Identifier;

import java.util.List;

public class DemoServer {
    public static void main(String[] args) {
        var server = MinecraftServer.init();

        var instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(-10, -1, Block.STONE));
        instance.setChunkSupplier(LightingChunk::new);

        MinecraftServer.getGlobalEventHandler()
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    event.setSpawningInstance(instance);
                    event.getPlayer().setRespawnPoint(new Pos(0, 0, 0));
                })
                .addListener(PlayerSpawnEvent.class, event -> {
                    var player = event.getPlayer();
                    player.setGameMode(GameMode.CREATIVE);
                    player.setPermissionLevel(4);

                    DebugMessage.builder()
                            .set("debug:test_box", Shape.box()
                                    .start(new Vec(10, 10, 10))
                                    .end(new Vec(20, 20, 20))
                                    .faceColor(0x66FF0000)
                                    .faceLayer(Layer.MIXED)
                                    .edgeColor(0xFF00FF00)
                                    .edgeLayer(Layer.TOP)
                                    .edgeWidth(4f)
                                    .build())
                            .set("debug:test_quad", Shape.quad()
                                    .a(new Vec(0, 0, 0))
                                    .b(new Vec(2, 0, 0))
                                    .c(new Vec(2, 2, 0))
                                    .d(new Vec(0, 2, 0))
                                    .color(0xFFFF0000)
                                    .renderLayer(Layer.MIXED)
                                    .build())
//                            .set("debug:test_line", Shape.line()
//                                    .type(LineShape.Type.STRIP)
//                                    .point(new Vec(0, 0, 0))
//                                    .point(new Vec(5, 5, 5))
//                                    .point(new Vec(-5, 5, 5))
//                                    .color(0xFFFF0000)
//                                    .lineWidth(4f)
//                                    .layer(Layer.MIXED)
//                                    .build())
                            .set("debug:test_bezier", Shape.spline()
                                    .type(SplineShape.Type.BEZIER)
                                    .point(new Vec(0, 0, 0))
                                    .point(new Vec(5, 5, 5))
                                    .point(new Vec(-5, 5, 5))
                                    .color(0xFFFF0000)
                                    .lineWidth(4f)
                                    .layer(Layer.MIXED)
                                    .build())
                            .set("debug:test_catmull_rom", Shape.spline()
                                    .type(SplineShape.Type.CATMULL_ROM)
                                    .point(new Vec(0, 0, 0))
                                    .point(new Vec(5, 5, 5))
                                    .point(new Vec(-5, 5, 5))
                                    .color(0xFFFF0000)
                                    .lineWidth(4f)
                                    .layer(Layer.MIXED)
                                    .build())
                            .build()
                            .sendTo(player);

                });

        server.start("0.0.0.0", 25565);
    }
}
