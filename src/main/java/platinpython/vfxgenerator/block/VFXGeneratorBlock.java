package platinpython.vfxgenerator.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class VFXGeneratorBlock extends Block {
	public VFXGeneratorBlock() {
		super(Properties.copy(Blocks.STONE).noOcclusion());
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState pState, IBlockReader pReader, BlockPos pPos) {
		return true;
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
