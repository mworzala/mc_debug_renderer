package com.mattworzala.debug.client;

import com.mattworzala.debug.client.render.*;
import com.mattworzala.debug.client.render.shape.BoxShape;
import com.mattworzala.debug.client.render.shape.LineShape;
import com.mattworzala.debug.client.render.shape.TextShape;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class DebugRendererClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Waiting to render debug shapes!");

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier("debug", "shapes"),
                this::handleMultiOpPacket
        );

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier("debug", "shape"),
                this::handleSingleOpPacket
        );
    }

    private void handleMultiOpPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender responseSender) {
        var opCount = buffer.readVarInt();
        for (int i = 0; i < opCount; i++) {
            handleSingleOpPacket(client, handler, buffer, responseSender);
        }
    }

    private void handleSingleOpPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender responseSender) {
        ClientRenderer renderer = ClientRenderer.getInstance();
        int op = buffer.readVarInt();
        switch (op) {
            case 0 -> { // SET
                Identifier shapeId = buffer.readIdentifier();
                int shapeType = buffer.readVarInt();
                if (shapeType == 0) {
                    renderer.addShape(shapeId, new BoxShape(buffer));
                } else if (shapeType == 1) {
                    renderer.addShape(shapeId, new LineShape(buffer));
                } else if (shapeType == 2) {
                    renderer.addShape(shapeId, new TextShape(buffer));
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
