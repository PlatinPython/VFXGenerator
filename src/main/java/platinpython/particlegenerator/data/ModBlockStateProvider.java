package platinpython.particlegenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.util.registries.BlockRegistry;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, ParticleGenerator.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(BlockRegistry.PARTICLE_GENERATOR.get(), models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/particle_generator")));
	}
}
