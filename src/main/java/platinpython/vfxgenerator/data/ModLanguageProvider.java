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
		
		addGui("enabled", "Enabled");
		addGui("disabled", "Disabled");
		addGui("useRGB", "RGB Selected");
		addGui("useHSB", "HSB Selected");
		addGui("red", "Red");
		addGui("green", "Green");
		addGui("blue", "Blue");
		addGui("hue", "Hue");
		addGui("saturation", "Saturation");
		addGui("brightness", "Brightness");
		
		addGui("particle", "Particle");
		addGui("particleSelected", "");
		addGui("particleLifetime", "Lifetime");
		addGui("particleSize", "Size");
		addGui("particleSpawnX", "X Position");
		addGui("particleSpawnY", "Y Position");
		addGui("particleSpawnZ", "Z Position");
		addGui("particleMotionX", "X Motion");
		addGui("particleMotionY", "Y Motion");
		addGui("particleMotionZ", "Z Motion");
		addGui("particleDelay", "Delay");
		addGui("particleGravity", "Gravity");
		addGui("particleCollision", "Collision");
	}

	private void addGui(String suffix, String text) {
		add("gui." + VFXGenerator.MOD_ID + "." + suffix, text);
	}
}
