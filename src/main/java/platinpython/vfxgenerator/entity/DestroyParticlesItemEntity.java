package platinpython.vfxgenerator.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class DestroyParticlesItemEntity extends ItemEntity {

    public DestroyParticlesItemEntity(Level level, double x, double y, double z, double xd, double yd, double zd,
                                      int pickUpDelay, ItemStack stack) {
        super(level, x, y, z, stack);
        this.lifespan = stack.getEntityLifespan(level);
        this.setPickUpDelay(pickUpDelay);
        this.setDeltaMovement(xd, yd, zd);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level.isClientSide || !this.isAlive()) {
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) {
            return false;
        } else if (!this.getItem().getItem().canBeHurtBy(source)) {
            return false;
        } else {
            this.markHurt();
            this.health = (int) (this.health - amount);
            if (this.health <= 0) {
                this.remove(RemovalReason.KILLED);
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
                                             new VFXGeneratorDestroyParticlesPKT(this.position())
                );
            }

            return true;
        }
    }
}
