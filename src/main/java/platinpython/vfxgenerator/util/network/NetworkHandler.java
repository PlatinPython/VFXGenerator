package platinpython.vfxgenerator.util.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import platinpython.vfxgenerator.util.Util;
import platinpython.vfxgenerator.util.network.packets.MissingImagesDataPKT;
import platinpython.vfxgenerator.util.network.packets.MissingImagesPKT;
import platinpython.vfxgenerator.util.network.packets.RequiredImageHashesPKT;
import platinpython.vfxgenerator.util.network.packets.SelectableParticlesSyncPKT;
import platinpython.vfxgenerator.util.network.packets.UpdateRequiredImagesPKT;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDataSyncPKT;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Util.createNamespacedResourceLocation("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int index = 0;
        INSTANCE.messageBuilder(VFXGeneratorDataSyncPKT.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(VFXGeneratorDataSyncPKT::encode)
                .decoder(VFXGeneratorDataSyncPKT::decode)
                .consumerMainThread(VFXGeneratorDataSyncPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(VFXGeneratorDestroyParticlesPKT.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(VFXGeneratorDestroyParticlesPKT::encode)
                .decoder(VFXGeneratorDestroyParticlesPKT::decode)
                .consumerMainThread(VFXGeneratorDestroyParticlesPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(SelectableParticlesSyncPKT.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SelectableParticlesSyncPKT::encode)
                .decoder(SelectableParticlesSyncPKT::decode)
                .consumerMainThread(SelectableParticlesSyncPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(RequiredImageHashesPKT.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(RequiredImageHashesPKT::encode)
                .decoder(RequiredImageHashesPKT::decode)
                .consumerMainThread(RequiredImageHashesPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(MissingImagesPKT.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(MissingImagesPKT::encode)
                .decoder(MissingImagesPKT::decode)
                .consumerMainThread(MissingImagesPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(MissingImagesDataPKT.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MissingImagesDataPKT::encode)
                .decoder(MissingImagesDataPKT::decode)
                .consumerMainThread(MissingImagesDataPKT.Handler::handle)
                .add();
        INSTANCE.messageBuilder(UpdateRequiredImagesPKT.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateRequiredImagesPKT::encode)
                .decoder(UpdateRequiredImagesPKT::decode)
                .consumerMainThread(UpdateRequiredImagesPKT.Handler::handle)
                .add();
    }
}
