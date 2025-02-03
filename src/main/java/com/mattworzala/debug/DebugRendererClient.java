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
    private static final Logger LOGGER = LogManager.getLogger();
    private final ClientRenderer renderer = new ClientRenderer();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Waiting to render debug shapes!");

        // Setup listeners
        ClientPlayConnectionEvents.JOIN.register(this::handleJoinGame);
        ClientPlayConnectionEvents.DISCONNECT.register(this::handleDisconnect);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(this::handleRenderFabulous);
        WorldRenderEvents.LAST.register(this::handleRenderLast);

        // Networking
        PayloadTypeRegistry.playC2S().register(DebugHelloPacket.PACKET_ID, DebugHelloPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(DebugShapesPacket.PACKET_ID, DebugShapesPacket.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DebugShapesPacket.PACKET_ID, this::handlePacket);
    }

    private void handleRenderFabulous(WorldRenderContext ctx) {
        if (!ctx.advancedTranslucency()) return;
        renderer.render(ctx.matrixStack(), ctx.camera());
    }

    private void handleRenderLast(WorldRenderContext ctx) {
        if (ctx.advancedTranslucency()) return;
        renderer.render(ctx.matrixStack(), ctx.camera());
    }

    private void handleJoinGame(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        sender.sendPacket(new DebugHelloPacket(PROTOCOL_VERSION));
    }

    private void handleDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        renderer.clear();
    }

    private void handlePacket(@NotNull DebugShapesPacket packet, @NotNull ClientPlayNetworking.Context context) {
        for (var operation : packet.operations()) {
            switch (operation) {
                case DebugShapesPacket.Set op -> renderer.add(op.namespaceId(), op.shape());
                case DebugShapesPacket.Remove op -> renderer.remove(op.namespaceId());
                case DebugShapesPacket.ClearNamespace op -> renderer.remove(op.namespace());
                case DebugShapesPacket.Clear ignored -> renderer.clear();
            }
        }
    }
}
