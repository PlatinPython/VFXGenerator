package platinpython.particlegenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import platinpython.particlegenerator.data.DataGatherer;
import platinpython.particlegenerator.util.RegistryHandler;
import platinpython.particlegenerator.util.network.PacketHandler;

@Mod(ParticleGenerator.MOD_ID)
public class ParticleGenerator {
	public static final String MOD_ID = "particlegenerator";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public ParticleGenerator() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGatherer::onGatherData);

		RegistryHandler.register();
		
		PacketHandler.register();
	}
}
