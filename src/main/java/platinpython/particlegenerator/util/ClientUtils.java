package platinpython.particlegenerator.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import platinpython.particlegenerator.ParticleGenerator;
import platinpython.particlegenerator.particle.TestParticle;

@EventBusSubscriber(modid = ParticleGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onTextureStich(TextureStitchEvent.Pre event) {
		if(event.getMap().location().equals(AtlasTexture.LOCATION_PARTICLES)) {
			event.addSprite(new ResourceLocation(ParticleGenerator.MOD_ID, "particle/test"));
		}
	}

	public static void addParticle(Vector3d pos) {
		Minecraft minecraft = Minecraft.getInstance();
		TestParticle particle = new TestParticle(minecraft.level, pos.x, pos.y, pos.z, minecraft.particleEngine.textureAtlas.getSprite(
//				new ResourceLocation("particle/smoke_0")));
				new ResourceLocation(ParticleGenerator.MOD_ID, "particle/test")));
		minecraft.particleEngine.add(particle);
	}
}
