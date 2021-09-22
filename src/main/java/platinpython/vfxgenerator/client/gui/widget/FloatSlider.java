package platinpython.vfxgenerator.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import platinpython.vfxgenerator.util.Util;
import platinpython.vfxgenerator.util.Util.VoidFunction;

import java.text.DecimalFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatSlider extends UpdateableWidget {
	private final double minValue, maxValue;
	private final float stepSize;
	private final DecimalFormat format;
	private final ITextComponent prefix;
	private final ITextComponent suffix;
	private final Consumer<Float> setValueFunction;
	private final Supplier<Float> valueSupplier;
	private double sliderValue;

	public FloatSlider(int x, int y, int width, int height, ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
		super(x, y, width, height, applyValueFunction);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.stepSize = stepSize;
		this.format = Float.toString(this.stepSize).split("\\.")[1].length() == 1 && Float.toString(this.stepSize).split("\\.")[1].equals("0") ? new DecimalFormat("0") : new DecimalFormat(Float.toString(this.stepSize).replaceAll("\\d", "0"));
		this.prefix = prefix;
		this.suffix = suffix;
		this.setValueFunction = setValueFunction;
		this.valueSupplier = valueSupplier;
		this.setupSliderValues(this.valueSupplier.get());
	}

	private void setupSliderValues(double value) {
		this.sliderValue = Util.clamp(value, this.minValue, this.maxValue, this.stepSize);
		this.setValueFunction.accept((float) this.getSliderValue());
		this.applyValue();
		this.updateMessage();
	}

	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}

	private int getYImageNoDisabled(boolean isHovered) {
		if (!this.active)
			return 1;
		return isHovered ? 2 : 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, Minecraft minecraft, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(matrixStack, this.x + (int) (this.sliderValue * (double) (this.width - 8)), this.y, 0, 46 + this.getYImageNoDisabled(isHovered()) * 20, 4, this.height);
		this.blit(matrixStack, this.x + (int) (this.sliderValue * (double) (this.width - 8)) + 4, this.y, 196, 46 + this.getYImageNoDisabled(isHovered()) * 20, 4, this.height);
	}

	@Override
	public void updateValue() {
		if (this.valueSupplier.get() != this.getSliderValue()) {
			this.sliderValue = Util.clamp(this.valueSupplier.get(), this.minValue, this.maxValue, this.stepSize);
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

	private double getSliderValue() {
		return this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
	}

	private void setSliderValue(double value) {
		double d0 = this.sliderValue;
		this.sliderValue = Util.toValue(value, this.minValue, this.maxValue, this.stepSize);
		if (d0 != this.sliderValue) {
			this.setValueFunction.accept((float) this.getSliderValue());
			this.applyValue();
		}

		this.updateMessage();
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
		setMessage(new StringTextComponent("").append(prefix).append(": ").append(format.format(getSliderValue())).append(suffix.getString().isEmpty() ? "" : " ").append(suffix));
	}

	@Override
	protected IFormattableTextComponent createNarrationMessage() {
		return new TranslationTextComponent("gui.narrate.slider", this.getMessage());
	}
}
