package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, VFXGenerator.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(BlockRegistry.VFX_GENERATOR.getId().getPath(), modLoc(BLOCK_FOLDER + "/vfx_generator"));
	}
}
