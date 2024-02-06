package platinpython.vfxgenerator.util.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.io.IOUtils;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.Util;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.RequiredImageHashesPKT;
import platinpython.vfxgenerator.util.network.packets.SelectableParticlesSyncPKT;
import platinpython.vfxgenerator.util.resources.server.ParticleListLoader;

import java.io.IOException;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandling {
    private static boolean loadingDisabled = false;

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ParticleListLoader());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        NetworkHandler.INSTANCE.send(
            PacketDistributor.PLAYER.with(event::getPlayer),
            new SelectableParticlesSyncPKT(DataManager.selectableParticles())
        );
        NetworkHandler.INSTANCE.send(
            PacketDistributor.PLAYER.with(event::getPlayer), new RequiredImageHashesPKT(DataManager.requiredImages()
                .entrySet()
                .stream()
                .map(entry -> {
                    try {
                        return Optional.of(Pair.of(entry.getKey(),
                            Util.HASH_FUNCTION.hashBytes(IOUtils.toByteArray(entry.getValue().get()))
                        ));
                    } catch (IOException e) {
                        VFXGenerator.LOGGER.error("Failed to hash image for syncing: {}", e.getMessage());
                        return Optional.<Pair<ResourceLocation, HashCode>>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond))));
    }

    public static boolean loadingDisabled() {
        return loadingDisabled;
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        loadingDisabled = true;
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        loadingDisabled = false;
    }
}
