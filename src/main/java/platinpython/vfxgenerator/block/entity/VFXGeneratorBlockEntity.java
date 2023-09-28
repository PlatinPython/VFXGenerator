package platinpython.vfxgenerator.block.entity;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;
import platinpython.vfxgenerator.util.data.ParticleData;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.registries.BlockEntityRegistry;
import platinpython.vfxgenerator.util.resources.DataManager;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class VFXGeneratorBlockEntity extends BlockEntity {
    public static final String PARTICLE_DATA_KEY = "particleData";

    private final ParticleData particleData = new ParticleData(this);

    public VFXGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.VFX_GENERATOR.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!(blockEntity instanceof VFXGeneratorBlockEntity generatorBlockEntity) || !(generatorBlockEntity.level instanceof ClientLevel clientLevel)) {
            return;
        }
        if ((state.getValue(VFXGeneratorBlock.INVERTED) && !state.getValue(
                VFXGeneratorBlock.POWERED)) || (!state.getValue(VFXGeneratorBlock.INVERTED) && state.getValue(
                VFXGeneratorBlock.POWERED))) {
            if (generatorBlockEntity.particleData.isEnabled()) {
                if (level.getGameTime() % generatorBlockEntity.particleData.getDelay() == 0) {
                    ThreadLocalRandom random = ThreadLocalRandom.current();

                    if (generatorBlockEntity.particleData.getSelected().isEmpty()) {
                        return;
                    }
                    ResourceLocation particle = new ArrayList<>(generatorBlockEntity.particleData.getSelected()).get(
                            random.nextInt(generatorBlockEntity.particleData.getSelected().size()));
                    ParticleType particleType = DataManager.selectableParticles().get(particle);
                    if (particleType == null) {
                        return;
                    }

                    int color;
                    if (generatorBlockEntity.particleData.useHSB()) {
                        color = Color.getRandomHSBColor(random, new float[]{
                                generatorBlockEntity.particleData.getHueBot(),
                                generatorBlockEntity.particleData.getSaturationBot(),
                                generatorBlockEntity.particleData.getBrightnessBot()
                        }, new float[]{
                                generatorBlockEntity.particleData.getHueTop(),
                                generatorBlockEntity.particleData.getSaturationTop(),
                                generatorBlockEntity.particleData.getBrightnessTop()
                        });
                    } else {
                        color = Color.getRandomRGBColor(random, generatorBlockEntity.particleData.getRGBColorBot(),
                                                        generatorBlockEntity.particleData.getRGBColorTop()
                        );
                    }
                    int lifetime = Math.round(
                            (generatorBlockEntity.particleData.getLifetimeBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getLifetimeTop() - generatorBlockEntity.particleData.getLifetimeBot()))));
                    float size = generatorBlockEntity.particleData.getSizeBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getSizeTop() - generatorBlockEntity.particleData.getSizeBot()));

                    Vec3 center = Vec3.atCenterOf(pos);
                    double spawnX = center.x + generatorBlockEntity.particleData.getSpawnXBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getSpawnXTop() - generatorBlockEntity.particleData.getSpawnXBot()));
                    double spawnY = center.y + generatorBlockEntity.particleData.getSpawnYBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getSpawnYTop() - generatorBlockEntity.particleData.getSpawnYBot()));
                    double spawnZ = center.z + generatorBlockEntity.particleData.getSpawnZBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getSpawnZTop() - generatorBlockEntity.particleData.getSpawnZBot()));
                    center = new Vec3(spawnX, spawnY, spawnZ);

                    double motionX = generatorBlockEntity.particleData.getMotionXBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getMotionXTop() - generatorBlockEntity.particleData.getMotionXBot()));
                    double motionY = generatorBlockEntity.particleData.getMotionYBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getMotionYTop() - generatorBlockEntity.particleData.getMotionYBot()));
                    double motionZ = generatorBlockEntity.particleData.getMotionZBot() + (random.nextFloat() * (generatorBlockEntity.particleData.getMotionZTop() - generatorBlockEntity.particleData.getMotionZBot()));
                    Vec3 motion = new Vec3(motionX, motionY, motionZ);

                    ClientUtils.addParticle(clientLevel, particleType, color, lifetime, size, center, motion,
                                            generatorBlockEntity.particleData.getGravity(),
                                            generatorBlockEntity.particleData.hasCollision(),
                                            generatorBlockEntity.particleData.isFullBright()
                    );
                }
            }
        }
    }

    public CompoundTag saveToTag(CompoundTag tag) {
        tag.put(PARTICLE_DATA_KEY, this.particleData.saveToTag());
        return tag;
    }

    public void loadFromTag(CompoundTag tag) {
        this.particleData.loadFromTag(tag.getCompound(PARTICLE_DATA_KEY));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        saveToTag(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        loadFromTag(tag);
        super.load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveToTag(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        loadFromTag(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        loadFromTag(pkt.getTag());
    }

    public ParticleData getParticleData() {
        return particleData;
    }
}
