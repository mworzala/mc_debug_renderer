package com.mattworzala.debug.client.screens;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;

public class DebugRenderersGUI extends LightweightGuiDescription {
    public DebugRenderersGUI() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);

        WToggleButton button = new WToggleButton(MutableText.of(new LiteralTextContent("Pathfinders")));
        button.setToggle(com.mattworzala.debug.DebugRenderer.debugPathfinders);
        button.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugPathfinders = bool);

        WToggleButton button1 = new WToggleButton(MutableText.of(new LiteralTextContent("Water")));
        button1.setToggle(com.mattworzala.debug.DebugRenderer.debugWater);
        button1.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugWater = bool);

        WToggleButton button2 = new WToggleButton(MutableText.of(new LiteralTextContent("Height Maps")));
        button2.setToggle(com.mattworzala.debug.DebugRenderer.debugHeightMap);
        button2.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugHeightMap = bool);

        WToggleButton button3 = new WToggleButton(MutableText.of(new LiteralTextContent("Collision")));
        button3.setToggle(com.mattworzala.debug.DebugRenderer.debugCollision);
        button3.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugCollision = bool);

        WToggleButton button4 = new WToggleButton(MutableText.of(new LiteralTextContent("Neighbour Updates")));
        button4.setToggle(com.mattworzala.debug.DebugRenderer.debugNeighbourUpdate);
        button4.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugNeighbourUpdate = bool);

        WToggleButton button5 = new WToggleButton(MutableText.of(new LiteralTextContent("Structures")));
        button5.setToggle(com.mattworzala.debug.DebugRenderer.debugStructure);
        button5.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugStructure = bool);

        WToggleButton button6 = new WToggleButton(MutableText.of(new LiteralTextContent("Sky Light")));
        button6.setToggle(com.mattworzala.debug.DebugRenderer.debugSkyLight);
        button6.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugSkyLight = bool);

        WToggleButton button7 = new WToggleButton(MutableText.of(new LiteralTextContent("World Gen")));
        button7.setToggle(com.mattworzala.debug.DebugRenderer.debugWorldGen);
        button7.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugWorldGen = bool);

        WToggleButton button8 = new WToggleButton(MutableText.of(new LiteralTextContent("Block Outlines")));
        button8.setToggle(com.mattworzala.debug.DebugRenderer.debugBlockOutline);
        button8.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugBlockOutline = bool);

        WToggleButton button9 = new WToggleButton(MutableText.of(new LiteralTextContent("Chunk Loading")));
        button9.setToggle(com.mattworzala.debug.DebugRenderer.debugChunkLoading);
        button9.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugChunkLoading = bool);

        WToggleButton button10 = new WToggleButton(MutableText.of(new LiteralTextContent("Bees")));
        button10.setToggle(com.mattworzala.debug.DebugRenderer.debugBee);
        button10.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugBee = bool);

        WToggleButton button11 = new WToggleButton(MutableText.of(new LiteralTextContent("Raids")));
        button10.setToggle(com.mattworzala.debug.DebugRenderer.debugRaid);
        button10.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugRaid = bool);

        WToggleButton button12 = new WToggleButton(MutableText.of(new LiteralTextContent("Goal Selectors")));
        button10.setToggle(com.mattworzala.debug.DebugRenderer.debugGoalSelector);
        button10.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugGoalSelector = bool);

        WToggleButton button13 = new WToggleButton(MutableText.of(new LiteralTextContent("Game Events")));
        button13.setToggle(com.mattworzala.debug.DebugRenderer.debugGameEvent);
        button13.setOnToggle(bool -> com.mattworzala.debug.DebugRenderer.debugGameEvent = bool);

        root.add(button, 0, 1);
        root.add(button1, 0, 2);
        root.add(button2, 0, 3);
        root.add(button3, 0, 4);
        root.add(button4, 0, 5);
        root.add(button5, 0, 6);
        root.add(button7, 0, 7);
        root.add(button8, 0, 8);
        root.add(button9, 0, 9);
        root.add(button10, 0, 10);
        root.add(button11, 0, 11);
        root.add(button12, 0, 12);
        root.add(button13, 0, 13);

        root.validate(this);
    }
}
