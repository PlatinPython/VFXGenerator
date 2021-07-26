package platinpython.particlegenerator.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import platinpython.particlegenerator.ParticleGenerator;

@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SubscribeEvent
	public static void doClientStuff(FMLClientSetupEvent event) {
	}
}
