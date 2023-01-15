package com.mattworzala.debug.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Final @Shadow
    public PathfindingDebugRenderer pathfindingDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer waterDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer heightmapDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer collisionDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer neighborUpdateDebugRenderer;
    @Final @Shadow
    public StructureDebugRenderer structureDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer skyLightDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer worldGenAttemptDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer blockOutlineDebugRenderer;
    @Final @Shadow
    public DebugRenderer.Renderer chunkLoadingDebugRenderer;
    @Final @Shadow
    public VillageDebugRenderer villageDebugRenderer;
    @Final @Shadow
    public VillageSectionsDebugRenderer villageSectionsDebugRenderer;
    @Final @Shadow
    public BeeDebugRenderer beeDebugRenderer;
    @Final @Shadow
    public RaidCenterDebugRenderer raidCenterDebugRenderer;
    @Final @Shadow
    public GoalSelectorDebugRenderer goalSelectorDebugRenderer;
    @Final @Shadow
    public GameEventDebugRenderer gameEventDebugRenderer;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V", at = @At("RETURN"))
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (com.mattworzala.debug.DebugRenderer.debugPathfinders)
            pathfindingDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugWater)
            waterDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugHeightMap)
            heightmapDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugCollision)
            collisionDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugNeighbourUpdate)
            neighborUpdateDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugStructure)
            structureDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugSkyLight)
            skyLightDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugWorldGen)
            worldGenAttemptDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugBlockOutline)
            blockOutlineDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugChunkLoading)
            chunkLoadingDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugVillage)
            villageDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugVillage)
            villageSectionsDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugBee)
            beeDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugRaid)
            raidCenterDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugGoalSelector)
            goalSelectorDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (com.mattworzala.debug.DebugRenderer.debugGameEvent)
            gameEventDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);

//        ClientRenderer.getInstance().render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
