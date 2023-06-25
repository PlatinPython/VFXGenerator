package platinpython.vfxgenerator.util.particle;

import com.mojang.serialization.Decoder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import platinpython.vfxgenerator.VFXGenerator;

public abstract class ParticleType {
    public static final Decoder<ParticleType> FILE_DECODER = ParticleTypes.FILE_DECODER.dispatch(
            ParticleType::type, ParticleTypes::fileDecoder);

    private final boolean supportsColor;

    protected ParticleType(boolean supportsColor) {
        this.supportsColor = supportsColor;
    }

    public boolean supportsColor() {
        return supportsColor;
    }

    public abstract ParticleTypes type();

    // TODO Remove once no longer needed
    public void debug(ResourceLocation location, ResourceLocation optionsLocation, String sourcePackId) {
        VFXGenerator.LOGGER.debug("Loaded VFXGenerator Particle Type: {} from {} from {} in {}",
                                  ToStringBuilder.reflectionToString(this, new MultilineRecursiveToStringStyle() {
                                      {
                                          this.setUseShortClassName(true);
                                          this.setUseIdentityHashCode(false);
                                      }
                                  }), location, optionsLocation, sourcePackId
        );
    }
}
