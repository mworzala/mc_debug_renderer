package com.mattworzala.debug.demo;

import com.mattworzala.debug.DebugMessage;
import com.mattworzala.debug.Layer;
import com.mattworzala.debug.shape.LineShape;
import com.mattworzala.debug.shape.Shape;
import com.mattworzala.debug.shape.SplineShape;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class DemoServer {

    public static void main(String[] args) {
        var server = MinecraftServer.init();

        var instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(-10, -1, Block.STONE));
        instance.setChunkSupplier(LightingChunk::new);

        // Register a command to trigger the debug shapes
        var debugCommand = new Command("democmd");
        debugCommand.setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                player.sendMessage("Sending debug shapes!");
                sendTestShapes(player);
            } else {
                sender.sendMessage("This command can only be used by a player.");
            }
        });
        MinecraftServer.getCommandManager().register(debugCommand);


        MinecraftServer.getGlobalEventHandler()
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    event.setSpawningInstance(instance);
                    event.getPlayer().setRespawnPoint(new Pos(0, 20, 0));
                })
                .addListener(PlayerSpawnEvent.class, event -> {
                    var player = event.getPlayer();
                    player.setGameMode(GameMode.CREATIVE);
                    player.setPermissionLevel(4);

                    // Send shapes when player first joins
                    if (event.isFirstSpawn()) {
                        sendTestShapes(player);
                    }
                });

        server.start("0.0.0.0", 25565);
    }

    /**
     * Builds and sends a collection of test shapes to the player.
     * @param player The player to send the shapes to.
     */
    public static void sendTestShapes(Player player) {
        Pos playerPos = player.getPosition();

        DebugMessage.builder()
                // Clear any previous shapes
                .clear("debug")

                // A large box relative to the player
                .set("debug:test_box", Shape.box()
                        .start(playerPos.add(5, 0, 5))
                        .end(playerPos.add(15, 10, 15))
                        .faceColor(0x66FF0000)
                        .faceLayer(Layer.MIXED)
                        .edgeColor(0xFF00FF00)
                        .edgeLayer(Layer.TOP)
                        .edgeWidth(4f)
                        .build())

                // A quad in front of the player
                .set("debug:test_quad", Shape.quad()
                        .a(playerPos.add(-2, 0, -5))
                        .b(playerPos.add(2, 0, -5))
                        .c(playerPos.add(2, 4, -5))
                        .d(playerPos.add(-2, 4, -5))
                        .color(0xAAFF00FF)
                        .renderLayer(Layer.MIXED)
                        .build())

                // A line strip
                .set("debug:test_line", Shape.line()
                        .type(LineShape.Type.STRIP)
                        .point(playerPos.withY(playerPos.y() + 10).add(-10, 0, 0))
                        .point(playerPos.withY(playerPos.y() + 15))
                        .point(playerPos.withY(playerPos.y() + 10).add(10, 0, 0))
                        .color(0xFFFFFFFF)
                        .lineWidth(5f)
                        .layer(Layer.TOP)
                        .build())

                // A bezier spline
                .set("debug:test_bezier", Shape.spline()
                        .type(SplineShape.Type.BEZIER)
                        .point(new Vec(-10, 50, -10))
                        .point(new Vec(0, 60, 0))
                        .point(new Vec(10, 40, -10))
                        .point(new Vec(0, 50, 10))
                        .color(0xFFFF8800)
                        .lineWidth(2f)
                        .layer(Layer.INLINE)
                        .build())

                // A catmull-rom spline
                .set("debug:test_catmull_rom", Shape.spline()
                        .type(SplineShape.Type.CATMULL_ROM)
                        .point(playerPos.add(0, 0, 15))
                        .point(playerPos.add(5, 5, 15))
                        .point(playerPos.add(-5, 5, 15))
                        .color(0xFF00FFFF)
                        .lineWidth(4f)
                        .layer(Layer.TOP)
                        .build())

                // Send the final message
                .build()
                .sendTo(player);
    }
}