package platinpython.particlegenerator.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.particle.TestParticle;

//Most of the stuff in here is currently for testing purposes only
@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SubscribeEvent
	public static void doClientStuff(FMLClientSetupEvent event) {
	}

	public static void addParticle(Vector3d pos) {
		Minecraft minecraft = Minecraft.getInstance();
		TestParticle p = new TestParticle(minecraft.level, pos.x, pos.y, pos.z, minecraft.particleEngine.textureAtlas.getSprite(new ResourceLocation("particle/heart")));
		minecraft.particleEngine.add(p);
	}

	@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.FORGE)
	public static class Test {
		@SubscribeEvent
		public static void onJump(LivingJumpEvent event) {
			LivingEntity e = event.getEntityLiving();
			if (e instanceof ServerPlayerEntity) {
			}
		}
	}
}
