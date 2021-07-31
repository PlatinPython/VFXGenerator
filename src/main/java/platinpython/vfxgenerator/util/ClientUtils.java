package platinpython.vfxgenerator.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.client.gui.screen.VFXGeneratorScreen;
import platinpython.vfxgenerator.client.particle.VFXParticle;
import platinpython.vfxgenerator.tileentity.VFXGeneratorTileEntity;

@EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onTextureStich(TextureStitchEvent.Pre event) {
		if (event.getMap().location().equals(AtlasTexture.LOCATION_PARTICLES)) {
			event.addSprite(new ResourceLocation(VFXGenerator.MOD_ID, "particle/circle"));
			event.addSprite(new ResourceLocation(VFXGenerator.MOD_ID, "particle/square"));
		}
	}

	public static void addParticle(String spriteLocation, int color, int lifetime, float size, Vector3d pos, Vector3d motion, float gravity, boolean collision) {
		Minecraft minecraft = Minecraft.getInstance();
		VFXParticle particle = new VFXParticle(minecraft.level, minecraft.particleEngine.textureAtlas.getSprite(new ResourceLocation(VFXGenerator.MOD_ID, "particle/" + spriteLocation)), color, lifetime, size, pos, motion, gravity, collision);
		minecraft.particleEngine.add(particle);
	}

	public static void openVFXGeneratorScreen(VFXGeneratorTileEntity tileEntity) {
		Minecraft.getInstance().setScreen(new VFXGeneratorScreen(tileEntity));
	}
}
