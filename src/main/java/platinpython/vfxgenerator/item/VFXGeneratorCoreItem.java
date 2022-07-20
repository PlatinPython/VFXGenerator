package platinpython.vfxgenerator.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public Entity createEntity(Level level, Entity itemEntity, ItemStack stack) {
        return new DestroyParticlesItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                                              itemEntity.getDeltaMovement().x, itemEntity.getDeltaMovement().y,
                                              itemEntity.getDeltaMovement().z, ((ItemEntity) itemEntity).pickupDelay,
                                              stack
        );
    }
}
