package platinpython.vfxgenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import platinpython.vfxgenerator.data.DataGatherer;
import platinpython.vfxgenerator.util.RegistryHandler;

@Mod(VFXGenerator.MOD_ID)
public class VFXGenerator {
	public static final String MOD_ID = "vfxgenerator";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public VFXGenerator() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGatherer::onGatherData);

		RegistryHandler.register();
	}
}
