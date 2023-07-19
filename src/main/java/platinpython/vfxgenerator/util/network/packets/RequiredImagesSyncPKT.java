package platinpython.vfxgenerator.util.network.packets;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.VFXGenerator;

import java.nio.ByteBuffer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RequiredImagesSyncPKT {
    private static final Codec<ImmutableMap<ResourceLocation, HashCode>> CODEC = Codec.unboundedMap(
            ResourceLocation.CODEC, Codec.BYTE_BUFFER.xmap(
                    byteBuffer -> HashCode.fromBytes(byteBuffer.array()),
                    hashCode -> ByteBuffer.wrap(hashCode.asBytes())
            )).xmap(ImmutableMap::copyOf, Function.identity());

    private final ImmutableMap<ResourceLocation, HashCode> map;

    public RequiredImagesSyncPKT(ImmutableMap<ResourceLocation, HashCode> map) {
        this.map = map;
    }

    public static void encode(RequiredImagesSyncPKT message, FriendlyByteBuf buffer) {
        buffer.writeJsonWithCodec(CODEC, message.map);
    }

    public static RequiredImagesSyncPKT decode(FriendlyByteBuf buffer) {
        return new RequiredImagesSyncPKT(buffer.readJsonWithCodec(CODEC));
    }

    public static class Handler {
        public static void handle(RequiredImagesSyncPKT message, Supplier<NetworkEvent.Context> context) {
            // TODO Actually do something useful with the data
            VFXGenerator.LOGGER.info("Received: {}", message.map);
        }
    }
}
