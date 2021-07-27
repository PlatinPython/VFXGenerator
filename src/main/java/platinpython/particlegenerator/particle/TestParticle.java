package platinpython.particlegenerator.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;

public class TestParticle extends SpriteTexturedParticle {
	public TestParticle(ClientWorld clientWorld, double x, double y, double z, TextureAtlasSprite sprite) {
		super(clientWorld, x, y, z);
		this.setSprite(sprite);
		this.yd = 1f;
		this.xd = 5f;
		this.gravity = 0.2F;
		this.lifetime = 100;
		this.hasPhysics = true;
	}

	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public float getQuadSize(float p_217561_1_) {
		return 0.5F;
	}
}
