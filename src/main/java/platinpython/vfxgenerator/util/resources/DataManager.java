package platinpython.vfxgenerator.util.resources;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.resources.server.ParticleListLoader;

import java.util.function.Function;

public class DataManager {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static Codec<ImmutableMap<ResourceLocation, ParticleType>> SELECTABLE_PARTICLES_CODEC = Codec.unboundedMap(
            ResourceLocation.CODEC, ParticleType.CODEC).xmap(ImmutableMap::copyOf, Function.identity());

    private static ImmutableMap<ResourceLocation, ParticleType> SELECTABLE_PARTICLES = ImmutableMap.of();

    public static ImmutableMap<ResourceLocation, ParticleType> selectableParticles() {
        return SELECTABLE_PARTICLES;
    }

    public static void setSelectableParticles(ImmutableMap<ResourceLocation, ParticleType> selectableParticles) {
        if (STACK_WALKER.getCallerClass() != ParticleListLoader.class) {
            throw new IllegalCallerException(
                    "This method is only allowed to be called by " + ParticleListLoader.class.getName() + ", was instead called by " + STACK_WALKER.getCallerClass()
                                                                                                                                                   .getName());
        }
        SELECTABLE_PARTICLES = selectableParticles;
    }
}
