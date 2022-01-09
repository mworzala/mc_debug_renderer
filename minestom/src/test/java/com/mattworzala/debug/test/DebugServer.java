package com.mattworzala.debug.test;

import com.mattworzala.debug.DebugMessage;
import com.mattworzala.debug.shape.Shape;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DebugServer {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new FlatGenerator());

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();

            DebugMessage.builder()
                    .set("test:box", Shape.box()
                            .start(new Vec(10, 40, 10))
                            .end(new Vec(15, 45, 15))
                            .build())
                    .set("test:line", Shape.line()
                            .point(new Vec(10, 40, 10))
                            .point(new Vec(15, 45, -15))
                            .build())
                    .set("test:text", Shape.text()
                            .position(new Vec(5, 40, 5))
                            .content("Hello World!")
                            .build())
                    .build()
                    .sendTo(player);
        });
        globalEventHandler.addListener(PlayerPluginMessageEvent.class, event -> {
            if (!event.getIdentifier().equals("debug:enabled"))
                return;

            System.out.println(event.getPlayer().getUsername() + " has debug rendering enabled!");
        });

        minecraftServer.start("localhost", 25565);
    }

    private static class FlatGenerator implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
            // Set chunk blocks
            for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    for (byte y = 0; y < 40; y++) {
                        batch.setBlock(x, y, z, Block.STONE);
                    }
                }
            }
        }

        @Override
        public List<ChunkPopulator> getPopulators() {
            return null;
        }
    }
}
