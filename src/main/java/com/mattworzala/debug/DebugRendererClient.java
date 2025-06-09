package com.mattworzala.debug;

import com.mattworzala.debug.network.DebugHelloPacket;
import com.mattworzala.debug.network.DebugShapesPacket;
import com.mattworzala.debug.render.ClientRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class DebugRendererClient implements ClientModInitializer {
    public static final int PROTOCOL_VERSION = 2;
    private static final Logger LOGGER = LogManager.getLogger("DebugRenderer"); 
    private final ClientRenderer renderer = new ClientRenderer();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Debug Renderer Client Initializing...");

        
        ClientPlayConnectionEvents.JOIN.register(this::handleJoinGame);
        ClientPlayConnectionEvents.DISCONNECT.register(this::handleDisconnect);

        
        WorldRenderEvents.LAST.register(this::onRenderWorld);

        
        PayloadTypeRegistry.playC2S().register(DebugHelloPacket.PACKET_ID, DebugHelloPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(DebugShapesPacket.PACKET_ID, DebugShapesPacket.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DebugShapesPacket.PACKET_ID, this::handlePacket);

        LOGGER.info("Debug Renderer Client Initialized!");
    }

    private void onRenderWorld(WorldRenderContext ctx) {
        
        
        
        

        renderer.render(ctx.matrixStack(), ctx.camera());
    }

    private void handleJoinGame(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        
        LOGGER.info("Joined server, sending Hello Packet (protocol version: " + PROTOCOL_VERSION + ")");
        sender.sendPacket(new DebugHelloPacket(PROTOCOL_VERSION));
    }

    private void handleDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        
        LOGGER.info("Disconnected from server, clearing all shapes.");
        renderer.clear();
    }

    private void handlePacket(@NotNull DebugShapesPacket packet, @NotNull ClientPlayNetworking.Context context) {
        
        
        LOGGER.info("Received DebugShapesPacket! Processing " + packet.operations().size() + " operations...");

        for (var operation : packet.operations()) {
            switch (operation) {
                case DebugShapesPacket.Set op -> {
                    
                    LOGGER.info("  -> Set shape: " + op.namespaceId());
                    renderer.add(op.namespaceId(), op.shape());
                }
                case DebugShapesPacket.Remove op -> {
                    
                    LOGGER.info("  -> Remove shape: " + op.namespaceId());
                    renderer.remove(op.namespaceId());
                }
                case DebugShapesPacket.ClearNamespace op -> {
                    
                    LOGGER.info("  -> Clear namespace: " + op.namespace());
                    renderer.remove(op.namespace());
                }
                case DebugShapesPacket.Clear op -> {
                    
                    LOGGER.info("  -> Clear ALL shapes");
                    renderer.clear();
                }
            }
        }
    }
}