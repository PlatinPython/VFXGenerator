package platinpython.vfxgenerator.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class VFXGeneratorCoreItem extends Item {
    public VFXGeneratorCoreItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        //noinspection resource
        if (!itemEntity.level().isClientSide) {
            NetworkHandler.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> itemEntity),
                new VFXGeneratorDestroyParticlesPKT(itemEntity.position())
            );
        }
    }
}
