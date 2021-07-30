package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, VFXGenerator.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(BlockRegistry.VFX_GENERATOR.get(), models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator")));
	}
}
