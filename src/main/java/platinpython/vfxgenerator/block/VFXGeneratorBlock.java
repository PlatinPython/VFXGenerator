package platinpython.vfxgenerator.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.network.packets.VFXGeneratorDestroyParticlesPKT;
import platinpython.vfxgenerator.util.registries.BlockEntityRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class VFXGeneratorBlock extends BaseEntityBlock {
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final String INVERTED_KEY = INVERTED.getName();

    public VFXGeneratorBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion());
        this.registerDefaultState(
            this.stateDefinition.any().setValue(INVERTED, Boolean.FALSE).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(INVERTED, POWERED);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getTagElement("particleData") != null) {
            tooltip.add(ClientUtils.getGuiTranslationTextComponent("dataSaved"));
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(
            POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.VFX_GENERATOR.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState state,
        BlockEntityType<T> blockEntityType
    ) {
        return level.isClientSide ? createTickerHelper(
            blockEntityType, BlockEntityRegistry.VFX_GENERATOR.get(), VFXGeneratorBlockEntity::tick) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block block,
        BlockPos fromPos,
        boolean isMoving
    ) {
        if (!level.isClientSide) {
            if (state.getValue(POWERED) != level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.cycle(POWERED), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
    }

    @Override
    public InteractionResult use(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit
    ) {
        if (player.getMainHandItem().isEmpty()) {
            if (player.isShiftKeyDown() && player.getOffhandItem().isEmpty()) {
                level.setBlock(pos, state.cycle(INVERTED), 2);
                return InteractionResult.SUCCESS;
            } else {
                if (level.isClientSide) {
                    BlockEntity tileEntity = level.getBlockEntity(pos);
                    if (tileEntity instanceof VFXGeneratorBlockEntity) {
                        ClientUtils.openVFXGeneratorScreen((VFXGeneratorBlockEntity) tileEntity);
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.getTagElement("particleData") != null) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof VFXGeneratorBlockEntity) {
                ((VFXGeneratorBlockEntity) tileEntity).loadFromTag(stack.getOrCreateTag());
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(
        BlockState state,
        HitResult target,
        BlockGetter world,
        BlockPos pos,
        Player player
    ) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag blockStateTag = new CompoundTag();
        blockStateTag.putString(INVERTED_KEY, state.getValue(INVERTED).toString());
        tag.put(BlockItem.BLOCK_STATE_TAG, blockStateTag);
        stack.setTag(tag);
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof VFXGeneratorBlockEntity) {
            stack.setTag(((VFXGeneratorBlockEntity) tileEntity).saveToTag(tag));
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            NetworkHandler.INSTANCE.send(
                PacketDistributor.ALL.noArg(), new VFXGeneratorDestroyParticlesPKT(Vec3.atCenterOf(pos)));
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
