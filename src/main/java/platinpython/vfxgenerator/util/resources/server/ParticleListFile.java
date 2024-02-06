package platinpython.vfxgenerator.util.resources.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ParticleListFile(List<ResourceLocation> particles, boolean replace) {
    public static final Decoder<ParticleListFile> FILE_DECODER = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.listOf().fieldOf("values").forGetter(ParticleListFile::particles),
        Codec.BOOL.optionalFieldOf("replace", Boolean.FALSE).forGetter(ParticleListFile::replace)
    ).apply(instance, ParticleListFile::new));
}
