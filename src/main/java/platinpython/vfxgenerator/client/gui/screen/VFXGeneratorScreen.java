package platinpython.vfxgenerator.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import platinpython.vfxgenerator.client.gui.widget.FloatRangeSlider;
import platinpython.vfxgenerator.client.gui.widget.FloatSlider;
import platinpython.vfxgenerator.client.gui.widget.UpdateableWidget;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Constants.ParticleConstants;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDataSyncPKT;

public class VFXGeneratorScreen extends Screen {
	public final VFXGeneratorTileEntity tileEntity;

	public VFXGeneratorScreen(VFXGeneratorTileEntity tileEntity) {
		super(new TranslationTextComponent("block.vfxgenerator.vfx_generator"));
		this.tileEntity = tileEntity;
	}

	@Override
	protected void init() {
		// For testing
		FloatRangeSlider rangeSlider = new FloatRangeSlider(this.width / 2 - 165, this.height / 2 - 10, 330, 20, ClientUtils.getGuiTranslationTextComponent("particleSize"), ParticleConstants.MIN_SIZE, ParticleConstants.MAX_SIZE, .5F, this.tileEntity::setParticleSizeBot, this.tileEntity::setParticleSizeTop, this.tileEntity::getParticleSizeBot, this.tileEntity::getParticleSizeTop, this::sendToServer);

		addButton(rangeSlider);

		FloatSlider slider = new FloatSlider(this.width / 2 - 165, this.height / 2 - 10 + 25, 330, 20, ClientUtils.getGuiTranslationTextComponent("particleDelay"), ParticleConstants.MIN_DELAY, ParticleConstants.MAX_DELAY, 1F, (value) -> this.tileEntity.setParticleDelay(value.intValue()), () -> (float) this.tileEntity.getParticleDelay(), this::sendToServer);

		addButton(slider);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		if (minecraft.player.distanceToSqr(Vector3d.atCenterOf(tileEntity.getBlockPos())) > 64.0D) {
			onClose();
		}
		this.buttons.parallelStream().filter((widget) -> widget instanceof UpdateableWidget).map(UpdateableWidget.class::cast).forEach((widget) -> {
			widget.updateValue();
		});
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private final void sendToServer() {
		NetworkHandler.INSTANCE.sendToServer(new VFXGeneratorDataSyncPKT(this.tileEntity.saveToTag(new CompoundNBT()), this.tileEntity.getBlockPos()));
	}

	@FunctionalInterface
	public interface VoidFunction {
		void apply();
	}
}
