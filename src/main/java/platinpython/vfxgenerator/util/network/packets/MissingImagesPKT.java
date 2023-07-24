package platinpython.vfxgenerator.util.network.packets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.Util;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.resources.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class MissingImagesPKT {
    private static final Codec<ImmutableList<ResourceLocation>> CODEC = ResourceLocation.CODEC.listOf()
                                                                                              .xmap(
                                                                                                      ImmutableList::copyOf,
                                                                                                      Function.identity()
                                                                                              );

    private final ImmutableList<ResourceLocation> list;

    public MissingImagesPKT(ImmutableList<ResourceLocation> list) {
        this.list = list;
    }

    public static void encode(MissingImagesPKT message, FriendlyByteBuf buffer) {
        buffer.writeJsonWithCodec(CODEC, message.list);
    }

    public static MissingImagesPKT decode(FriendlyByteBuf buffer) {
        return new MissingImagesPKT(buffer.readJsonWithCodec(CODEC));
    }

    public static class Handler {
        public static void handle(MissingImagesPKT message, Supplier<NetworkEvent.Context> context) {
            // TODO look into improving memory footprint
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            ImmutableMap<ResourceLocation, Resource> requiredImages = DataManager.requiredImages();
            List<Pair<ResourceLocation, byte[]>> list = message.list.stream()
                                                                    .map(key -> Pair.of(key, requiredImages.get(key)))
                                                                    .filter(pair -> Objects.nonNull(pair.getSecond()))
                                                                    .map(pair -> {
                                                                        try (
                                                                                InputStream image = pair.getSecond()
                                                                                                        .open()
                                                                        ) {
                                                                            return Optional.of(Pair.of(
                                                                                    pair.getFirst(),
                                                                                    image.readAllBytes()
                                                                            ));
                                                                        } catch (IOException e) {
                                                                            VFXGenerator.LOGGER.error(
                                                                                    "Failed to open resource", e);
                                                                            return Optional.<Pair<ResourceLocation, byte[]>>empty();
                                                                        }
                                                                    })
                                                                    .filter(Optional::isPresent)
                                                                    .map(Optional::get)
                                                                    .toList();
            buffer.writeInt(list.size());
            list.forEach(pair -> {
                buffer.writeResourceLocation(pair.getFirst());
                buffer.writeByteArray(pair.getSecond());
            });
            while (buffer.readableBytes() > Util.MAX_PAYLOAD_SIZE - 6) {
                byte[] data = new byte[Util.MAX_PAYLOAD_SIZE - 6];
                buffer.readBytes(data);
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(context.get()::getSender),
                        new MissingImagesDataPKT(false, data)
                );
            }
            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(context.get()::getSender),
                    new MissingImagesDataPKT(true, data)
            );
        }
    }
}
