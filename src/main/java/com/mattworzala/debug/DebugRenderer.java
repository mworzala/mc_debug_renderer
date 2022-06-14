package com.mattworzala.debug;

import com.mattworzala.debug.client.screens.DebugRenderersGUI;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class DebugRenderer implements ModInitializer {

    public static boolean debugPathfinders = false;
    public static boolean debugWater = false;
    public static boolean debugHeightMap = false;
    public static boolean debugCollision = false;
    public static boolean debugNeighbourUpdate = false;
    public static boolean debugStructure = false;
    public static boolean debugSkyLight = false;
    public static boolean debugWorldGen = false;
    public static boolean debugBlockOutline = false;
    public static boolean debugChunkLoading = false;
    public static boolean debugVillage = false;
    public static boolean debugBee = false;
    public static boolean debugRaid = false;
    public static boolean debugGoalSelector = false;
    public static boolean debugGameEvent = false;

    private static final KeyBinding GUI_KEY_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("debug.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "debug.category"));

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (GUI_KEY_BIND.wasPressed() && !client.isPaused()) {
                client.setScreen(new CottonClientScreen(new LiteralText("Debug"), new DebugRenderersGUI()));
            }
        });
    }
}
