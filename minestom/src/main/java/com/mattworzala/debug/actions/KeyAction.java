package com.mattworzala.debug.actions;

import net.minestom.server.entity.Player;

import java.util.function.Consumer;

// https://www.glfw.org/docs/3.3/group__keys.html
public record KeyAction(int keyCode, Consumer<Player> action) {
}
