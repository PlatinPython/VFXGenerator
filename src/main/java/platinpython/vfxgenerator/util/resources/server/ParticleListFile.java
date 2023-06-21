package platinpython.vfxgenerator.util.resources.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import platinpython.vfxgenerator.VFXGenerator;

import java.util.List;

public record ParticleListFile(List<ResourceLocation> particles, boolean replace) {
    public static final Codec<ParticleListFile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("values").forGetter(ParticleListFile::particles),
            Codec.BOOL.optionalFieldOf("replace", Boolean.FALSE).forGetter(ParticleListFile::replace)
    ).apply(instance, ParticleListFile::new));

    // TODO Remove once no longer needed
    public void debug(ResourceLocation location, String sourcePackId) {
        VFXGenerator.LOGGER.debug(
                "Loaded VFXGenerator Particle Options: {} from {} in {}",
                ToStringBuilder.reflectionToString(this, new MultilineRecursiveToStringStyle() {
                    {
                        this.setUseShortClassName(true);
                        this.setUseIdentityHashCode(false);
                    }
                }), location, sourcePackId
        );
    }
}
