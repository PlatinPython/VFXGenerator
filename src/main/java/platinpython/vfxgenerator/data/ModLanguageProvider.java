package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(DataGenerator gen) {
		super(gen, VFXGenerator.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add(BlockRegistry.VFX_GENERATOR.get(), "VFX Generator");
		
		addGui("dataSaved", "Contents Saved.");
	}
	
	private void addGui(String suffix, String text) {
		add("gui." + VFXGenerator.MOD_ID + "." + suffix, text);
	}
}
