package platinpython.particlegenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.util.registries.BlockRegistry;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(DataGenerator gen) {
		super(gen, ParticleGenerator.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add(BlockRegistry.PARTICLE_GENERATOR.get(), "Particle Generator");
	}
}
