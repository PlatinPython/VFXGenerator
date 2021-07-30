package platinpython.vfxgenerator.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class ParticleGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
	public ParticleGeneratorTileEntity() {
		super(TileEntityRegistry.PARTICLE_GENERATOR.get());
	}

	@Override
	public void tick() {
		World world = this.getLevel();
		if (world.getGameTime() % 2 == 0) {
			if (world.isClientSide) {
				BlockPos pos = this.getBlockPos();
				ClientUtils.addParticle(Vector3d.atCenterOf(pos));
			}
		}
	}
}
