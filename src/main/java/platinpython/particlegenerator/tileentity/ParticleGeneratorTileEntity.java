package platinpython.particlegenerator.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import platinpython.particlegenerator.util.network.PacketHandler;
import platinpython.particlegenerator.util.network.packets.AddParticlePKT;
import platinpython.particlegenerator.util.registries.TileEntityRegistry;

public class ParticleGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
	public ParticleGeneratorTileEntity() {
		super(TileEntityRegistry.PARTICLE_GENERATOR.get());
	}

	@Override
	public void tick() {
		World world = this.getLevel();
		if (world.getGameTime() % 1 == 0) {
			if (!world.isClientSide) {
				BlockPos pos = this.getBlockPos();
				PacketHandler.sendToAllClientsInDimension(new AddParticlePKT(Vector3d.atCenterOf(pos)), world.dimension());
			}
		}
	}
}
