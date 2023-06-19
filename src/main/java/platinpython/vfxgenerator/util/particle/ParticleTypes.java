package platinpython.vfxgenerator.util.particle;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import platinpython.vfxgenerator.util.particle.types.SingleParticle;

import java.util.Locale;
import java.util.function.Supplier;

public enum ParticleTypes implements StringRepresentable {
    SINGLE(() -> SingleParticle.CODEC);

    public static final Codec<ParticleTypes> CODEC = StringRepresentable.fromEnum(ParticleTypes::values);

    private final Supplier<Codec<? extends ParticleType>> codec;

    ParticleTypes(Supplier<Codec<? extends ParticleType>> codec) {
        this.codec = codec;
    }

    public Codec<? extends ParticleType> codec() {
        return this.codec.get();
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
