package com.mattworzala.debug.render;

import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.jetbrains.annotations.NotNull;

public record RenderType(
        @NotNull VertexFormat.DrawMode drawMode,
        @NotNull VertexFormat vertexFormat,
        ShaderProgramKey shader
) {

    public static final RenderType QUADS = new RenderType(
            VertexFormat.DrawMode.QUADS,
            VertexFormats.POSITION_COLOR,
            ShaderProgramKeys.RENDERTYPE_LINES
    );

    public static final RenderType LINES = new RenderType(
            VertexFormat.DrawMode.LINES,
            VertexFormats.LINES,
            ShaderProgramKeys.RENDERTYPE_LINES
    );

    public static final RenderType LINE_STRIP = new RenderType(
            VertexFormat.DrawMode.LINE_STRIP,
            VertexFormats.LINES,
            ShaderProgramKeys.RENDERTYPE_LINES
    );

}
