package platinpython.particlegenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.util.registries.BlockRegistry;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, ParticleGenerator.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(BlockRegistry.PARTICLE_GENERATOR.getId().getPath(), modLoc(BLOCK_FOLDER + "/particle_generator"));
	}
}
