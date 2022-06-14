package com.mattworzala.debug.actions;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.packet.server.play.PluginMessagePacket;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.binary.BinaryWriter;

import java.nio.ByteBuffer;
import java.util.*;

public class KeyActionRegistry {

    private final Map<Integer, KeyAction> actions = new HashMap<>();

    public void register(KeyAction action) {
        this.actions.put(action.keyCode(), action);
    }

    public void processPluginMessage(PlayerPluginMessageEvent event) {
        if (!event.getIdentifier().equals("debug:key_action")) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.wrap(event.getMessage());
        int keyCode = buffer.getInt();

        KeyAction action = this.actions.get(keyCode);
        if (action != null) {
            action.action().accept(event.getPlayer());
        }
    }

    public void sendPacket(Audience audience) {
        PacketUtils.sendPacket(audience, getPacket());
    }

    private PluginMessagePacket getPacket() {
        BinaryWriter writer = new BinaryWriter(1024);
        writer.writeVarInt(actions.size());
        for (int keyCode : actions.keySet()) {
            writer.write(keyCode);
        }
        return new PluginMessagePacket("debug:key_actions", writer.toByteArray());
    }
}
