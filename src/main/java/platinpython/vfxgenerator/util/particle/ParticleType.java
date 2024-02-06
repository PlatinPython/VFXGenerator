package platinpython.vfxgenerator.util.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.Stream;

public abstract class ParticleType {
    public static final Decoder<ParticleType> FILE_DECODER = ParticleTypes.CODEC.dispatch(
        ParticleType::type, ParticleTypes::fileDecoder);

    public static final Codec<ParticleType> CODEC = ParticleTypes.CODEC.dispatch(
        ParticleType::type, ParticleTypes::codec);

    private final boolean supportsColor;

    protected ParticleType(boolean supportsColor) {
        this.supportsColor = supportsColor;
    }

    public boolean supportsColor() {
        return supportsColor;
    }

    public abstract ParticleTypes type();

    public abstract Stream<ResourceLocation> images();
}
