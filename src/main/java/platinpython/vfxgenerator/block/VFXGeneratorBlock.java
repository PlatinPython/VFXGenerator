package platinpython.vfxgenerator.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class VFXGeneratorBlock extends Block {
	public static BooleanProperty INVERTED = BlockStateProperties.INVERTED;
	public static BooleanProperty POWERED = BlockStateProperties.POWERED;

	public VFXGeneratorBlock() {
		super(Properties.copy(Blocks.STONE).noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(INVERTED, POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		context.getItemInHand();
		return this.defaultBlockState().setValue(INVERTED, Boolean.FALSE).setValue(POWERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityRegistry.VFX_GENERATOR.get().create();
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide) {
			boolean isPowered = state.getValue(POWERED);
			if (isPowered != level.hasNeighborSignal(pos)) {
				if (isPowered) {
					level.getBlockTicks().scheduleTick(pos, this, 4);
				} else {
					level.setBlock(pos, state.cycle(POWERED), 2);
				}
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
			level.setBlock(pos, state.cycle(POWERED), 2);
		}
	}

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (player.getMainHandItem().isEmpty()) {
			if (player.isShiftKeyDown() && player.getOffhandItem().isEmpty()) {
				level.setBlock(pos, state.cycle(INVERTED), 2);
				return ActionResultType.SUCCESS;
			} else {
				if (level.isClientSide) {
					TileEntity tileEntity = level.getBlockEntity(pos);
					if (tileEntity instanceof VFXGeneratorTileEntity) {
						ClientUtils.openVFXGeneratorScreen((VFXGeneratorTileEntity) tileEntity);
						return ActionResultType.CONSUME;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}
}
