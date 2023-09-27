package platinpython.vfxgenerator.util.network.packets;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.resources.DataManager;
import platinpython.vfxgenerator.util.resources.client.CacheHandler;
import platinpython.vfxgenerator.util.resources.client.VirtualPack;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

public class UpdateRequiredImagesPKT {
    private static final Codec<ImmutableSet<ResourceLocation>> CODEC = ResourceLocation.CODEC.listOf()
                                                                                             .xmap(
                                                                                                     ImmutableSet::copyOf,
                                                                                                     ImmutableSet::asList
                                                                                             );

    private final ImmutableSet<ResourceLocation> set;

    public UpdateRequiredImagesPKT(ImmutableSet<ResourceLocation> set) {
        this.set = set;
    }

    public static void encode(UpdateRequiredImagesPKT message, FriendlyByteBuf buffer) {
        buffer.writeJsonWithCodec(CODEC, message.set);
    }

    public static UpdateRequiredImagesPKT decode(FriendlyByteBuf buffer) {
        return new UpdateRequiredImagesPKT(buffer.readJsonWithCodec(CODEC));
    }

    public static class Handler {
        public static void handle(UpdateRequiredImagesPKT message, Supplier<NetworkEvent.Context> context) {
            //noinspection OptionalGetWithoutIsPresent
            DataManager.setRequiredImages(message.set.stream()
                                                     .map(resourceLocation -> Pair.of(
                                                             resourceLocation,
                                                             CacheHandler.getIoSupplier(
                                                                     resourceLocation)
                                                     ))
                                                     .filter(pair -> {
                                                         Optional<IoSupplier<InputStream>> second = pair.getSecond();
                                                         if (second.isEmpty()) {
                                                             VFXGenerator.LOGGER.warn(
                                                                     "Missing texture {} in cache, check earlier log for errors.",
                                                                     pair.getFirst()
                                                             );
                                                         }
                                                         return second.isPresent();
                                                     })
                                                     .map(pair -> Pair.of(pair.getFirst(), pair.getSecond().get()))
                                                     .collect(ImmutableMap.toImmutableMap(
                                                             Pair::getFirst,
                                                             Pair::getSecond
                                                     )));
            VirtualPack.VIRTUAL_PACK.reload();
        }
    }
}
