package platinpython.particlegenerator.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;

public class TestParticle extends SpriteTexturedParticle {
	public TestParticle(ClientWorld clientWorld, double x, double y, double z, TextureAtlasSprite sprite) {
		super(clientWorld, x, y, z);
		this.setSprite(sprite);

		// Random testing stuff
		this.yd = 0.5f;
		this.xd = 0.5f;
		this.gravity = 0.2F;
		this.lifetime = 200;
		this.hasPhysics = true;
		this.quadSize = 1.0f;
		this.scale(0.5f);
		this.rCol = 0f;
		this.gCol = 0f;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public boolean shouldCull() {
		return false;
	}
}
