package platinpython.vfxgenerator.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.BlockRegistry;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(DataGenerator gen) {
		super(gen, VFXGenerator.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add(ItemRegistry.VFX_GENERATOR_CORE.get(), "VFX Generator Core");

		add(BlockRegistry.VFX_GENERATOR.get(), "VFX Generator");

		addGui("dataSaved", "Contents Saved.");

		addGui("particle", "Particle");

		addGui("enabled", "Enabled");
		addGui("disabled", "Disabled");

		addGui("useRGB", "Use RGB");
		addGui("useHSB", "Use HSB");

		addGui("red", "Red");
		addGui("green", "Green");
		addGui("blue", "Blue");

		addGui("hue", "Hue");
		addGui("saturation", "Saturation");
		addGui("brightness", "Brightness");

		addGui("spawnX", "X Position");
		addGui("spawnY", "Y Position");
		addGui("spawnZ", "Z Position");

		addGui("motionX", "X Motion");
		addGui("motionY", "Y Motion");
		addGui("motionZ", "Z Motion");

		addGui("lifetime", "Lifetime");

		addGui("size", "Size");

		addGui("delay", "Delay");

		addGui("gravity", "Gravity");

		addGui("collision", "Collision");
		
		addGui("ticks", "Ticks");
	}

	private void addGui(String suffix, String text) {
		add("gui." + VFXGenerator.MOD_ID + "." + suffix, text);
	}
}
