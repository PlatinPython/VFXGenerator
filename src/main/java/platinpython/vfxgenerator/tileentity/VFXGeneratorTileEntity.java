package platinpython.vfxgenerator.tileentity;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class VFXGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
	public boolean particleEnabled = true;
	public String particleSelected = "circle";
	public int particleColorBot = 0xFF000000;
	public int particleColorTop = 0xFF000000;
	public int particleLifetimeBot = 100;
	public int particleLifetimeTop = 100;
	public float particleSizeBot = 5F;
	public float particleSizeTop = 5F;
	public float particleSpawnXBot = 0F;
	public float particleSpawnXTop = 0F;
	public float particleSpawnYBot = 0F;
	public float particleSpawnYTop = 0F;
	public float particleSpawnZBot = 0F;
	public float particleSpawnZTop = 0F;
	public float particleMotionXBot = 0F;
	public float particleMotionXTop = 0F;
	public float particleMotionYBot = 0F;
	public float particleMotionYTop = 0F;
	public float particleMotionZBot = 0F;
	public float particleMotionZTop = 0F;
	public int particleDelay = 1;
	public float particleGravity = 0F;
	public boolean particleCollision = false;

	public VFXGeneratorTileEntity() {
		super(TileEntityRegistry.VFX_GENERATOR.get());
	}

	@Override
	public void tick() {
		World world = this.getLevel();
		if (world.isClientSide && this.particleEnabled) {
			if (world.getGameTime() % (particleDelay + 1) == 0) {
				Random random = world.getRandom();

				int color = Color.getRandomColor(random, particleColorBot, particleColorTop);
				int lifetime = Math.round((particleLifetimeBot + (random.nextFloat() * (particleLifetimeTop - particleLifetimeBot))));
				float size = particleSizeBot + (random.nextFloat() * (particleSizeTop - particleSizeBot));

				Vector3d pos = Vector3d.atCenterOf(this.getBlockPos());
				double spawnX = pos.x + particleSpawnXBot + (random.nextFloat() * (particleSpawnXTop - particleSpawnXBot));
				double spawnY = pos.y + particleSpawnYBot + (random.nextFloat() * (particleSpawnYTop - particleSpawnYBot));
				double spawnZ = pos.z + particleSpawnZBot + (random.nextFloat() * (particleSpawnZTop - particleSpawnZBot));
				pos = new Vector3d(spawnX, spawnY, spawnZ);

				double motionX = particleMotionXBot + (random.nextFloat() * (particleMotionXTop - particleMotionXBot));
				double motionY = particleMotionYBot + (random.nextFloat() * (particleMotionYTop - particleMotionYBot));
				double motionZ = particleMotionZBot + (random.nextFloat() * (particleMotionZTop - particleMotionZBot));
				Vector3d motion = new Vector3d(motionX, motionY, motionZ);

				ClientUtils.addParticle(particleSelected, color, lifetime, size, pos, motion, particleGravity, particleCollision);
			}
		}
	}

	private CompoundNBT saveToTag(CompoundNBT tag) {
		tag.putBoolean("particleEnabled", particleEnabled);
		tag.putString("particleSelected", particleSelected);
		tag.putInt("particleColorBot", particleColorBot);
		tag.putInt("particleColorTop", particleColorTop);
		tag.putInt("particleLifetimeBot", particleLifetimeBot);
		tag.putInt("particleLifetimeTop", particleLifetimeTop);
		tag.putFloat("particleSizeBot", particleSizeBot);
		tag.putFloat("particleSizeTop", particleSizeTop);
		tag.putFloat("particleSpawnXBot", particleSpawnXBot);
		tag.putFloat("particleSpawnXTop", particleSpawnXTop);
		tag.putFloat("particleSpawnYBot", particleSpawnYBot);
		tag.putFloat("particleSpawnYTop", particleSpawnYTop);
		tag.putFloat("particleSpawnZBot", particleSpawnZBot);
		tag.putFloat("particleSpawnZTop", particleSpawnZTop);
		tag.putFloat("particleMotionXBot", particleMotionXBot);
		tag.putFloat("particleMotionXTop", particleMotionXTop);
		tag.putFloat("particleMotionYBot", particleMotionYBot);
		tag.putFloat("particleMotionYTop", particleMotionYTop);
		tag.putFloat("particleMotionZBot", particleMotionZBot);
		tag.putFloat("particleMotionZTop", particleMotionZTop);
		tag.putInt("particleDelay", particleDelay);
		tag.putFloat("particleGravity", particleGravity);
		tag.putBoolean("particleCollision", particleCollision);
		return tag;
	}

	private void loadFromTag(CompoundNBT tag) {
		particleEnabled = tag.getBoolean("particleEnabled");
		particleSelected = tag.getString("particleSelected");
		particleColorBot = tag.getInt("particleColorBot");
		particleColorTop = tag.getInt("particleColorTop");
		particleLifetimeBot = tag.getInt("particleLifetimeBot");
		particleLifetimeTop = tag.getInt("particleLifetimeTop");
		particleSizeBot = tag.getFloat("particleSizeBot");
		particleSizeTop = tag.getFloat("particleSizeTop");
		particleSpawnXBot = tag.getFloat("particleSpawnXBot");
		particleSpawnXTop = tag.getFloat("particleSpawnXTop");
		particleSpawnYBot = tag.getFloat("particleSpawnYBot");
		particleSpawnYTop = tag.getFloat("particleSpawnYTop");
		particleSpawnZBot = tag.getFloat("particleSpawnZBot");
		particleSpawnZTop = tag.getFloat("particleSpawnZTop");
		particleMotionXBot = tag.getFloat("particleMotionXBot");
		particleMotionXTop = tag.getFloat("particleMotionXTop");
		particleMotionYBot = tag.getFloat("particleMotionYBot");
		particleMotionYTop = tag.getFloat("particleMotionYTop");
		particleMotionZBot = tag.getFloat("particleMotionZBot");
		particleMotionZTop = tag.getFloat("particleMotionZTop");
		particleDelay = tag.getInt("particleDelay");
		particleGravity = tag.getFloat("particleGravity");
		particleCollision = tag.getBoolean("particleCollision");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		return super.save(saveToTag(tag));
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		loadFromTag(tag);
		super.load(state, tag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return saveToTag(super.getUpdateTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		loadFromTag(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), -1, saveToTag(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		loadFromTag(pkt.getTag());
	}
}
