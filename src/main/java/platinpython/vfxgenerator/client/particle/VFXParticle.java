package platinpython.vfxgenerator.client.particle;

import java.util.stream.Stream;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;

public class VFXParticle extends SpriteTexturedParticle {
	private boolean stoppedByCollision;

	public VFXParticle(ClientWorld clientWorld, TextureAtlasSprite sprite, int color, int lifetime, float size, Vector3d pos, Vector3d motion, float gravity, boolean collision) {
		super(clientWorld, pos.x, pos.y, pos.z);
		this.setSprite(sprite);
		this.rCol = (color >> 16 & 0xFF) / 255f;
		this.gCol = (color >> 8 & 0xFF) / 255f;
		this.bCol = (color >> 0 & 0xFF) / 255f;
		this.lifetime = lifetime;
		this.quadSize = size / 10;
		this.xd = motion.x;
		this.yd = motion.y;
		this.zd = motion.z;
		this.gravity = gravity;
		this.hasPhysics = collision;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.yd -= 0.02D * (double) this.gravity;
			this.move(this.xd, this.yd, this.zd);
		}
	}

	@Override
	public void move(double x, double y, double z) {
		if (!stoppedByCollision) {
			double dX = x;
			double dY = y;
			double dZ = z;
			if (this.hasPhysics && (x != 0.0D || y != 0.0D || z != 0.0D)) {
				Vector3d vector3d = Entity.collideBoundingBoxHeuristically((Entity) null, new Vector3d(x, y, z), this.getBoundingBox(), this.level, ISelectionContext.empty(), new ReuseableStream<>(Stream.empty()));
				x = vector3d.x;
				y = vector3d.y;
				z = vector3d.z;
			}

			if (x != 0.0D || y != 0.0D || z != 0.0D) {
				this.setBoundingBox(this.getBoundingBox().move(x, y, z));
				this.setLocationFromBoundingbox();
			} else {
				this.stoppedByCollision = true;
			}

			if (dX != x) {
				this.xd = 0.0D;
			}

			if (dY != y) {
				this.yd = 0.0D;
			}

			if (dZ != z) {
				this.zd = 0.0D;
			}
		}
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
}
