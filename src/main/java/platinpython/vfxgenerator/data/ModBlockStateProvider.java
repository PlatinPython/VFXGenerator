package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, VFXGenerator.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
//		simpleBlock(BlockRegistry.VFX_GENERATOR.get(), models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator")));
		getVariantBuilder(BlockRegistry.VFX_GENERATOR.get()).forAllStatesExcept((state) -> {
			if (state.getValue(VFXGeneratorBlock.INVERTED)) {
				if (state.getValue(VFXGeneratorBlock.POWERED)) {
					return ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator_on_inverted"))).build();
				} else {
					return ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator_off_inverted"))).build();
				}
			} else {
				if (state.getValue(VFXGeneratorBlock.POWERED)) {
					return ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator_on"))).build();
				} else {
					return ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/vfx_generator_off"))).build();
				}
			}
		}, VFXGeneratorBlock.WATERLOGGED);
	}
}
