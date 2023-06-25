package platinpython.vfxgenerator.util.particle;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import platinpython.vfxgenerator.util.particle.types.SingleParticle;

import java.util.Locale;
import java.util.function.Supplier;

public enum ParticleTypes implements StringRepresentable {
    SINGLE(() -> SingleParticle.FILE_DECODER);

    public static final Codec<ParticleTypes> FILE_DECODER = StringRepresentable.fromEnum(ParticleTypes::values);

    private final Supplier<Codec<? extends ParticleType>> fileDecoder;

    ParticleTypes(Supplier<Codec<? extends ParticleType>> fileDecoder) {
        this.fileDecoder = fileDecoder;
    }

    public Codec<? extends ParticleType> fileDecoder() {
        return this.fileDecoder.get();
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
