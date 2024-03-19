package com.mattworzala.debug.client.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record RenderType(
        @NotNull VertexFormat.DrawMode drawMode,
        @NotNull VertexFormat vertexFormat,
        @NotNull Supplier<ShaderProgram> shader
) {

    public static final RenderType QUADS = new RenderType(
            VertexFormat.DrawMode.QUADS,
            VertexFormats.POSITION_COLOR,
            GameRenderer::getPositionColorProgram
    );

    public static final RenderType LINES = new RenderType(
            VertexFormat.DrawMode.LINES,
            VertexFormats.LINES,
            GameRenderer::getRenderTypeLinesProgram
    );

    public static final RenderType LINE_STRIP = new RenderType(
            VertexFormat.DrawMode.LINE_STRIP,
            VertexFormats.LINES,
            GameRenderer::getRenderTypeLinesProgram
    );

}
