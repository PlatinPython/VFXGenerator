package platinpython.particlegenerator.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import platinpython.particlegenerator.util.registries.TileEntityRegistry;

public class ParticleGeneratorBlock extends Block {
	public ParticleGeneratorBlock() {
		super(Properties.copy(Blocks.STONE).noOcclusion());
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityRegistry.PARTICLE_GENERATOR.get().create();
	}
}
