package platinpython.vfxgenerator.util.network.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkEvent;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;

import java.util.function.Supplier;

public class VFXGeneratorDataSyncPKT {
    private final CompoundNBT tag;
    private final BlockPos pos;

    public VFXGeneratorDataSyncPKT(CompoundNBT tag, BlockPos pos) {
        this.tag = tag;
        this.pos = pos;
    }

    public static void encode(VFXGeneratorDataSyncPKT message, PacketBuffer buffer) {
        buffer.writeNbt(message.tag);
        buffer.writeBlockPos(message.pos);
    }

    public static VFXGeneratorDataSyncPKT decode(PacketBuffer buffer) {
        return new VFXGeneratorDataSyncPKT(buffer.readNbt(), buffer.readBlockPos());
    }

    public static class Handler {
        public static void handle(VFXGeneratorDataSyncPKT message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                TileEntity tileEntity = context.get().getSender().getLevel().getBlockEntity(message.pos);
                if (tileEntity instanceof VFXGeneratorTileEntity) {
                    ((VFXGeneratorTileEntity) tileEntity).loadFromTag(message.tag);
                    tileEntity.setChanged();
                }
                context.get()
                       .getSender()
                       .getLevel()
                       .sendBlockUpdated(message.pos,
                                         context.get().getSender().getLevel().getBlockState(message.pos),
                                         context.get().getSender().getLevel().getBlockState(message.pos),
                                         BlockFlags.DEFAULT);
            });
            context.get().setPacketHandled(true);
        }
    }
}
