package platinpython.vfxgenerator.util.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;

import java.util.function.Supplier;

public class VFXGeneratorDataSyncPKT {
    private final CompoundTag tag;
    private final BlockPos pos;

    public VFXGeneratorDataSyncPKT(CompoundTag tag, BlockPos pos) {
        this.tag = tag;
        this.pos = pos;
    }

    public static void encode(VFXGeneratorDataSyncPKT message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.tag);
        buffer.writeBlockPos(message.pos);
    }

    public static VFXGeneratorDataSyncPKT decode(FriendlyByteBuf buffer) {
        return new VFXGeneratorDataSyncPKT(buffer.readNbt(), buffer.readBlockPos());
    }

    public static class Handler {
        @SuppressWarnings("resource")
        public static void handle(VFXGeneratorDataSyncPKT message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                BlockEntity tileEntity = context.get().getSender().level().getBlockEntity(message.pos);
                if (tileEntity instanceof VFXGeneratorBlockEntity) {
                    ((VFXGeneratorBlockEntity) tileEntity).loadFromTag(message.tag);
                    tileEntity.setChanged();
                }
                context.get()
                       .getSender()
                       .level()
                       .sendBlockUpdated(message.pos, context.get().getSender().level().getBlockState(message.pos),
                                         context.get().getSender().level().getBlockState(message.pos), Block.UPDATE_ALL
                       );
            });
            context.get().setPacketHandled(true);
        }
    }
}
