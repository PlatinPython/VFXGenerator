package platinpython.vfxgenerator.util.particle.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.particle.ParticleTypes;
import platinpython.vfxgenerator.util.resources.ResourceCodec;
import platinpython.vfxgenerator.util.resources.ResourceUtil;

public class SingleParticle extends ParticleType {
    public static final Codec<SingleParticle> FILE_DECODER = RecordCodecBuilder.create(
            instance -> instance.group(
                                        ResourceLocation.CODEC.fieldOf("value").forGetter(SingleParticle::value),
                                        new ResourceCodec().flatXmap(ResourceUtil::supportsColor, i -> DataResult.error(
                                                                   () -> "Serializing not supported"))
                                                           .fieldOf("value")
                                                           .forGetter(i -> null)
                                )
                                .apply(instance, SingleParticle::new));

    public static final Codec<SingleParticle> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("value").forGetter(SingleParticle::value),
                    Codec.BOOL.fieldOf("supportsColor").forGetter(SingleParticle::supportsColor)
            ).apply(instance, SingleParticle::new));

    private final ResourceLocation value;

    public SingleParticle(ResourceLocation value, boolean supportsColor) {
        super(supportsColor);
        this.value = value;
    }

    public ResourceLocation value() {
        return this.value;
    }

    @Override
    public ParticleTypes type() {
        return ParticleTypes.SINGLE;
    }
}
