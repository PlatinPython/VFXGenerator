package platinpython.vfxgenerator.client.gui.widget;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import platinpython.vfxgenerator.client.gui.screen.VFXGeneratorScreen.VoidFunction;

public abstract class UpdateableWidget extends Widget {
	protected final double minValue, maxValue;
	protected final float stepSize;
	protected final VoidFunction applyValueFunction;
	
	protected ITextComponent displayText;

	public UpdateableWidget(int x, int y, int width, int height, ITextComponent displayText, double minValue, double maxValue, float stepSize, VoidFunction applyValueFunction) {
		super(x, y, width, height, displayText);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.stepSize = stepSize;
		this.applyValueFunction = applyValueFunction;
		this.displayText = displayText;
	}

	public abstract void updateValue();

	protected double toValue(double value) {
		return this.clamp(MathHelper.lerp(MathHelper.clamp(value, 0.0D, 1.0D), this.minValue, this.maxValue));
	}

	protected double clamp(double value) {
		if (this.stepSize > 0.0F) {
			value = (this.stepSize * Math.round(value / this.stepSize));
		}

		return map(MathHelper.clamp(value, this.minValue, this.maxValue), this.minValue, this.maxValue, 0D, 1D);
	}

	protected double map(double value, double inMin, double inMax, double outMin, double outMax) {
		return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
	
	protected abstract void updateMessage();
	
	protected void applyValue() {
		this.applyValueFunction.apply();
	}
}
