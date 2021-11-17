package platinpython.vfxgenerator.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import platinpython.vfxgenerator.entity.DestroyParticlesItemEntity;

public class VFXGeneratorCoreItem extends Item {
    public VFXGeneratorCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World level, Entity itemEntity, ItemStack stack) {
        return new DestroyParticlesItemEntity(level,
                                              itemEntity.getX(),
                                              itemEntity.getY(),
                                              itemEntity.getZ(),
                                              itemEntity.getDeltaMovement().x,
                                              itemEntity.getDeltaMovement().y,
                                              itemEntity.getDeltaMovement().z,
                                              ((ItemEntity) itemEntity).pickupDelay,
                                              stack);
    }
}
