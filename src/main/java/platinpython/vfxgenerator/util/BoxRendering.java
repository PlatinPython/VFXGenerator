package platinpython.vfxgenerator.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11C;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;
import platinpython.vfxgenerator.util.data.ParticleData;

@Mod.EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BoxRendering {
    public static BlockPos currentRenderPos;

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (currentRenderPos == null) return;
        TileEntity tileEntity = Minecraft.getInstance().level.getBlockEntity(currentRenderPos);
        if (!(tileEntity instanceof VFXGeneratorTileEntity)) return;
        ParticleData particleData = ((VFXGeneratorTileEntity) tileEntity).getParticleData();

        MatrixStack matrixStack = event.getMatrixStack();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        matrixStack.pushPose();
        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        Matrix4f matrix = matrixStack.last().pose();

        Vector3f pos = new Vector3f(Vector3d.atCenterOf(currentRenderPos));

        IVertexBuilder builder = buffer.getBuffer(BoxRenderType.TRANSLUCENT_NO_CULL);
        renderBoxSides(builder, matrix, pos.x() + particleData.getSpawnXBot(), pos.y() + particleData.getSpawnYBot(),
                       pos.z() + particleData.getSpawnZBot(), pos.x() + particleData.getSpawnXTop(),
                       pos.y() + particleData.getSpawnYTop(), pos.z() + particleData.getSpawnZTop()
        );
        buffer.endBatch(BoxRenderType.TRANSLUCENT_NO_CULL);

        builder = buffer.getBuffer(BoxRenderType.LINES);
        renderBoxEdges(builder, matrix, pos.x() + particleData.getSpawnXBot(), pos.y() + particleData.getSpawnYBot(),
                       pos.z() + particleData.getSpawnZBot(), pos.x() + particleData.getSpawnXTop(),
                       pos.y() + particleData.getSpawnYTop(), pos.z() + particleData.getSpawnZTop()
        );
        buffer.endBatch(BoxRenderType.LINES);

        builder = buffer.getBuffer(BoxRenderType.LINES_LIGHTMAP);
        renderBoxEdgesFullbright(builder, matrix, pos);
        buffer.endBatch(BoxRenderType.LINES_LIGHTMAP);

        matrixStack.popPose();
    }

    private static void renderBoxSides(IVertexBuilder builder, Matrix4f matrix, float minX, float minY, float minZ,
                                       float maxX, float maxY, float maxZ) {
        int red = 0;
        int green = 128;
        int blue = 128;
        int alpha = 128;

        // Top side
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        // Bottom side
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        // North side
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        // East side
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        // South side
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        // West side
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
    }

    private static void renderBoxEdges(IVertexBuilder builder, Matrix4f matrix, float minX, float minY, float minZ,
                                       float maxX, float maxY, float maxZ) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 255;

        // West side
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();

        // East side
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        // South side (don't repeat the vertical lines that are done by the east/west sides)
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    }

    private static void renderBoxEdgesFullbright(IVertexBuilder builder, Matrix4f matrix, Vector3f center) {
        float minX = center.x() - 0.5001F;
        float minY = center.y() - 0.5001F;
        float minZ = center.z() - 0.5001F;
        float maxX = center.x() + 0.5001F;
        float maxY = center.y() + 0.5001F;
        float maxZ = center.z() + 0.5001F;

        int red = 255;
        int green = 0;
        int blue = 0;
        int alpha = 255;

        // West side
        builder.vertex(matrix, minX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, minX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, minX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, minX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        // East side
        builder.vertex(matrix, maxX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, maxX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, maxX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, maxX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        builder.vertex(matrix, maxX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, minY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, minX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, maxY, minZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        // South side (don't repeat the vertical lines that are done by the east/west sides)
        builder.vertex(matrix, minX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, maxX, minY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();

        builder.vertex(matrix, maxX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
        builder.vertex(matrix, minX, maxY, maxZ)
               .color(red, green, blue, alpha)
               .uv2(LightTexture.pack(15, 15))
               .endVertex();
    }

    private static class BoxRenderType extends RenderType {

        public BoxRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_,
                             boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
            super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_,
                  p_i225992_8_
            );
        }

        public static final RenderType TRANSLUCENT_NO_CULL = RenderType.create(
                Util.createNamespacedResourceLocation("translucent_no_cull").toString(),
                DefaultVertexFormats.POSITION_COLOR, GL11C.GL_QUADS, 256, RenderType.State.builder()
                                                                                          .setLayeringState(
                                                                                                  LayerState.VIEW_OFFSET_Z_LAYERING)
                                                                                          .setTransparencyState(
                                                                                                  RenderState.TRANSLUCENT_TRANSPARENCY)
                                                                                          .setTextureState(
                                                                                                  RenderState.NO_TEXTURE)
                                                                                          .setDepthTestState(
                                                                                                  RenderState.NO_DEPTH_TEST)
                                                                                          .setCullState(
                                                                                                  RenderState.NO_CULL)
                                                                                          .setLightmapState(
                                                                                                  RenderState.NO_LIGHTMAP)
                                                                                          .setWriteMaskState(
                                                                                                  RenderState.COLOR_WRITE)
                                                                                          .createCompositeState(false)
        );

        public static final RenderType LINES = RenderType.create(
                Util.createNamespacedResourceLocation("lines").toString(), DefaultVertexFormats.POSITION_COLOR,
                GL11C.GL_LINES, 256, RenderType.State.builder()
                                                     .setLineState(LineState.DEFAULT_LINE)
                                                     .setLayeringState(LayerState.VIEW_OFFSET_Z_LAYERING)
                                                     .setTransparencyState(RenderState.NO_TRANSPARENCY)
                                                     .setTextureState(RenderState.NO_TEXTURE)
                                                     .setDepthTestState(RenderState.NO_DEPTH_TEST)
                                                     .setCullState(RenderState.CULL)
                                                     .setLightmapState(RenderState.NO_LIGHTMAP)
                                                     .setWriteMaskState(RenderState.COLOR_WRITE)
                                                     .createCompositeState(false)
        );

        public static final RenderType LINES_LIGHTMAP = RenderType.create(
                Util.createNamespacedResourceLocation("lines_lightmap").toString(),
                DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11C.GL_LINES, 256, RenderType.State.builder()
                                                                                                   .setLineState(
                                                                                                           LineState.DEFAULT_LINE)
                                                                                                   .setLayeringState(
                                                                                                           LayerState.VIEW_OFFSET_Z_LAYERING)
                                                                                                   .setTransparencyState(
                                                                                                           RenderState.NO_TRANSPARENCY)
                                                                                                   .setTextureState(
                                                                                                           RenderState.NO_TEXTURE)
                                                                                                   .setDepthTestState(
                                                                                                           RenderState.NO_DEPTH_TEST)
                                                                                                   .setCullState(
                                                                                                           RenderState.NO_CULL)
                                                                                                   .setLightmapState(
                                                                                                           RenderState.LIGHTMAP)
                                                                                                   .setWriteMaskState(
                                                                                                           RenderState.COLOR_WRITE)
                                                                                                   .createCompositeState(
                                                                                                           false)
        );
    }
}