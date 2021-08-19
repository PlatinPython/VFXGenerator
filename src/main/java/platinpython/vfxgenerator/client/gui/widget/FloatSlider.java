package platinpython.vfxgenerator.client.gui.widget;

import java.text.DecimalFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import platinpython.vfxgenerator.client.gui.screen.VFXGeneratorScreen.VoidFunction;

public class FloatSlider extends UpdateableWidget {
	private double sliderValue;
	private final DecimalFormat format;

	private final Consumer<Float> setValueFunction;
	private final Supplier<Float> valueSupplier;

	public FloatSlider(int x, int y, int width, int height, ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
		super(x, y, width, height, displayText, minValue, maxValue, stepSize, applyValueFunction);
		this.format = Float.toString(this.stepSize).split("\\.")[1].length() == 1 && Float.toString(this.stepSize).split("\\.")[1].equals("0") ? new DecimalFormat("0") : new DecimalFormat(Float.toString(this.stepSize).replaceAll("\\d", "0"));
		this.setValueFunction = setValueFunction;
		this.valueSupplier = valueSupplier;
		this.setupSliderValues(this.valueSupplier.get());
	}

	private void setupSliderValues(double value) {
		this.sliderValue = this.clamp(value);
		this.setValueFunction.accept((float) this.getSliderValue());
		this.applyValue();
		this.updateMessage();
	}

	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, Minecraft minecraft, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(matrixStack, this.x + (int) (this.sliderValue * (double) (this.width - 8)), this.y, 0, 46 + super.getYImage(isHovered()) * 20, 4, this.height);
		this.blit(matrixStack, this.x + (int) (this.sliderValue * (double) (this.width - 8)) + 4, this.y, 196, 46 + super.getYImage(isHovered()) * 20, 4, this.height);
	}

	@Override
	public void updateValue() {
		if (this.valueSupplier.get() != this.getSliderValue()) {
			this.sliderValue = this.clamp(this.valueSupplier.get());
		}
		this.updateMessage();
	}

	private void setValueFromMouse(double mouseX) {
		this.setSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.setValueFromMouse(mouseX);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		this.setValueFromMouse(mouseX);
	}

	private void setSliderValue(double value) {
		double d0 = this.sliderValue;
		this.sliderValue = this.toValue(value);
		if (d0 != this.sliderValue) {
			this.setValueFunction.accept((float) this.getSliderValue());
			this.applyValue();
		}

		this.updateMessage();
	}

	private double getSliderValue() {
		return this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
	}

	@Override
	public void playDownSound(SoundHandler handler) {
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		super.playDownSound(Minecraft.getInstance().getSoundManager());
	}

	@Override
	protected void updateMessage() {
		setMessage(new StringTextComponent("").append(displayText).append(": ").append(format.format(getSliderValue())));
	}

	@Override
	protected IFormattableTextComponent createNarrationMessage() {
		return new TranslationTextComponent("gui.narrate.slider", this.getMessage());
	}
}