package platinpython.vfxgenerator.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import platinpython.vfxgenerator.client.gui.widget.ToggleButton;
import platinpython.vfxgenerator.client.gui.widget.VFXGeneratorOptionsList;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.Constants.ParticleConstants;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDataSyncPKT;

public class VFXGeneratorScreen extends Screen {
	public final VFXGeneratorTileEntity tileEntity;

	private VFXGeneratorOptionsList particleOptionsList;

	public VFXGeneratorScreen(VFXGeneratorTileEntity tileEntity) {
		super(new TranslationTextComponent("block.vfxgenerator.vfx_generator"));
		this.tileEntity = tileEntity;
	}

	@Override
	protected void init() {
		ToggleButton button = new ToggleButton(this.width / 2 - 30, 20, 60, 10, this.tileEntity::setParticleEnabled, this.tileEntity::isParticleEnabled, this::sendToServer);
		addButton(button);

		this.particleOptionsList = new VFXGeneratorOptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

		this.particleOptionsList.addMultipleChoiceButton(ParticleConstants.PARTICLE_OPTIONS, this.tileEntity::setParticleSelected, this.tileEntity::getParticleSelected, this::sendToServer);

		this.particleOptionsList.addToggleButton(ClientUtils.getGuiTranslationTextComponent("rgb"), ClientUtils.getGuiTranslationTextComponent("hsb"), this.tileEntity::setParticleUseHSB, this.tileEntity::isParticleUseHSB, this::sendToServer);

		this.particleOptionsList.addToggleableRangeSlider(1F, ClientUtils.getGuiTranslationTextComponent("red"), StringTextComponent.EMPTY, 0F, 255F, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorBot());
			Color newColor = new Color(value.intValue(), oldColor.getGreen(), oldColor.getBlue());
			this.tileEntity.setParticleRGBColorBot(newColor.getRGB());
		}, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorTop());
			Color newColor = new Color(value.intValue(), oldColor.getGreen(), oldColor.getBlue());
			this.tileEntity.setParticleRGBColorTop(newColor.getRGB());
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorBot()).getRed();
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorTop()).getRed();
		}, ClientUtils.getGuiTranslationTextComponent("hue"), new StringTextComponent("°"), 0F, 360F, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorBot();
			this.tileEntity.setParticleHSBColorBot(new float[] { value / 360F, oldHsb[1], oldHsb[2] });
		}, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorTop();
			this.tileEntity.setParticleHSBColorTop(new float[] { value / 360F, oldHsb[1], oldHsb[2] });
		}, () -> {
			return this.tileEntity.getParticleHSBColorBot()[0] * 360F;
		}, () -> {
			return this.tileEntity.getParticleHSBColorTop()[0] * 360F;
		}, this::sendToServer, this.tileEntity::isParticleUseHSB);

		this.particleOptionsList.addToggleableRangeSlider(1F, ClientUtils.getGuiTranslationTextComponent("green"), StringTextComponent.EMPTY, 0F, 255F, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorBot());
			Color newColor = new Color(oldColor.getRed(), value.intValue(), oldColor.getBlue());
			this.tileEntity.setParticleRGBColorBot(newColor.getRGB());
		}, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorTop());
			Color newColor = new Color(oldColor.getRed(), value.intValue(), oldColor.getBlue());
			this.tileEntity.setParticleRGBColorTop(newColor.getRGB());
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorBot()).getGreen();
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorTop()).getGreen();
		}, ClientUtils.getGuiTranslationTextComponent("saturation"), new StringTextComponent("%"), 0F, 100F, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorBot();
			this.tileEntity.setParticleHSBColorBot(new float[] { oldHsb[0], value / 100F, oldHsb[2] });
		}, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorTop();
			this.tileEntity.setParticleHSBColorTop(new float[] { oldHsb[0], value / 100F, oldHsb[2] });
		}, () -> {
			return this.tileEntity.getParticleHSBColorBot()[1] * 100F;
		}, () -> {
			return this.tileEntity.getParticleHSBColorTop()[1] * 100F;
		}, this::sendToServer, this.tileEntity::isParticleUseHSB);

		this.particleOptionsList.addToggleableRangeSlider(1F, ClientUtils.getGuiTranslationTextComponent("blue"), StringTextComponent.EMPTY, 0F, 255F, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorBot());
			Color newColor = new Color(oldColor.getRed(), oldColor.getGreen(), value.intValue());
			this.tileEntity.setParticleRGBColorBot(newColor.getRGB());
		}, (value) -> {
			Color oldColor = new Color(this.tileEntity.getParticleRGBColorTop());
			Color newColor = new Color(oldColor.getRed(), oldColor.getGreen(), value.intValue());
			this.tileEntity.setParticleRGBColorTop(newColor.getRGB());
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorBot()).getBlue();
		}, () -> {
			return (float) new Color(this.tileEntity.getParticleRGBColorTop()).getBlue();
		}, ClientUtils.getGuiTranslationTextComponent("brightness"), new StringTextComponent("%"), 0F, 100F, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorBot();
			this.tileEntity.setParticleHSBColorBot(new float[] { oldHsb[0], oldHsb[1], value / 100F });
		}, (value) -> {
			float[] oldHsb = this.tileEntity.getParticleHSBColorTop();
			this.tileEntity.setParticleHSBColorTop(new float[] { oldHsb[0], oldHsb[1], value / 100F });
		}, () -> {
			return this.tileEntity.getParticleHSBColorBot()[2] * 100F;
		}, () -> {
			return this.tileEntity.getParticleHSBColorTop()[2] * 100F;
		}, this::sendToServer, this.tileEntity::isParticleUseHSB);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("lifetime"), ClientUtils.getGuiTranslationTextComponent("ticks"), ParticleConstants.MIN_LIFETIME, ParticleConstants.MAX_LIFETIME, 1F, (value) -> this.tileEntity.setParticleLifetimeBot(value.intValue()), (value) -> this.tileEntity.setParticleLifetimeTop(value.intValue()), () -> (float) this.tileEntity.getParticleLifetimeBot(), () -> (float) this.tileEntity.getParticleLifetimeTop(), this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("size"), StringTextComponent.EMPTY, ParticleConstants.MIN_SIZE, ParticleConstants.MAX_SIZE, .5F, this.tileEntity::setParticleSizeBot, this.tileEntity::setParticleSizeTop, this.tileEntity::getParticleSizeBot, this.tileEntity::getParticleSizeTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("spawnX"), StringTextComponent.EMPTY, ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN, .1F, this.tileEntity::setParticleSpawnXBot, this.tileEntity::setParticleSpawnXTop, this.tileEntity::getParticleSpawnXBot, this.tileEntity::getParticleSpawnXTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("spawnY"), StringTextComponent.EMPTY, ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN, .1F, this.tileEntity::setParticleSpawnYBot, this.tileEntity::setParticleSpawnYTop, this.tileEntity::getParticleSpawnYBot, this.tileEntity::getParticleSpawnYTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("spawnZ"), StringTextComponent.EMPTY, ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN, .1F, this.tileEntity::setParticleSpawnZBot, this.tileEntity::setParticleSpawnZTop, this.tileEntity::getParticleSpawnZBot, this.tileEntity::getParticleSpawnZTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("motionX"), StringTextComponent.EMPTY, ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION, .01F, this.tileEntity::setParticleMotionXBot, this.tileEntity::setParticleMotionXTop, this.tileEntity::getParticleMotionXBot, this.tileEntity::getParticleMotionXTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("motionY"), StringTextComponent.EMPTY, ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION, .01F, this.tileEntity::setParticleMotionYBot, this.tileEntity::setParticleMotionYTop, this.tileEntity::getParticleMotionYBot, this.tileEntity::getParticleMotionYTop, this::sendToServer);

		this.particleOptionsList.addRangeSlider(ClientUtils.getGuiTranslationTextComponent("motionZ"), StringTextComponent.EMPTY, ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION, .01F, this.tileEntity::setParticleMotionZBot, this.tileEntity::setParticleMotionZTop, this.tileEntity::getParticleMotionZBot, this.tileEntity::getParticleMotionZTop, this::sendToServer);

		this.particleOptionsList.addSlider(ClientUtils.getGuiTranslationTextComponent("delay"), ClientUtils.getGuiTranslationTextComponent("ticks"), ParticleConstants.MIN_DELAY, ParticleConstants.MAX_DELAY, 1F, (value) -> this.tileEntity.setParticleDelay(value.intValue()), () -> (float) this.tileEntity.getParticleDelay(), this::sendToServer);

		this.particleOptionsList.addSlider(ClientUtils.getGuiTranslationTextComponent("gravity"), StringTextComponent.EMPTY, ParticleConstants.MIN_GRAVITY, ParticleConstants.MAX_GRAVITY, .01F, this.tileEntity::setParticleGravity, this.tileEntity::getParticleGravity, this::sendToServer);

		this.particleOptionsList.addToggleButton(ClientUtils.getGuiTranslationTextComponent("collision").append(": ").append(ClientUtils.getGuiTranslationTextComponent("disabled")), ClientUtils.getGuiTranslationTextComponent("collision").append(": ").append(ClientUtils.getGuiTranslationTextComponent("enabled")), this.tileEntity::setParticleCollision, this.tileEntity::isParticleCollision, this::sendToServer);

		this.particleOptionsList.children().parallelStream().forEach((entry) -> entry.setActive(this.tileEntity.isParticleEnabled()));

		this.addWidget(this.particleOptionsList);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.particleOptionsList.render(matrixStack, mouseX, mouseY, partialTicks);
		if (!this.tileEntity.isParticleEnabled()) {
			this.fillGradient(matrixStack, 0, 32, this.width, this.height - 32, 0xC0101010, 0xD0101010);
		}
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		AbstractGui.drawCenteredString(matrixStack, this.font, ClientUtils.getGuiTranslationTextComponent("particle"), this.width / 2, 10, 0xFFFFFFFF);
	}

	@Override
	public void tick() {
		if (minecraft.player.distanceToSqr(Vector3d.atCenterOf(tileEntity.getBlockPos())) > 64.0D) {
			onClose();
		}
		this.particleOptionsList.children().parallelStream().forEach((entry) -> {
			entry.updateValue();
			entry.setActive(this.tileEntity.isParticleEnabled());
		});
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private final void sendToServer() {
		NetworkHandler.INSTANCE.sendToServer(new VFXGeneratorDataSyncPKT(this.tileEntity.saveToTag(new CompoundNBT()), this.tileEntity.getBlockPos()));
	}
}
