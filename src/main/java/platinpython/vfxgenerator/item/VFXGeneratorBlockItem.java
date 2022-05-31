package platinpython.vfxgenerator.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import platinpython.vfxgenerator.entity.DestroyParticlesItemEntity;

public class VFXGeneratorBlockItem extends BlockItem {
    public VFXGeneratorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World level, Entity itemEntity, ItemStack stack) {
        return new DestroyParticlesItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                                              itemEntity.getDeltaMovement().x, itemEntity.getDeltaMovement().y,
                                              itemEntity.getDeltaMovement().z, ((ItemEntity) itemEntity).pickupDelay,
                                              stack
        );
    }
}
