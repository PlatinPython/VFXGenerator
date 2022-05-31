package platinpython.vfxgenerator.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.PacketDistributor;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

import java.util.List;
import java.util.Random;

public class VFXGeneratorBlock extends Block {
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final String INVERTED_KEY = "inverted";

    public VFXGeneratorBlock() {
        super(Properties.copy(Blocks.STONE).noOcclusion());
        this.registerDefaultState(
                this.stateDefinition.any().setValue(INVERTED, Boolean.FALSE).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(INVERTED, POWERED);
    }

    @Override
    public void appendHoverText(ItemStack stack, IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (stack.getTagElement("particleData") != null) {
            tooltip.add(ClientUtils.getGuiTranslationTextComponent("dataSaved"));
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                   .setValue(POWERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
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
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos,
                                boolean isMoving) {
        if (!level.isClientSide) {
            if (state.getValue(POWERED) != level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.cycle(POWERED), BlockFlags.DEFAULT);
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
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult hit) {
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

    @Override
    public void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.getTagElement("particleData") != null) {
            TileEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof VFXGeneratorTileEntity) {
                ((VFXGeneratorTileEntity) tileEntity).loadFromTag(stack.getOrCreateTag());
            }
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
                                  PlayerEntity player) {
        ItemStack stack = new ItemStack(this);
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT blockStateTag = new CompoundNBT();
        blockStateTag.putString("inverted", state.getValue(INVERTED).toString());
        tag.put("BlockStateTag", blockStateTag);
        stack.setTag(tag);
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof VFXGeneratorTileEntity) {
            stack.setTag(((VFXGeneratorTileEntity) tileEntity).saveToTag(tag));
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock()))
            NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                                         new VFXGeneratorDestroyParticlesPKT(Vector3d.atCenterOf(pos))
            );
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
