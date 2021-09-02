package platinpython.vfxgenerator.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;

public class DestroyParticlesItemEntity extends ItemEntity {

	public DestroyParticlesItemEntity(World level, double x, double y, double z, double xd, double yd, double zd, int pickUpDelay, ItemStack stack) {
		super(level, x, y, z);
		this.setItem(stack);
		this.lifespan = stack.getItem() == null ? 6000 : stack.getEntityLifespan(level);
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
				this.remove();
				NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new VFXGeneratorDestroyParticlesPKT(this.position()));
			}

			return true;
		}
	}
}
