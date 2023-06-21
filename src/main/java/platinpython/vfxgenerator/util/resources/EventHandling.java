package platinpython.vfxgenerator.util.resources;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.resources.server.ParticleListLoader;

@Mod.EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandling {
    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ParticleListLoader());
    }
}
