package platinpython.vfxgenerator.util.particle.types;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.particle.ParticleTypes;

public class SingleParticle extends ParticleType {
    public static final Codec<SingleParticle> CODEC = ResourceLocation.CODEC.xmap(
            SingleParticle::new, SingleParticle::value).fieldOf("value").codec();

    private final ResourceLocation value;

    public SingleParticle(ResourceLocation value) {
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
