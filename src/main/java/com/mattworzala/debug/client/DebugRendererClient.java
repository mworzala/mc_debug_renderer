package com.mattworzala.debug.client;

import com.mattworzala.debug.client.render.ClientRenderer;
import com.mattworzala.debug.client.render.Shape;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class DebugRendererClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Waiting to render debug shapes!");

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier("debug", "shapes"),
                this::handleDebugRenderPacket
        );
    }

    private void handleDebugRenderPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender responseSender) {
        ClientRenderer renderer = ClientRenderer.getInstance();
        var opCount = buffer.readVarInt();
        for (int i = 0; i < opCount; i++) {
            int op = buffer.readVarInt();
            switch (op) {
                case 0 -> { // SET
                    Identifier shapeId = buffer.readIdentifier();
                    int shapeType = buffer.readVarInt();
                    if (shapeType == 0) {
                        renderer.addShape(shapeId, new Shape.Box(buffer));
                    } else if (shapeType == 1) {
                        renderer.addShape(shapeId, new Shape.Line(buffer));
                    }
                }
                case 1 -> { // REMOVE
                    Identifier targetShape = buffer.readIdentifier();
                    renderer.removeShape(targetShape);
                }
                case 2 -> { // CLEAR_NS
                    String targetNamespace = buffer.readString(32767);
                    renderer.removeShapes(targetNamespace);
                }
                case 3 -> { // CLEAR
                    renderer.removeAllShapes();
                }
            }
        }
    }
}
