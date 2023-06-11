package platinpython.vfxgenerator.util.resources.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import platinpython.vfxgenerator.VFXGenerator;

import java.util.List;

public record ParticleOptions(List<ResourceLocation> particles, boolean replace) {
    public static final Codec<ParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("values").forGetter(ParticleOptions::particles),
            Codec.BOOL.optionalFieldOf("replace", Boolean.FALSE).forGetter(ParticleOptions::replace)
    ).apply(instance, ParticleOptions::new));

    // TODO Remove once no longer needed
    public void debug() {
        VFXGenerator.LOGGER.debug("Loaded VFXGenerator Particle Options: {}",
                                  ToStringBuilder.reflectionToString(this, new MultilineRecursiveToStringStyle() {
                                      {
                                          this.setUseShortClassName(true);
                                          this.setUseIdentityHashCode(false);
                                      }
                                  })
        );
    }
}
