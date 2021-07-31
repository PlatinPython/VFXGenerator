package platinpython.vfxgenerator.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;

public class VFXGeneratorScreen extends Screen {
	public VFXGeneratorScreen(VFXGeneratorTileEntity tileEntity) {
		super(new TranslationTextComponent("block.vfxgenerator.vfx_generator"));
	}
	
	@Override
	protected void init() {
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
