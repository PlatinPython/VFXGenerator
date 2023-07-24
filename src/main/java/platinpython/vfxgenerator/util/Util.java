package platinpython.vfxgenerator.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import platinpython.vfxgenerator.VFXGenerator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;

public class Util {
    public static final int MAX_PAYLOAD_SIZE = 1024 * 1024;

    public static ResourceLocation createNamespacedResourceLocation(String path) {
        return new ResourceLocation(VFXGenerator.MOD_ID, path);
    }

    public static <E> TreeSet<E> createTreeSetFromCollectionWithComparator(
            Collection<? extends E> collection, Comparator<? super E> comparator
    ) {
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);
        return set;
    }

    public static double toValue(double value, double minValue, double maxValue, float stepSize) {
        return clamp(Mth.lerp(Mth.clamp(value, 0.0D, 1.0D), minValue, maxValue), minValue, maxValue, stepSize);
    }

    public static double clamp(double value, double minValue, double maxValue, float stepSize) {
        if (stepSize > 0.0F) {
            value = (stepSize * Math.round(value / stepSize));
        }

        return map(Mth.clamp(value, minValue, maxValue), minValue, maxValue, 0D, 1D);
    }

    public static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    @FunctionalInterface
    public interface BooleanSupplier {
        boolean get();
    }

    @FunctionalInterface
    public interface BooleanConsumer {
        void accept(boolean value);

        default BooleanConsumer andThen(BooleanConsumer after) {
            Objects.requireNonNull(after);
            return (boolean t) -> {
                accept(t);
                after.accept(t);
            };
        }
    }

    @FunctionalInterface
    public interface FloatSupplier {
        float get();
    }

    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);

        default FloatConsumer andThen(FloatConsumer after) {
            Objects.requireNonNull(after);
            return (float t) -> {
                accept(t);
                after.accept(t);
            };
        }
    }
}
