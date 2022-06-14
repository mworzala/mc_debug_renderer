package com.mattworzala.debug.client.actions;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KeyActions {

    private static final List<Integer> keyCodes = new ArrayList<>();

    public static void register(int keyCode) {
        keyCodes.add(keyCode);
    }

    public static void clear() {
        keyCodes.clear();
    }

    public static void process(int keyCode) {
        if (keyCodes.contains(keyCode)) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(keyCode);

            ClientPlayNetworking.send(
                    new Identifier("debug", "key_action"),
                    buf
            );
        }
    }
}
