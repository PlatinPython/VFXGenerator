package platinpython.vfxgenerator.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11C;
import platinpython.vfxgenerator.util.Util;

import java.util.Collections;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImageSelectionWidget extends UpdateableWidget {
    private final ResourceLocation imageLocation;
    private final Consumer<TreeSet<ResourceLocation>> setValueFunction;
    private final Supplier<TreeSet<ResourceLocation>> valueSupplier;

    private boolean selected;

    public ImageSelectionWidget(int x, int y, int width, int height, ResourceLocation imageLocation,
                                Consumer<TreeSet<ResourceLocation>> setValueFunction,
                                Supplier<TreeSet<ResourceLocation>> valueSupplier, Runnable applyValueFunction) {
        super(x, y, width, height, applyValueFunction);
        this.imageLocation = imageLocation;
        this.setValueFunction = setValueFunction;
        this.valueSupplier = valueSupplier;
        this.updateValue();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height,
                         this.selected ? 0xFFFFFFFF : 0xFF000000
        );
        AbstractGui.fill(matrixStack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1,
                         0xFF000000
        );
        GuiUtils.drawContinuousTexturedBox(matrixStack, WIDGETS_LOCATION, this.x, this.y, 0, this.selected ? 86 : 66,
                                           this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset()
        );
        this.renderImage(matrixStack.last().pose(), this.x + 5, this.y + 5, this.x + this.width - 5,
                         this.y + this.height - 5
        );
    }

    @SuppressWarnings("deprecation")
    private void renderImage(Matrix4f matrix, int minX, int minY, int maxX, int maxY) {
        Minecraft minecraft = Minecraft.getInstance();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_COLOR, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ZERO,
                                       DestFactor.ZERO
        );
        minecraft.getTextureManager().bind(AtlasTexture.LOCATION_PARTICLES);
        bufferBuilder.begin(GL11C.GL_LINES, DefaultVertexFormats.POSITION_TEX);
        TextureAtlasSprite sprite = minecraft.particleEngine.textureAtlas.getSprite(imageLocation);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        bufferBuilder.vertex(matrix, minX, maxY, 0F).uv(u0, v1).color(1F, 0F, 0F, 1F).endVertex();
        bufferBuilder.vertex(matrix, maxX, maxY, 0F).uv(u1, v1).color(1F, 0F, 0F, 1F).endVertex();
        bufferBuilder.vertex(matrix, maxX, minY, 0F).uv(u1, v0).color(1F, 0F, 0F, 1F).endVertex();
        bufferBuilder.vertex(matrix, minX, minY, 0F).uv(u0, v0).color(1F, 0F, 0F, 1F).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (Screen.hasControlDown()) {
            this.selected = !this.selected;
            if (!this.selected) {
                this.selected = this.valueSupplier.get().size() <= 1;
            }
            TreeSet<ResourceLocation> set = this.valueSupplier.get();
            if (this.selected) {
                set.add(this.imageLocation);
            } else {
                set.remove(this.imageLocation);
            }
            this.setValueFunction.accept(set);
        } else {
            TreeSet<ResourceLocation> list = Util.createTreeSetFromCollectionWithComparator(
                    Collections.singletonList(this.imageLocation), ResourceLocation::compareNamespaced);
            this.setValueFunction.accept(list);
            this.selected = true;
        }
    }

    @Override
    public void updateValue() {
        this.selected = this.valueSupplier.get().contains(imageLocation);
    }

    @Override
    protected void updateMessage() {
    }

}
