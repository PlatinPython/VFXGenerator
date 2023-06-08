package platinpython.vfxgenerator.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import platinpython.vfxgenerator.VFXGenerator;

public class ModSpriteSourceProvider extends SpriteSourceProvider {
    public ModSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, VFXGenerator.MOD_ID);
    }

    @Override
    protected void addSources() {
    }
}
