package platinpython.vfxgenerator.util.resources;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import platinpython.vfxgenerator.util.particle.ParticleType;

import java.util.function.Function;

public class DataManager {
    public static Codec<ImmutableMap<ResourceLocation, ParticleType>> SELECTABLE_PARTICLES_CODEC = Codec.unboundedMap(
            ResourceLocation.CODEC, ParticleType.CODEC).xmap(ImmutableMap::copyOf, Function.identity());

    public static ImmutableMap<ResourceLocation, ParticleType> SELECTABLE_PARTICLES = ImmutableMap.of();
}
