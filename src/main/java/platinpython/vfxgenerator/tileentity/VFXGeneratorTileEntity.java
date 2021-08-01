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
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class VFXGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
	public boolean particleEnabled = true;
	public String particleSelected = "circle";
	public boolean particleUseHSB = false;
	public int particleRGBColorBot = 0xFF000000;
	public int particleRGBColorTop = 0xFFFFFFFF;
	public float[] particleHSBColorBot = { 0F, 0F, 0F };
	public float[] particleHSBColorTop = { 1F, 1F, 1F };
	public int particleLifetimeBot = 20;
	public int particleLifetimeTop = 80;
	public float particleSizeBot = 1F;
	public float particleSizeTop = 3F;
	public float particleSpawnXBot = -1F;
	public float particleSpawnXTop = 1F;
	public float particleSpawnYBot = 0F;
	public float particleSpawnYTop = 0F;
	public float particleSpawnZBot = -1F;
	public float particleSpawnZTop = 1F;
	public float particleMotionXBot = -0.1F;
	public float particleMotionXTop = 0.1F;
	public float particleMotionYBot = 0.1F;
	public float particleMotionYTop = 0.1F;
	public float particleMotionZBot = -0.1F;
	public float particleMotionZTop = 0.1F;
	public int particleDelay = 5;
	public float particleGravity = 0F;
	public boolean particleCollision = false;

	public VFXGeneratorTileEntity() {
		super(TileEntityRegistry.VFX_GENERATOR.get());
	}

	@Override
	public void tick() {
		if ((this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) && !this.getBlockState().getValue(VFXGeneratorBlock.POWERED)) || (!this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) && this.getBlockState().getValue(VFXGeneratorBlock.POWERED))) {
			World world = this.getLevel();
			if (world.isClientSide && this.particleEnabled) {
				if (world.getGameTime() % (particleDelay + 1) == 0) {
					Random random = world.getRandom();

					int color = 0xFF000000;
					if (particleUseHSB) {
						color = Color.getRandomHSBColor(random, particleHSBColorBot, particleHSBColorTop);
					} else {
						color = Color.getRandomRGBColor(random, particleRGBColorBot, particleRGBColorTop);
					}
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
	}

	public CompoundNBT saveToTag(CompoundNBT tag) {
		CompoundNBT particleTag = new CompoundNBT();
		particleTag.putBoolean("enabled", particleEnabled);
		particleTag.putString("selected", particleSelected);
		particleTag.putBoolean("useHSB", particleUseHSB);
		particleTag.putInt("RGBColorBot", particleRGBColorBot);
		particleTag.putInt("RGBColorTop", particleRGBColorTop);
		particleTag.putFloat("hueBot", particleHSBColorBot[0]);
		particleTag.putFloat("saturationBot", particleHSBColorBot[1]);
		particleTag.putFloat("brightnessBot", particleHSBColorBot[2]);
		particleTag.putFloat("hueTop", particleHSBColorTop[0]);
		particleTag.putFloat("saturationTop", particleHSBColorTop[1]);
		particleTag.putFloat("brightnessTop", particleHSBColorTop[2]);
		particleTag.putInt("lifetimeBot", particleLifetimeBot);
		particleTag.putInt("lifetimeTop", particleLifetimeTop);
		particleTag.putFloat("sizeBot", particleSizeBot);
		particleTag.putFloat("sizeTop", particleSizeTop);
		particleTag.putFloat("spawnXBot", particleSpawnXBot);
		particleTag.putFloat("spawnXTop", particleSpawnXTop);
		particleTag.putFloat("spawnYBot", particleSpawnYBot);
		particleTag.putFloat("spawnYTop", particleSpawnYTop);
		particleTag.putFloat("spawnZBot", particleSpawnZBot);
		particleTag.putFloat("spawnZTop", particleSpawnZTop);
		particleTag.putFloat("motionXBot", particleMotionXBot);
		particleTag.putFloat("motionXTop", particleMotionXTop);
		particleTag.putFloat("motionYBot", particleMotionYBot);
		particleTag.putFloat("motionYTop", particleMotionYTop);
		particleTag.putFloat("motionZBot", particleMotionZBot);
		particleTag.putFloat("motionZTop", particleMotionZTop);
		particleTag.putInt("delay", particleDelay);
		particleTag.putFloat("gravity", particleGravity);
		particleTag.putBoolean("collision", particleCollision);
		tag.put("particleData", particleTag);
		return tag;
	}

	public void loadFromTag(CompoundNBT tag) {
		CompoundNBT particleTag = tag.getCompound("particleData");
		particleEnabled = particleTag.getBoolean("enabled");
		particleSelected = particleTag.getString("selected");
		particleUseHSB = particleTag.getBoolean("useHSB");
		particleRGBColorBot = particleTag.getInt("RGBColorBot");
		particleRGBColorTop = particleTag.getInt("RGBColorTop");
		particleHSBColorBot[0] = particleTag.getFloat("hueBot");
		particleHSBColorBot[1] = particleTag.getFloat("saturationBot");
		particleHSBColorBot[2] = particleTag.getFloat("brightnessBot");
		particleHSBColorTop[0] = particleTag.getFloat("hueTop");
		particleHSBColorTop[1] = particleTag.getFloat("saturationTop");
		particleHSBColorTop[2] = particleTag.getFloat("brightnessTop");
		particleLifetimeBot = particleTag.getInt("lifetimeBot");
		particleLifetimeTop = particleTag.getInt("lifetimeTop");
		particleSizeBot = particleTag.getFloat("sizeBot");
		particleSizeTop = particleTag.getFloat("sizeTop");
		particleSpawnXBot = particleTag.getFloat("spawnXBot");
		particleSpawnXTop = particleTag.getFloat("spawnXTop");
		particleSpawnYBot = particleTag.getFloat("spawnYBot");
		particleSpawnYTop = particleTag.getFloat("spawnYTop");
		particleSpawnZBot = particleTag.getFloat("spawnZBot");
		particleSpawnZTop = particleTag.getFloat("spawnZTop");
		particleMotionXBot = particleTag.getFloat("motionXBot");
		particleMotionXTop = particleTag.getFloat("motionXTop");
		particleMotionYBot = particleTag.getFloat("motionYBot");
		particleMotionYTop = particleTag.getFloat("motionYTop");
		particleMotionZBot = particleTag.getFloat("motionZBot");
		particleMotionZTop = particleTag.getFloat("motionZTop");
		particleDelay = particleTag.getInt("delay");
		particleGravity = particleTag.getFloat("gravity");
		particleCollision = particleTag.getBoolean("collision");
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
