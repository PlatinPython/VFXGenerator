package platinpython.vfxgenerator.tileentity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.Constants.ParticleConstants;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

public class VFXGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
	private boolean particleEnabled = true;
	private List<String> particleSelected = Arrays.asList("circle", "square");
	private boolean particleUseHSB = false;
	private int particleRGBColorBot = 0xFF000000;
	private int particleRGBColorTop = 0xFFFFFFFF;
	private float[] particleHSBColorBot = { 0F, 0F, 0F };
	private float[] particleHSBColorTop = { 1F, 1F, 1F };
	private int particleLifetimeBot = 20;
	private int particleLifetimeTop = 80;
	private float particleSizeBot = 1F;
	private float particleSizeTop = 3F;
	private float particleSpawnXBot = -1F;
	private float particleSpawnXTop = 1F;
	private float particleSpawnYBot = 0F;
	private float particleSpawnYTop = 0F;
	private float particleSpawnZBot = -1F;
	private float particleSpawnZTop = 1F;
	private float particleMotionXBot = -0.1F;
	private float particleMotionXTop = 0.1F;
	private float particleMotionYBot = 0.1F;
	private float particleMotionYTop = 0.1F;
	private float particleMotionZBot = -0.1F;
	private float particleMotionZTop = 0.1F;
	private int particleDelay = 5;
	private float particleGravity = 0F;
	private boolean particleCollision = false;
	private boolean particleFullbright = true;

	public VFXGeneratorTileEntity() {
		super(TileEntityRegistry.VFX_GENERATOR.get());
	}

	@Override
	public void tick() {
		if ((this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) && !this.getBlockState().getValue(VFXGeneratorBlock.POWERED)) || (!this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) && this.getBlockState().getValue(VFXGeneratorBlock.POWERED))) {
			World world = this.getLevel();
			if (world.isClientSide && this.particleEnabled) {
				if (world.getGameTime() % particleDelay == 0) {
					Random random = world.getRandom();

					String particle = particleSelected.get(random.nextInt(particleSelected.size()));

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

					ClientUtils.addParticle(particle, color, lifetime, size, pos, motion, particleGravity, particleCollision, particleFullbright);
				}
			}
		}
	}

	public CompoundNBT saveToTag(CompoundNBT tag) {
		tag.put("particleData", saveToParticleTag());
		return tag;
	}

	private CompoundNBT saveToParticleTag() {
		CompoundNBT particleTag = new CompoundNBT();
		particleTag.putBoolean("enabled", isParticleEnabled());
		ListNBT listNBT = new ListNBT();
		getParticleSelected().forEach((s) -> listNBT.add(StringNBT.valueOf(s)));
		particleTag.put("selected", listNBT);
		particleTag.putBoolean("useHSB", isParticleUseHSB());
		particleTag.putInt("RGBColorBot", getParticleRGBColorBot());
		particleTag.putInt("RGBColorTop", getParticleRGBColorTop());
		particleTag.putFloat("hueBot", getParticleHSBColorBot()[0]);
		particleTag.putFloat("saturationBot", getParticleHSBColorBot()[1]);
		particleTag.putFloat("brightnessBot", getParticleHSBColorBot()[2]);
		particleTag.putFloat("hueTop", getParticleHSBColorTop()[0]);
		particleTag.putFloat("saturationTop", getParticleHSBColorTop()[1]);
		particleTag.putFloat("brightnessTop", getParticleHSBColorTop()[2]);
		particleTag.putInt("lifetimeBot", getParticleLifetimeBot());
		particleTag.putInt("lifetimeTop", getParticleLifetimeTop());
		particleTag.putFloat("sizeBot", getParticleSizeBot());
		particleTag.putFloat("sizeTop", getParticleSizeTop());
		particleTag.putFloat("spawnXBot", getParticleSpawnXBot());
		particleTag.putFloat("spawnXTop", getParticleSpawnXTop());
		particleTag.putFloat("spawnYBot", getParticleSpawnYBot());
		particleTag.putFloat("spawnYTop", getParticleSpawnYTop());
		particleTag.putFloat("spawnZBot", getParticleSpawnZBot());
		particleTag.putFloat("spawnZTop", getParticleSpawnZTop());
		particleTag.putFloat("motionXBot", getParticleMotionXBot());
		particleTag.putFloat("motionXTop", getParticleMotionXTop());
		particleTag.putFloat("motionYBot", getParticleMotionYBot());
		particleTag.putFloat("motionYTop", getParticleMotionYTop());
		particleTag.putFloat("motionZBot", getParticleMotionZBot());
		particleTag.putFloat("motionZTop", getParticleMotionZTop());
		particleTag.putInt("delay", getParticleDelay());
		particleTag.putFloat("gravity", getParticleGravity());
		particleTag.putBoolean("collision", isParticleCollision());
		particleTag.putBoolean("fullbright", isParticleFullbright());
		return particleTag;
	}

	public void loadFromTag(CompoundNBT tag) {
		loadFromParticleTag(tag.getCompound("particleData"));
	}

	private void loadFromParticleTag(CompoundNBT particleTag) {
		particleEnabled = particleTag.getBoolean("enabled");
		if (particleTag.getTagType("selected") == Constants.NBT.TAG_LIST) {
			particleSelected = ((ListNBT) particleTag.get("selected")).stream().map((nbt) -> nbt.getAsString()).filter((string) -> ParticleConstants.PARTICLE_OPTIONS.contains(string)).collect(Collectors.toList());
		} else {
			particleSelected = Arrays.asList(ParticleConstants.PARTICLE_OPTIONS.contains(particleTag.getString("selected")) ? particleTag.getString("selected") : "circle");
		}
		particleUseHSB = particleTag.getBoolean("useHSB");
		particleRGBColorBot = MathHelper.clamp(particleTag.getInt("RGBColorBot"), 0xFF000000, 0xFFFFFFFF);
		particleRGBColorTop = MathHelper.clamp(particleTag.getInt("RGBColorTop"), 0xFF000000, 0xFFFFFFFF);
		particleHSBColorBot[0] = MathHelper.clamp(particleTag.getFloat("hueBot"), 0F, 1F);
		particleHSBColorBot[1] = MathHelper.clamp(particleTag.getFloat("saturationBot"), 0F, 1F);
		particleHSBColorBot[2] = MathHelper.clamp(particleTag.getFloat("brightnessBot"), 0F, 1F);
		particleHSBColorTop[0] = MathHelper.clamp(particleTag.getFloat("hueTop"), 0F, 1F);
		particleHSBColorTop[1] = MathHelper.clamp(particleTag.getFloat("saturationTop"), 0F, 1F);
		particleHSBColorTop[2] = MathHelper.clamp(particleTag.getFloat("brightnessTop"), 0F, 1F);
		particleLifetimeBot = MathHelper.clamp(particleTag.getInt("lifetimeBot"), ParticleConstants.MIN_LIFETIME, ParticleConstants.MAX_LIFETIME);
		particleLifetimeTop = MathHelper.clamp(particleTag.getInt("lifetimeTop"), ParticleConstants.MIN_LIFETIME, ParticleConstants.MAX_LIFETIME);
		particleSizeBot = MathHelper.clamp(particleTag.getFloat("sizeBot"), ParticleConstants.MIN_SIZE, ParticleConstants.MAX_SIZE);
		particleSizeTop = MathHelper.clamp(particleTag.getFloat("sizeTop"), ParticleConstants.MIN_SIZE, ParticleConstants.MAX_SIZE);
		particleSpawnXBot = MathHelper.clamp(particleTag.getFloat("spawnXBot"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleSpawnXTop = MathHelper.clamp(particleTag.getFloat("spawnXTop"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleSpawnYBot = MathHelper.clamp(particleTag.getFloat("spawnYBot"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleSpawnYTop = MathHelper.clamp(particleTag.getFloat("spawnYTop"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleSpawnZBot = MathHelper.clamp(particleTag.getFloat("spawnZBot"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleSpawnZTop = MathHelper.clamp(particleTag.getFloat("spawnZTop"), ParticleConstants.MIN_SPAWN, ParticleConstants.MAX_SPAWN);
		particleMotionXBot = MathHelper.clamp(particleTag.getFloat("motionXBot"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleMotionXTop = MathHelper.clamp(particleTag.getFloat("motionXTop"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleMotionYBot = MathHelper.clamp(particleTag.getFloat("motionYBot"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleMotionYTop = MathHelper.clamp(particleTag.getFloat("motionYTop"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleMotionZBot = MathHelper.clamp(particleTag.getFloat("motionZBot"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleMotionZTop = MathHelper.clamp(particleTag.getFloat("motionZTop"), ParticleConstants.MIN_MOTION, ParticleConstants.MAX_MOTION);
		particleDelay = MathHelper.clamp(particleTag.getInt("delay"), ParticleConstants.MIN_DELAY, ParticleConstants.MAX_DELAY);
		particleGravity = MathHelper.clamp(particleTag.getFloat("gravity"), ParticleConstants.MIN_GRAVITY, ParticleConstants.MAX_GRAVITY);
		particleCollision = particleTag.getBoolean("collision");
		particleFullbright = particleTag.getBoolean("fullbright");

		ensureParticleDataOrder();
	}

	private void ensureParticleDataOrder() {
		particleRGBColorBot = MathHelper.clamp(getParticleRGBColorBot(), 0xFF000000, getParticleRGBColorTop());
		particleRGBColorTop = MathHelper.clamp(getParticleRGBColorTop(), getParticleRGBColorBot(), 0xFFFFFFFF);
		particleHSBColorBot[0] = MathHelper.clamp(getParticleHSBColorBot()[0], 0F, getParticleHSBColorTop()[0]);
		particleHSBColorBot[1] = MathHelper.clamp(getParticleHSBColorBot()[1], 0F, getParticleHSBColorTop()[1]);
		particleHSBColorBot[2] = MathHelper.clamp(getParticleHSBColorBot()[2], 0F, getParticleHSBColorTop()[2]);
		particleHSBColorTop[0] = MathHelper.clamp(getParticleHSBColorTop()[0], getParticleHSBColorBot()[0], 1F);
		particleHSBColorTop[1] = MathHelper.clamp(getParticleHSBColorTop()[1], getParticleHSBColorBot()[1], 1F);
		particleHSBColorTop[2] = MathHelper.clamp(getParticleHSBColorTop()[2], getParticleHSBColorBot()[2], 1F);
		particleLifetimeBot = MathHelper.clamp(getParticleLifetimeBot(), ParticleConstants.MIN_LIFETIME, getParticleLifetimeTop());
		particleLifetimeTop = MathHelper.clamp(getParticleLifetimeTop(), getParticleLifetimeBot(), ParticleConstants.MAX_LIFETIME);
		particleSizeBot = MathHelper.clamp(getParticleSizeBot(), ParticleConstants.MIN_SIZE, getParticleSizeTop());
		particleSizeTop = MathHelper.clamp(getParticleSizeTop(), getParticleSizeBot(), ParticleConstants.MAX_SIZE);
		particleSpawnXBot = MathHelper.clamp(getParticleSpawnXBot(), ParticleConstants.MIN_SPAWN, getParticleSpawnXTop());
		particleSpawnXTop = MathHelper.clamp(getParticleSpawnXTop(), getParticleSpawnXBot(), ParticleConstants.MAX_SPAWN);
		particleSpawnYBot = MathHelper.clamp(getParticleSpawnYBot(), ParticleConstants.MIN_SPAWN, getParticleSpawnYTop());
		particleSpawnYTop = MathHelper.clamp(getParticleSpawnYTop(), getParticleSpawnYBot(), ParticleConstants.MAX_SPAWN);
		particleSpawnZBot = MathHelper.clamp(getParticleSpawnZBot(), ParticleConstants.MIN_SPAWN, getParticleSpawnZTop());
		particleSpawnZTop = MathHelper.clamp(getParticleSpawnZTop(), getParticleSpawnZBot(), ParticleConstants.MAX_SPAWN);
		particleMotionXBot = MathHelper.clamp(getParticleMotionXBot(), ParticleConstants.MIN_MOTION, getParticleMotionXTop());
		particleMotionXTop = MathHelper.clamp(getParticleMotionXTop(), getParticleMotionXBot(), ParticleConstants.MAX_MOTION);
		particleMotionYBot = MathHelper.clamp(getParticleMotionYBot(), ParticleConstants.MIN_MOTION, getParticleMotionYTop());
		particleMotionYTop = MathHelper.clamp(getParticleMotionYTop(), getParticleMotionYBot(), ParticleConstants.MAX_MOTION);
		particleMotionZBot = MathHelper.clamp(getParticleMotionZBot(), ParticleConstants.MIN_MOTION, getParticleMotionZTop());
		particleMotionZTop = MathHelper.clamp(getParticleMotionZTop(), getParticleMotionZBot(), ParticleConstants.MAX_MOTION);
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

	public boolean isParticleEnabled() {
		return particleEnabled;
	}

	public void setParticleEnabled(boolean particleEnabled) {
		this.particleEnabled = particleEnabled;
		setChanged();
	}

	public List<String> getParticleSelected() {
		return particleSelected;
	}

	public void setParticleSelected(List<String> particleSelected) {
		this.particleSelected = particleSelected.stream().filter((s) -> ParticleConstants.PARTICLE_OPTIONS.contains(s)).collect(Collectors.toList());
		if (this.particleSelected.isEmpty())
			this.particleSelected = Arrays.asList("circle");
		setChanged();
	}

	public boolean isParticleUseHSB() {
		return particleUseHSB;
	}

	public void setParticleUseHSB(boolean particleUseHSB) {
		this.particleUseHSB = particleUseHSB;
		setChanged();
	}

	public int getParticleRGBColorBot() {
		return particleRGBColorBot;
	}

	public void setParticleRGBColorBot(int particleRGBColorBot) {
		this.particleRGBColorBot = MathHelper.clamp(particleRGBColorBot, 0xFF000000, getParticleRGBColorTop());
		setChanged();
	}

	public int getParticleRGBColorTop() {
		return particleRGBColorTop;
	}

	public void setParticleRGBColorTop(int particleRGBColorTop) {
		this.particleRGBColorTop = MathHelper.clamp(particleRGBColorTop, getParticleRGBColorBot(), 0xFFFFFFFF);
		setChanged();
	}

	public float[] getParticleHSBColorBot() {
		return particleHSBColorBot;
	}

	public void setParticleHSBColorBot(float[] particleHSBColorBot) {
		this.particleHSBColorBot[0] = MathHelper.clamp(particleHSBColorBot[0], 0F, getParticleHSBColorTop()[0]);
		this.particleHSBColorBot[1] = MathHelper.clamp(particleHSBColorBot[1], 0F, getParticleHSBColorTop()[1]);
		this.particleHSBColorBot[2] = MathHelper.clamp(particleHSBColorBot[2], 0F, getParticleHSBColorTop()[2]);
		setChanged();
	}

	public float[] getParticleHSBColorTop() {
		return particleHSBColorTop;
	}

	public void setParticleHSBColorTop(float[] particleHSBColorTop) {
		this.particleHSBColorTop[0] = MathHelper.clamp(particleHSBColorTop[0], getParticleHSBColorBot()[0], 1F);
		this.particleHSBColorTop[1] = MathHelper.clamp(particleHSBColorTop[1], getParticleHSBColorBot()[1], 1F);
		this.particleHSBColorTop[2] = MathHelper.clamp(particleHSBColorTop[2], getParticleHSBColorBot()[2], 1F);
		setChanged();
	}

	public int getParticleLifetimeBot() {
		return particleLifetimeBot;
	}

	public void setParticleLifetimeBot(int particleLifetimeBot) {
		this.particleLifetimeBot = MathHelper.clamp(particleLifetimeBot, ParticleConstants.MIN_LIFETIME, getParticleLifetimeTop());
		setChanged();
	}

	public int getParticleLifetimeTop() {
		return particleLifetimeTop;
	}

	public void setParticleLifetimeTop(int particleLifetimeTop) {
		this.particleLifetimeTop = MathHelper.clamp(particleLifetimeTop, getParticleLifetimeBot(), ParticleConstants.MAX_LIFETIME);
		setChanged();
	}

	public float getParticleSizeBot() {
		return particleSizeBot;
	}

	public void setParticleSizeBot(float particleSizeBot) {
		this.particleSizeBot = MathHelper.clamp(particleSizeBot, ParticleConstants.MIN_SIZE, getParticleSizeTop());
		setChanged();
	}

	public float getParticleSizeTop() {
		return particleSizeTop;
	}

	public void setParticleSizeTop(float particleSizeTop) {
		this.particleSizeTop = MathHelper.clamp(particleSizeTop, getParticleSizeBot(), ParticleConstants.MAX_SIZE);
		setChanged();
	}

	public float getParticleSpawnXBot() {
		return particleSpawnXBot;
	}

	public void setParticleSpawnXBot(float particleSpawnXBot) {
		this.particleSpawnXBot = MathHelper.clamp(particleSpawnXBot, ParticleConstants.MIN_SPAWN, getParticleSpawnXTop());
		setChanged();
	}

	public float getParticleSpawnXTop() {
		return particleSpawnXTop;
	}

	public void setParticleSpawnXTop(float particleSpawnXTop) {
		this.particleSpawnXTop = MathHelper.clamp(particleSpawnXTop, getParticleSpawnXBot(), ParticleConstants.MAX_SPAWN);
		setChanged();
	}

	public float getParticleSpawnYBot() {
		return particleSpawnYBot;
	}

	public void setParticleSpawnYBot(float particleSpawnYBot) {
		this.particleSpawnYBot = MathHelper.clamp(particleSpawnYBot, ParticleConstants.MIN_SPAWN, getParticleSpawnYTop());
		setChanged();
	}

	public float getParticleSpawnYTop() {
		return particleSpawnYTop;
	}

	public void setParticleSpawnYTop(float particleSpawnYTop) {
		this.particleSpawnYTop = MathHelper.clamp(particleSpawnYTop, getParticleSpawnYBot(), ParticleConstants.MAX_SPAWN);
		setChanged();
	}

	public float getParticleSpawnZBot() {
		return particleSpawnZBot;
	}

	public void setParticleSpawnZBot(float particleSpawnZBot) {
		this.particleSpawnZBot = MathHelper.clamp(particleSpawnZBot, ParticleConstants.MIN_SPAWN, getParticleSpawnZTop());
		setChanged();
	}

	public float getParticleSpawnZTop() {
		return particleSpawnZTop;
	}

	public void setParticleSpawnZTop(float particleSpawnZTop) {
		this.particleSpawnZTop = MathHelper.clamp(particleSpawnZTop, getParticleSpawnZBot(), ParticleConstants.MAX_SPAWN);
		setChanged();
	}

	public float getParticleMotionXBot() {
		return particleMotionXBot;
	}

	public void setParticleMotionXBot(float particleMotionXBot) {
		this.particleMotionXBot = MathHelper.clamp(particleMotionXBot, ParticleConstants.MIN_MOTION, getParticleMotionXTop());
		setChanged();
	}

	public float getParticleMotionXTop() {
		return particleMotionXTop;
	}

	public void setParticleMotionXTop(float particleMotionXTop) {
		this.particleMotionXTop = MathHelper.clamp(particleMotionXTop, getParticleMotionXBot(), ParticleConstants.MAX_MOTION);
		setChanged();
	}

	public float getParticleMotionYBot() {
		return particleMotionYBot;
	}

	public void setParticleMotionYBot(float particleMotionYBot) {
		this.particleMotionYBot = MathHelper.clamp(particleMotionYBot, ParticleConstants.MIN_MOTION, getParticleMotionYTop());
		setChanged();
	}

	public float getParticleMotionYTop() {
		return particleMotionYTop;
	}

	public void setParticleMotionYTop(float particleMotionYTop) {
		this.particleMotionYTop = MathHelper.clamp(particleMotionYTop, getParticleMotionYBot(), ParticleConstants.MAX_MOTION);
		setChanged();
	}

	public float getParticleMotionZBot() {
		return particleMotionZBot;
	}

	public void setParticleMotionZBot(float particleMotionZBot) {
		this.particleMotionZBot = MathHelper.clamp(particleMotionZBot, ParticleConstants.MIN_MOTION, getParticleMotionZTop());
		setChanged();
	}

	public float getParticleMotionZTop() {
		return particleMotionZTop;
	}

	public void setParticleMotionZTop(float particleMotionZTop) {
		this.particleMotionZTop = MathHelper.clamp(particleMotionZTop, getParticleMotionZBot(), ParticleConstants.MAX_MOTION);
		setChanged();
	}

	public int getParticleDelay() {
		return particleDelay;
	}

	public void setParticleDelay(int particleDelay) {
		this.particleDelay = MathHelper.clamp(particleDelay, ParticleConstants.MIN_DELAY, ParticleConstants.MAX_DELAY);
		setChanged();
	}

	public float getParticleGravity() {
		return particleGravity;
	}

	public void setParticleGravity(float particleGravity) {
		this.particleGravity = MathHelper.clamp(particleGravity, ParticleConstants.MIN_GRAVITY, ParticleConstants.MAX_GRAVITY);
		setChanged();
	}

	public boolean isParticleCollision() {
		return particleCollision;
	}

	public void setParticleCollision(boolean particleCollision) {
		this.particleCollision = particleCollision;
		setChanged();
	}

	public boolean isParticleFullbright() {
		return particleFullbright;
	}

	public void setParticleFullbright(boolean particleFullbright) {
		this.particleFullbright = particleFullbright;
		setChanged();
	}
}
