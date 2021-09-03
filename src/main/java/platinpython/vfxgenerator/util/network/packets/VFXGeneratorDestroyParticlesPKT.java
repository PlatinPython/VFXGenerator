package platinpython.vfxgenerator.util.network.packets;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;

public class VFXGeneratorDestroyParticlesPKT {
	private final Vector3d pos;

	public VFXGeneratorDestroyParticlesPKT(Vector3d pos) {
		this.pos = pos;
	}

	public static void encode(VFXGeneratorDestroyParticlesPKT message, PacketBuffer buffer) {
		buffer.writeDouble(message.pos.x);
		buffer.writeDouble(message.pos.y);
		buffer.writeDouble(message.pos.z);
	}

	public static VFXGeneratorDestroyParticlesPKT decode(PacketBuffer buffer) {
		return new VFXGeneratorDestroyParticlesPKT(new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
	}

	public static class Handler {
		public static void handle(VFXGeneratorDestroyParticlesPKT message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				if (context.get().getDirection().getOriginationSide().isServer()) {
					Random random = new Random();
					for (int i = 0; i < 100; i++) {
						double motionX = (random.nextFloat() * (.2F)) - .1F;
						double motionY = (random.nextFloat() * (.2F)) - .1F;
						double motionZ = (random.nextFloat() * (.2F)) - .1F;
						Vector3d motion = new Vector3d(motionX, motionY, motionZ);
						ClientUtils.addParticle("circle", Color.HSBtoRGB(random.nextFloat(), 1F, 1F), Math.round(5 + (random.nextFloat() * (15 - 5))), .3F, message.pos, motion, 0F, false, false);
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
