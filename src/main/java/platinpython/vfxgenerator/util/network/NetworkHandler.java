package platinpython.vfxgenerator.util.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDataSyncPKT;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VFXGenerator.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int index = 0;
        INSTANCE.registerMessage(index++, VFXGeneratorDataSyncPKT.class, VFXGeneratorDataSyncPKT::encode,
                                 VFXGeneratorDataSyncPKT::decode, VFXGeneratorDataSyncPKT.Handler::handle
        );
        INSTANCE.registerMessage(index++, VFXGeneratorDestroyParticlesPKT.class,
                                 VFXGeneratorDestroyParticlesPKT::encode, VFXGeneratorDestroyParticlesPKT::decode,
                                 VFXGeneratorDestroyParticlesPKT.Handler::handle
        );
    }
}
