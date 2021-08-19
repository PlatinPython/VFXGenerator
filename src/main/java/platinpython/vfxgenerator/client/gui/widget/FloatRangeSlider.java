package platinpython.vfxgenerator.client.gui.widget;

import java.text.DecimalFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import platinpython.vfxgenerator.client.gui.screen.VFXGeneratorScreen.VoidFunction;

public class FloatRangeSlider extends UpdateableWidget {
	private double leftSliderValue, rightSliderValue;
	private final DecimalFormat format;

	private final Consumer<Float> setLeftValueFunction;
	private final Consumer<Float> setRightValueFunction;
	private final Supplier<Float> leftValueSupplier;
	private final Supplier<Float> rightValueSupplier;

	public FloatRangeSlider(int x, int y, int width, int height, ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setLeftValueFunction, Consumer<Float> setRightValueFunction, Supplier<Float> leftValueSupplier, Supplier<Float> rightValueSupplier, VoidFunction applyValueFunction) {
		super(x, y, width, height, displayText, minValue, maxValue, stepSize, applyValueFunction);
		this.format = Float.toString(this.stepSize).split("\\.")[1].length() == 1 && Float.toString(this.stepSize).split("\\.")[1].equals("0") ? new DecimalFormat("0") : new DecimalFormat(Float.toString(this.stepSize).replaceAll("\\d", "0"));
		this.setLeftValueFunction = setLeftValueFunction;
		this.setRightValueFunction = setRightValueFunction;
		this.leftValueSupplier = leftValueSupplier;
		this.rightValueSupplier = rightValueSupplier;
		this.setupSliderValues(this.leftValueSupplier.get(), this.rightValueSupplier.get());
	}

	private void setupSliderValues(double leftValue, double rightValue) {
		this.leftSliderValue = this.clamp(leftValue);
		this.rightSliderValue = this.clamp(rightValue);
		this.leftSliderValue = this.toValue(MathHelper.clamp(this.leftSliderValue, 0D, this.rightSliderValue));
		this.rightSliderValue = this.toValue(MathHelper.clamp(this.rightSliderValue, this.leftSliderValue, 1D));
		this.setLeftValueFunction.accept((float) this.getLeftSliderValue());
		this.setRightValueFunction.accept((float) this.getRightSliderValue());
		this.applyValue();
		this.updateMessage();
	}

	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}

	public boolean isLeftHovered(int mouseX) {
		return this.isHovered() && mouseX < (this.x + ((this.rightSliderValue + this.leftSliderValue) / 2) * this.width);
	}

	public boolean isRightHovered(int mouseX) {
		return this.isHovered() && mouseX > (this.x + ((this.rightSliderValue + this.leftSliderValue) / 2) * this.width);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, Minecraft minecraft, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtils.drawContinuousTexturedBox(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 4, this.y + 3, 0, 66, ((int) (this.rightSliderValue * (double) (this.width - 8))) - ((int) (this.leftSliderValue * (double) (this.width - 8))), this.height - 6, 200, 20, 2, 2, 2, 2, this.getBlitOffset());
		this.blit(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)), this.y, 0, 46 + super.getYImage(isLeftHovered(mouseX)) * 20, 4, this.height);
		this.blit(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 4, this.y, 196, 46 + super.getYImage(isLeftHovered(mouseX)) * 20, 4, this.height);
		this.blit(matrixStack, this.x + (int) (this.rightSliderValue * (double) (this.width - 8)), this.y, 0, 46 + super.getYImage(isRightHovered(mouseX)) * 20, 4, this.height);
		this.blit(matrixStack, this.x + (int) (this.rightSliderValue * (double) (this.width - 8)) + 4, this.y, 196, 46 + super.getYImage(isRightHovered(mouseX)) * 20, 4, this.height);
	}

	private boolean isLeftSelected;
	private boolean stopped;

	private boolean getIsLeftClicked(double mouseX) {
		if (mouseX < this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 8 || mouseX < ((this.x + this.leftSliderValue * this.width) + (this.x + this.rightSliderValue * this.width)) / 2) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.isLeftSelected = getIsLeftClicked(mouseX);
		this.stopped = false;
		if (this.isLeftSelected) {
			this.setLeftSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
		} else {
			this.setRightSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
		}
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		if (this.leftSliderValue == this.rightSliderValue && !stopped) {
			this.isLeftSelected = dragX < 0;
		}
		if (this.isLeftSelected) {
			this.setLeftSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
		} else {
			this.setRightSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
		}
		this.stopped = this.leftSliderValue == this.rightSliderValue;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean flag = keyCode == GLFW.GLFW_KEY_LEFT;
		if (flag || keyCode == GLFW.GLFW_KEY_RIGHT) {
			if (flag) {
				if (this.leftSliderValue != 0D) {
					this.setLeftSliderValue(this.clamp(this.getLeftSliderValue() - this.stepSize));
					this.setRightSliderValue(this.clamp(this.getRightSliderValue() - this.stepSize));
				}
			} else {
				if (this.rightSliderValue != 1D) {
					this.setRightSliderValue(this.clamp(this.getRightSliderValue() + this.stepSize));
					this.setLeftSliderValue(this.clamp(this.getLeftSliderValue() + this.stepSize));
				}
			}
		}
		return false;
	}

	@Override
	public void updateValue() {
		if (this.leftValueSupplier.get() != this.getLeftSliderValue()) {
			this.leftSliderValue = this.clamp(this.leftValueSupplier.get());
		}
		if (this.rightValueSupplier.get() != this.getRightSliderValue()) {
			this.rightSliderValue = this.clamp(this.rightValueSupplier.get());
		}
		this.updateMessage();
	}

	private void setLeftSliderValue(double value) {
		double d0 = this.leftSliderValue;
		this.leftSliderValue = this.toValue(MathHelper.clamp(value, 0.0D, this.rightSliderValue));
		if (d0 != this.leftSliderValue) {
			this.setLeftValueFunction.accept((float) this.getLeftSliderValue());
			this.applyValue();
		}

		this.updateMessage();
	}

	private double getLeftSliderValue() {
		return this.leftSliderValue * (this.maxValue - this.minValue) + this.minValue;
	}

	private void setRightSliderValue(double value) {
		double d0 = this.rightSliderValue;
		this.rightSliderValue = this.toValue(MathHelper.clamp(value, this.leftSliderValue, 1.0D));
		if (d0 != this.rightSliderValue) {
			this.setRightValueFunction.accept((float) this.getRightSliderValue());
			this.applyValue();
		}

		this.updateMessage();
	}

	private double getRightSliderValue() {
		return this.rightSliderValue * (this.maxValue - this.minValue) + this.minValue;
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
		setMessage(new StringTextComponent("").append(displayText).append(": ").append(format.format(getLeftSliderValue())).append(" - ").append(format.format(getRightSliderValue())));
	}

	@Override
	protected IFormattableTextComponent createNarrationMessage() {
		return new TranslationTextComponent("gui.narrate.slider", this.getMessage());
	}
}
