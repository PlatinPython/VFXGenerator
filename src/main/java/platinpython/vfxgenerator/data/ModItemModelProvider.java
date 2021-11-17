package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, VFXGenerator.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(ItemRegistry.VFX_GENERATOR_CORE.getId().getPath(),
                      mcLoc(ITEM_FOLDER + "/generated"),
                      "layer0",
                      modLoc(ITEM_FOLDER + "/vfx_generator_core"));
    }
}
