package platinpython.vfxgenerator.util.resources;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.SelectableParticlesSyncPKT;
import platinpython.vfxgenerator.util.resources.server.ParticleListLoader;

@Mod.EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandling {
    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ParticleListLoader());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        VFXGenerator.LOGGER.info("OnDatapackSyncEvent received");
        VFXGenerator.LOGGER.info("Player: {}", event.getPlayer());
        VFXGenerator.LOGGER.info("PlayerList: {}", event.getPlayerList().getPlayers());
        if (event.getPlayer() == null) {
            event.getPlayerList().getPlayers().forEach(player -> NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SelectableParticlesSyncPKT(DataManager.selectableParticles())
            ));
        } else {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(event::getPlayer),
                    new SelectableParticlesSyncPKT(DataManager.selectableParticles())
            );
        }
    }
}
