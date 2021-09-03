package platinpython.vfxgenerator.client.gui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.Util.VoidFunction;

public class ImageSelectionWidget extends UpdateableWidget {
	private final String imageName;
	private final Consumer<List<String>> setValueFunction;
	private final Supplier<List<String>> valueSupplier;

	private boolean selected;

	public ImageSelectionWidget(int x, int y, int width, int height, String imageName, Consumer<List<String>> setValueFunction, Supplier<List<String>> valueSupplier, VoidFunction applyValueFunction) {
		super(x, y, width, height, applyValueFunction);
		this.imageName = imageName;
		this.setValueFunction = setValueFunction;
		this.valueSupplier = valueSupplier;
		this.updateValue();
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, this.selected ? 0xFFFFFFFF : 0xFF000000);
		AbstractGui.fill(matrixStack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, 0xFF000000);
		GuiUtils.drawContinuousTexturedBox(matrixStack, WIDGETS_LOCATION, this.x, this.y, 0, this.selected ? 86 : 66, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
		this.renderImage(matrixStack.last().pose(), this.x + 5, this.y + 5, this.x + this.width - 5, this.y + this.height - 5);
	}

	@SuppressWarnings("deprecation")
	private void renderImage(Matrix4f matrix, int minX, int minY, int maxX, int maxY) {
		Minecraft minecraft = Minecraft.getInstance();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(SourceFactor.SRC_COLOR, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ZERO, DestFactor.ZERO);
		minecraft.getTextureManager().bind(AtlasTexture.LOCATION_PARTICLES);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		TextureAtlasSprite sprite = minecraft.particleEngine.textureAtlas.getSprite(new ResourceLocation(VFXGenerator.MOD_ID, "particle/" + imageName));

		float u0 = sprite.getU0();
		float u1 = sprite.getU1();
		float v0 = sprite.getV0();
		float v1 = sprite.getV1();
		bufferBuilder.vertex(matrix, minX, maxY, 0F).uv(u0, v1).endVertex();
		bufferBuilder.vertex(matrix, maxX, maxY, 0F).uv(u1, v1).endVertex();
		bufferBuilder.vertex(matrix, maxX, minY, 0F).uv(u1, v0).endVertex();
		bufferBuilder.vertex(matrix, minX, minY, 0F).uv(u0, v0).endVertex();
		tessellator.end();
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (Screen.hasControlDown()) {
			this.selected = !this.selected;
			if (!this.selected) {
				this.selected = this.valueSupplier.get().size() <= 1;
			}
			ArrayList<String> list = new ArrayList<>(this.valueSupplier.get());
			if (this.selected) {
				if (!list.contains(this.imageName))
					list.add(this.imageName);
			} else {
				if (list.contains(this.imageName))
					list.remove(this.imageName);
			}
			this.setValueFunction.accept(list);
		} else {
			List<String> list = Arrays.asList(this.imageName);
			this.setValueFunction.accept(list);
			this.selected = true;
		}
	}

	@Override
	public void updateValue() {
		this.selected = this.valueSupplier.get().contains(imageName);
	}

	@Override
	protected void updateMessage() {
	}

}
