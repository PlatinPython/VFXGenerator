package platinpython.particlegenerator.util.network.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import platinpython.particlegenerator.util.ClientUtils;

public class AddParticlePKT {
	private final Vector3d pos;

	public AddParticlePKT(Vector3d pos) {
		this.pos = pos;
	}

	public static void encode(AddParticlePKT message, PacketBuffer buffer) {
		buffer.writeDouble(message.pos.x);
		buffer.writeDouble(message.pos.y);
		buffer.writeDouble(message.pos.z);
	}

	public static AddParticlePKT decode(PacketBuffer buffer) {
		return new AddParticlePKT(new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
	}

	public static class Handler {
		public static void handle(AddParticlePKT message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				ClientUtils.addParticle(message.pos);
			});
			context.get().setPacketHandled(true);
		}
	}
}
