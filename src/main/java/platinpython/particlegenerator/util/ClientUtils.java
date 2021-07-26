package platinpython.particlegenerator.util;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.util.network.PacketHandler;
import platinpython.particlegenerator.util.network.packets.AddParticlePKT;

//Most of the stuff in here is currently for testing purposes only
@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	private static final Field LIFE = ObfuscationReflectionHelper.findField(ItemPickupParticle.class, "field_70594_ar");

	@SubscribeEvent
	public static void doClientStuff(FMLClientSetupEvent event) {
	}

	public static void addParticle() {
		Minecraft minecraft = Minecraft.getInstance();
		ItemPickupParticle p = new ItemPickupParticle(minecraft.getEntityRenderDispatcher(), minecraft.renderBuffers(), minecraft.level, minecraft.player, minecraft.player);
		try {
			LIFE.set(p, -3);
		} catch (Exception e) {
		}
		minecraft.particleEngine.add(p);
	}

	@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.FORGE)
	public static class Test {
		@SubscribeEvent
		public static void onJump(LivingJumpEvent event) {
			LivingEntity e = event.getEntityLiving();
			if (e instanceof ServerPlayerEntity) {
				ParticleGenerator.LOGGER.info("HEII");
				PacketHandler.sendToAllClientsInDimension(new AddParticlePKT(), e.level.dimension());
			}
		}
	}
}
