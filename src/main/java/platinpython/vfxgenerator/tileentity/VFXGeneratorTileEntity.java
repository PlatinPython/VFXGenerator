package platinpython.vfxgenerator.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.data.ParticleData;
import platinpython.vfxgenerator.util.registries.TileEntityRegistry;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class VFXGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
    public static final String PARTICLE_DATA_KEY = "particleData";

    private final ParticleData particleData = new ParticleData(this);

    public VFXGeneratorTileEntity() {
        super(TileEntityRegistry.VFX_GENERATOR.get());
    }

    @Override
    public void tick() {
        if ((this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) &&
             !this.getBlockState().getValue(VFXGeneratorBlock.POWERED)) ||
            (!this.getBlockState().getValue(VFXGeneratorBlock.INVERTED) &&
             this.getBlockState().getValue(VFXGeneratorBlock.POWERED))) {
            World world = this.getLevel();
            if (world.isClientSide && this.particleData.isEnabled()) {
                if (world.getGameTime() % this.particleData.getDelay() == 0) {
                    ThreadLocalRandom random = ThreadLocalRandom.current();

                    ResourceLocation particle = new ArrayList<>(this.particleData.getSelected()).get(random.nextInt(this.particleData.getSelected()
                                                                                                                                     .size()));

                    int color;
                    if (this.particleData.useHSB()) {
                        color = Color.getRandomHSBColor(random,
                                                        new float[]{this.particleData.getHueBot(), this.particleData.getSaturationBot(), this.particleData.getBrightnessBot()},
                                                        new float[]{this.particleData.getHueTop(), this.particleData.getSaturationTop(), this.particleData.getBrightnessTop()});
                    } else {
                        color = Color.getRandomRGBColor(random,
                                                        this.particleData.getRGBColorBot(),
                                                        this.particleData.getRGBColorTop());
                    }
                    int lifetime = Math.round((this.particleData.getLifetimeBot() +
                                               (random.nextFloat() *
                                                (this.particleData.getLifetimeTop() -
                                                 this.particleData.getLifetimeBot()))));
                    float size = this.particleData.getSizeBot() +
                                 (random.nextFloat() *
                                  (this.particleData.getSizeTop() - this.particleData.getSizeBot()));

                    Vector3d pos = Vector3d.atCenterOf(this.getBlockPos());
                    double spawnX = pos.x +
                                    this.particleData.getSpawnXBot() +
                                    (random.nextFloat() *
                                     (this.particleData.getSpawnXTop() - this.particleData.getSpawnXBot()));
                    double spawnY = pos.y +
                                    this.particleData.getSpawnYBot() +
                                    (random.nextFloat() *
                                     (this.particleData.getSpawnYTop() - this.particleData.getSpawnYBot()));
                    double spawnZ = pos.z +
                                    this.particleData.getSpawnZBot() +
                                    (random.nextFloat() *
                                     (this.particleData.getSpawnZTop() - this.particleData.getSpawnZBot()));
                    pos = new Vector3d(spawnX, spawnY, spawnZ);

                    double motionX = this.particleData.getMotionXBot() +
                                     (random.nextFloat() *
                                      (this.particleData.getMotionXTop() - this.particleData.getMotionXBot()));
                    double motionY = this.particleData.getMotionYBot() +
                                     (random.nextFloat() *
                                      (this.particleData.getMotionYTop() - this.particleData.getMotionYBot()));
                    double motionZ = this.particleData.getMotionZBot() +
                                     (random.nextFloat() *
                                      (this.particleData.getMotionZTop() - this.particleData.getMotionZBot()));
                    Vector3d motion = new Vector3d(motionX, motionY, motionZ);

                    ClientUtils.addParticle(particle,
                                            color,
                                            lifetime,
                                            size,
                                            pos,
                                            motion,
                                            this.particleData.getGravity(),
                                            this.particleData.hasCollision(),
                                            this.particleData.isFullBright());
                }
            }
        }
    }

    public CompoundNBT saveToTag(CompoundNBT tag) {
        tag.put(PARTICLE_DATA_KEY, this.particleData.saveToTag());
        return tag;
    }

    public void loadFromTag(CompoundNBT tag) {
        this.particleData.loadFromTag(tag.getCompound(PARTICLE_DATA_KEY));
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

    public ParticleData getParticleData() {
        return particleData;
    }
}
