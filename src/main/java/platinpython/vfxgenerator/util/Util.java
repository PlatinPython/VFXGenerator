package platinpython.vfxgenerator.util;

import net.minecraft.util.math.MathHelper;

public class Util {
	public static final double toValue(double value, double minValue, double maxValue, float stepSize) {
		return clamp(MathHelper.lerp(MathHelper.clamp(value, 0.0D, 1.0D), minValue, maxValue), minValue, maxValue, stepSize);
	}

	public static final double clamp(double value, double minValue, double maxValue, float stepSize) {
		if (stepSize > 0.0F) {
			value = (stepSize * Math.round(value / stepSize));
		}

		return map(MathHelper.clamp(value, minValue, maxValue), minValue, maxValue, 0D, 1D);
	}

	public static final double map(double value, double inMin, double inMax, double outMin, double outMax) {
		return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	@FunctionalInterface
	public interface VoidFunction {
		void apply();
	}
}
