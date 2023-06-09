package platinpython.vfxgenerator.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class VFXGeneratorBlockItem extends BlockItem {
    public VFXGeneratorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @SuppressWarnings("resource")
    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        if (!itemEntity.level().isClientSide) {
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> itemEntity),
                                         new VFXGeneratorDestroyParticlesPKT(itemEntity.position())
            );
        }
    }
}
