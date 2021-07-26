package platinpython.particlegenerator.util.network;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.util.network.packets.AddParticlePKT;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ParticleGenerator.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void register() {
		int index = 0;
		INSTANCE.registerMessage(index++, AddParticlePKT.class, AddParticlePKT::encode, AddParticlePKT::decode, AddParticlePKT.Handler::handle);
	}

	public static void sendToAllClientsInDimension(Object message, RegistryKey<World> dimension) {
		INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
	}
}
