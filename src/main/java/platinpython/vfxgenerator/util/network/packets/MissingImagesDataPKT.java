package platinpython.vfxgenerator.util.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.util.resources.client.CacheHandler;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MissingImagesDataPKT {
    private final boolean last;
    private final byte[] data;

    public MissingImagesDataPKT(boolean last, byte[] data) {
        this.last = last;
        this.data = data;
    }

    public static void encode(MissingImagesDataPKT message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.last);
        buffer.writeByteArray(message.data);
    }

    public static MissingImagesDataPKT decode(FriendlyByteBuf buffer) {
        return new MissingImagesDataPKT(buffer.readBoolean(), buffer.readByteArray());
    }

    public static class Handler {
        private static final ArrayList<MissingImagesDataPKT> MESSAGES = new ArrayList<>();

        public static void handle(MissingImagesDataPKT message, Supplier<NetworkEvent.Context> context) {
            MESSAGES.add(message);
            if (!message.last) {
                return;
            }
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            MESSAGES.forEach(packet -> buffer.writeBytes(packet.data));
            int numberOfElements = buffer.readInt();
            for (int i = 0; i < numberOfElements; i++) {
                CacheHandler.addToCache(buffer.readResourceLocation(), buffer.readByteArray());
            }
            MESSAGES.clear();
        }
    }
}
