package platinpython.particlegenerator.util.network.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import platinpython.particlegenerator.util.ClientUtils;

public class AddParticlePKT {
	public AddParticlePKT() {
	}

	public static void encode(AddParticlePKT message, PacketBuffer buffer) {
	}

	public static AddParticlePKT decode(PacketBuffer buffer) {
		return new AddParticlePKT();
	}

	public static class Handler {
		public static void handle(AddParticlePKT message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				ClientUtils.addParticle();
			});
			context.get().setPacketHandled(true);
		}
	}
}
