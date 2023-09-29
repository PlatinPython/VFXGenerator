package platinpython.vfxgenerator.util.network.packets;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.resources.DataManager;

import java.util.function.Supplier;

public class SelectableParticlesSyncPKT {
    private final ImmutableMap<ResourceLocation, ParticleType> map;

    public SelectableParticlesSyncPKT(ImmutableMap<ResourceLocation, ParticleType> map) {
        this.map = map;
    }

    public static void encode(SelectableParticlesSyncPKT message, FriendlyByteBuf buffer) {
        buffer.writeJsonWithCodec(DataManager.SELECTABLE_PARTICLES_CODEC, message.map);
    }

    public static SelectableParticlesSyncPKT decode(FriendlyByteBuf buffer) {
        return new SelectableParticlesSyncPKT(buffer.readJsonWithCodec(DataManager.SELECTABLE_PARTICLES_CODEC));
    }

    public static class Handler {
        public static void handle(SelectableParticlesSyncPKT message, Supplier<NetworkEvent.Context> context) {
            DataManager.setSelectableParticles(message.map);
        }
    }
}
