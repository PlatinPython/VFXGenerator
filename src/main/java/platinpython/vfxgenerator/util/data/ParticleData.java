package platinpython.vfxgenerator.util.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import platinpython.vfxgenerator.util.Constants;
import platinpython.vfxgenerator.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ParticleData {
    private final BlockEntity owner;

    private boolean enabled = true;
    private TreeSet<ResourceLocation> selected = Util.createTreeSetFromCollectionWithComparator(
            Arrays.asList(Util.createNamespacedResourceLocation("spark_small"),
                          Util.createNamespacedResourceLocation("spark_mid"),
                          Util.createNamespacedResourceLocation("spark_big")
            ), ResourceLocation::compareNamespaced);
    private boolean useHSB = false;
    private int RGBColorBot = 0xFF000000;
    private int RGBColorTop = 0xFFFFFFFF;
    private float hueBot = 0F;
    private float saturationBot = 0F;
    private float brightnessBot = 0F;
    private float hueTop = 0F;
    private float saturationTop = 0F;
    private float brightnessTop = 0F;
    private int lifetimeBot = 20;
    private int lifetimeTop = 80;
    private float sizeBot = 1F;
    private float sizeTop = 3F;
    private float spawnXBot = -1F;
    private float spawnXTop = 1F;
    private float spawnYBot = 0F;
    private float spawnYTop = 0F;
    private float spawnZBot = -1F;
    private float spawnZTop = 1F;
    private float motionXBot = -0.1F;
    private float motionXTop = 0.1F;
    private float motionYBot = 0.1F;
    private float motionYTop = 0.1F;
    private float motionZBot = -0.1F;
    private float motionZTop = 0.1F;
    private int delay = 5;
    private float gravity = 0F;
    private boolean collision = false;
    private boolean fullBright = false;

    public ParticleData(BlockEntity owner) {
        this.owner = owner;
    }

    public CompoundTag saveToTag() {
        CompoundTag particleTag = new CompoundTag();
        particleTag.putBoolean(Constants.ParticleConstants.Keys.ENABLED, isEnabled());
        ListTag listNBT = new ListTag();
        getSelected().forEach((location) -> listNBT.add(StringTag.valueOf(location.toString())));
        particleTag.put(Constants.ParticleConstants.Keys.SELECTED, listNBT);
        particleTag.putBoolean(Constants.ParticleConstants.Keys.USE_HSB, useHSB());
        particleTag.putInt(Constants.ParticleConstants.Keys.RGB_COLOR_BOT, getRGBColorBot());
        particleTag.putInt(Constants.ParticleConstants.Keys.RGB_COLOR_TOP, getRGBColorTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.HUE_BOT, getHueBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SATURATION_BOT, getSaturationBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_BOT, getBrightnessBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.HUE_TOP, getHueTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SATURATION_TOP, getSaturationTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_TOP, getBrightnessTop());
        particleTag.putInt(Constants.ParticleConstants.Keys.LIFETIME_BOT, getLifetimeBot());
        particleTag.putInt(Constants.ParticleConstants.Keys.LIFETIME_TOP, getLifetimeTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SIZE_BOT, getSizeBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SIZE_TOP, getSizeTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_X_BOT, getSpawnXBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_X_TOP, getSpawnXTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_Y_BOT, getSpawnYBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_Y_TOP, getSpawnYTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_Z_BOT, getSpawnZBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.SPAWN_Z_TOP, getSpawnZTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_X_BOT, getMotionXBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_X_TOP, getMotionXTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_Y_BOT, getMotionYBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_Y_TOP, getMotionYTop());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_Z_BOT, getMotionZBot());
        particleTag.putFloat(Constants.ParticleConstants.Keys.MOTION_Z_TOP, getMotionZTop());
        particleTag.putInt(Constants.ParticleConstants.Keys.DELAY, getDelay());
        particleTag.putFloat(Constants.ParticleConstants.Keys.GRAVITY, getGravity());
        particleTag.putBoolean(Constants.ParticleConstants.Keys.COLLISION, hasCollision());
        particleTag.putBoolean(Constants.ParticleConstants.Keys.FULLBRIGHT, isFullBright());
        return particleTag;
    }

    public void loadFromTag(CompoundTag particleTag) {
        enabled = particleTag.getBoolean(Constants.ParticleConstants.Keys.ENABLED);
        if (particleTag.getTagType(Constants.ParticleConstants.Keys.SELECTED) == Tag.TAG_LIST) {
            selected = Util.createTreeSetFromCollectionWithComparator(
                    particleTag.getList(Constants.ParticleConstants.Keys.SELECTED, Tag.TAG_STRING)
                               .stream()
                               .map(nbt -> ResourceLocation.tryParse(nbt.getAsString().replace(":particle/", ":")))
                               .filter(Constants.ParticleConstants.Values.PARTICLE_OPTIONS::contains)
                               .collect(Collectors.toList()), ResourceLocation::compareNamespaced);
            if (selected.isEmpty()) {
                selected = Util.createTreeSetFromCollectionWithComparator(
                        Collections.singletonList(Util.createNamespacedResourceLocation("circle")),
                        ResourceLocation::compareNamespaced
                );
            }
            if (selected.size() > Constants.ParticleConstants.Values.PARTICLE_OPTIONS.size()) {
                selected = Util.createTreeSetFromCollectionWithComparator(
                        Constants.ParticleConstants.Values.PARTICLE_OPTIONS, ResourceLocation::compareNamespaced);
            }
        } else {
            selected = Util.createTreeSetFromCollectionWithComparator(Collections.singletonList(
                    Constants.ParticleConstants.Values.PARTICLE_OPTIONS.contains(Util.createNamespacedResourceLocation(
                            particleTag.getString(Constants.ParticleConstants.Keys.SELECTED))) ?
                    Util.createNamespacedResourceLocation(
                            particleTag.getString(Constants.ParticleConstants.Keys.SELECTED)) :
                    Util.createNamespacedResourceLocation("circle")), ResourceLocation::compareNamespaced);
        }
        useHSB = particleTag.getBoolean(Constants.ParticleConstants.Keys.USE_HSB);
        RGBColorBot = Mth.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.RGB_COLOR_BOT), 0xFF000000,
                                0xFFFFFFFF
        );
        RGBColorTop = Mth.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.RGB_COLOR_TOP), 0xFF000000,
                                0xFFFFFFFF
        );
        hueBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.HUE_BOT), 0F, 1F);
        saturationBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SATURATION_BOT), 0F, 1F);
        brightnessBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_BOT), 0F, 1F);
        hueTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.HUE_TOP), 0F, 1F);
        saturationTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SATURATION_TOP), 0F, 1F);
        brightnessTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_TOP), 0F, 1F);
        lifetimeBot = Mth.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.LIFETIME_BOT),
                                Constants.ParticleConstants.Values.MIN_LIFETIME,
                                Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        lifetimeTop = Mth.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.LIFETIME_TOP),
                                Constants.ParticleConstants.Values.MIN_LIFETIME,
                                Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        sizeBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SIZE_BOT),
                            Constants.ParticleConstants.Values.MIN_SIZE, Constants.ParticleConstants.Values.MAX_SIZE
        );
        sizeTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SIZE_TOP),
                            Constants.ParticleConstants.Values.MIN_SIZE, Constants.ParticleConstants.Values.MAX_SIZE
        );
        spawnXBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_X_BOT),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnXTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_X_TOP),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnYBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Y_BOT),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnYTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Y_TOP),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnZBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Z_BOT),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnZTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Z_TOP),
                              Constants.ParticleConstants.Values.MIN_SPAWN, Constants.ParticleConstants.Values.MAX_SPAWN
        );
        motionXBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_X_BOT),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionXTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_X_TOP),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionYBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Y_BOT),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionYTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Y_TOP),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionZBot = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Z_BOT),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionZTop = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Z_TOP),
                               Constants.ParticleConstants.Values.MIN_MOTION,
                               Constants.ParticleConstants.Values.MAX_MOTION
        );
        delay = Mth.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.DELAY),
                          Constants.ParticleConstants.Values.MIN_DELAY, Constants.ParticleConstants.Values.MAX_DELAY
        );
        gravity = Mth.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.GRAVITY),
                            Constants.ParticleConstants.Values.MIN_GRAVITY,
                            Constants.ParticleConstants.Values.MAX_GRAVITY
        );
        collision = particleTag.getBoolean(Constants.ParticleConstants.Keys.COLLISION);
        fullBright = particleTag.getBoolean(Constants.ParticleConstants.Keys.FULLBRIGHT);

        ensureDataOrder();
    }

    private void ensureDataOrder() {
        RGBColorBot = Mth.clamp(getRGBColorBot(), 0xFF000000, getRGBColorTop());
        RGBColorTop = Mth.clamp(getRGBColorTop(), getRGBColorBot(), 0xFFFFFFFF);
        hueBot = Mth.clamp(getHueBot(), 0F, getHueTop());
        saturationBot = Mth.clamp(getSaturationBot(), 0F, getSaturationTop());
        brightnessBot = Mth.clamp(getBrightnessBot(), 0F, getBrightnessTop());
        hueTop = Mth.clamp(getHueTop(), getHueBot(), 1F);
        saturationTop = Mth.clamp(getSaturationTop(), getSaturationBot(), 1F);
        brightnessTop = Mth.clamp(getBrightnessTop(), getBrightnessBot(), 1F);
        lifetimeBot = Mth.clamp(getLifetimeBot(), Constants.ParticleConstants.Values.MIN_LIFETIME, getLifetimeTop());
        lifetimeTop = Mth.clamp(getLifetimeTop(), getLifetimeBot(), Constants.ParticleConstants.Values.MAX_LIFETIME);
        sizeBot = Mth.clamp(getSizeBot(), Constants.ParticleConstants.Values.MIN_SIZE, getSizeTop());
        sizeTop = Mth.clamp(getSizeTop(), getSizeBot(), Constants.ParticleConstants.Values.MAX_SIZE);
        spawnXBot = Mth.clamp(getSpawnXBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnXTop());
        spawnXTop = Mth.clamp(getSpawnXTop(), getSpawnXBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        spawnYBot = Mth.clamp(getSpawnYBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnYTop());
        spawnYTop = Mth.clamp(getSpawnYTop(), getSpawnYBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        spawnZBot = Mth.clamp(getSpawnZBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnZTop());
        spawnZTop = Mth.clamp(getSpawnZTop(), getSpawnZBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        motionXBot = Mth.clamp(getMotionXBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionXTop());
        motionXTop = Mth.clamp(getMotionXTop(), getMotionXBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        motionYBot = Mth.clamp(getMotionYBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionYTop());
        motionYTop = Mth.clamp(getMotionYTop(), getMotionYBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        motionZBot = Mth.clamp(getMotionZBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionZTop());
        motionZTop = Mth.clamp(getMotionZTop(), getMotionZBot(), Constants.ParticleConstants.Values.MAX_MOTION);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        owner.setChanged();
    }

    public TreeSet<ResourceLocation> getSelected() {
        return selected;
    }

    public void setSelected(TreeSet<ResourceLocation> selected) {
        this.selected = Util.createTreeSetFromCollectionWithComparator(selected.stream()
                                                                               .filter(Constants.ParticleConstants.Values.PARTICLE_OPTIONS::contains)
                                                                               .collect(Collectors.toList()),
                                                                       ResourceLocation::compareNamespaced
        );
        if (this.selected.isEmpty()) {
            this.selected = Util.createTreeSetFromCollectionWithComparator(
                    Collections.singletonList(Util.createNamespacedResourceLocation("circle")),
                    ResourceLocation::compareNamespaced
            );
        }
        owner.setChanged();
    }

    public boolean useHSB() {
        return useHSB;
    }

    public void setUseHSB(boolean useHSB) {
        this.useHSB = useHSB;
        owner.setChanged();
    }

    public int getRGBColorBot() {
        return RGBColorBot;
    }

    public void setRGBColorBot(int RGBColorBot) {
        this.RGBColorBot = Mth.clamp(RGBColorBot, 0xFF000000, getRGBColorTop());
        owner.setChanged();
    }

    public int getRGBColorTop() {
        return RGBColorTop;
    }

    public void setRGBColorTop(int RGBColorTop) {
        this.RGBColorTop = Mth.clamp(RGBColorTop, getRGBColorBot(), 0xFFFFFFFF);
        owner.setChanged();
    }

    public float getHueBot() {
        return hueBot;
    }

    public void setHueBot(float hueBot) {
        this.hueBot = Mth.clamp(hueBot, 0F, getHueTop());
    }

    public float getSaturationBot() {
        return saturationBot;
    }

    public void setSaturationBot(float saturationBot) {
        this.saturationBot = Mth.clamp(saturationBot, 0F, getSaturationTop());
    }

    public float getBrightnessBot() {
        return brightnessBot;
    }

    public void setBrightnessBot(float brightnessBot) {
        this.brightnessBot = Mth.clamp(brightnessBot, 0F, getBrightnessTop());
    }

    public float getHueTop() {
        return hueTop;
    }

    public void setHueTop(float hueTop) {
        this.hueTop = Mth.clamp(hueTop, getHueBot(), 1F);
    }

    public float getSaturationTop() {
        return saturationTop;
    }

    public void setSaturationTop(float saturationTop) {
        this.saturationTop = Mth.clamp(saturationTop, getSaturationBot(), 1F);
    }

    public float getBrightnessTop() {
        return brightnessTop;
    }

    public void setBrightnessTop(float brightnessTop) {
        this.brightnessTop = Mth.clamp(brightnessTop, getBrightnessBot(), 1F);
    }

    public int getLifetimeBot() {
        return lifetimeBot;
    }

    public void setLifetimeBot(int lifetimeBot) {
        this.lifetimeBot = Mth.clamp(lifetimeBot, Constants.ParticleConstants.Values.MIN_LIFETIME, getLifetimeTop());
        owner.setChanged();
    }

    public int getLifetimeTop() {
        return lifetimeTop;
    }

    public void setLifetimeTop(int lifetimeTop) {
        this.lifetimeTop = Mth.clamp(lifetimeTop, getLifetimeBot(), Constants.ParticleConstants.Values.MAX_LIFETIME);
        owner.setChanged();
    }

    public float getSizeBot() {
        return sizeBot;
    }

    public void setSizeBot(float sizeBot) {
        this.sizeBot = Mth.clamp(sizeBot, Constants.ParticleConstants.Values.MIN_SIZE, getSizeTop());
        owner.setChanged();
    }

    public float getSizeTop() {
        return sizeTop;
    }

    public void setSizeTop(float sizeTop) {
        this.sizeTop = Mth.clamp(sizeTop, getSizeBot(), Constants.ParticleConstants.Values.MAX_SIZE);
        owner.setChanged();
    }

    public float getSpawnXBot() {
        return spawnXBot;
    }

    public void setSpawnXBot(float spawnXBot) {
        this.spawnXBot = Mth.clamp(spawnXBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnXTop());
        owner.setChanged();
    }

    public float getSpawnXTop() {
        return spawnXTop;
    }

    public void setSpawnXTop(float spawnXTop) {
        this.spawnXTop = Mth.clamp(spawnXTop, getSpawnXBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getSpawnYBot() {
        return spawnYBot;
    }

    public void setSpawnYBot(float spawnYBot) {
        this.spawnYBot = Mth.clamp(spawnYBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnYTop());
        owner.setChanged();
    }

    public float getSpawnYTop() {
        return spawnYTop;
    }

    public void setSpawnYTop(float spawnYTop) {
        this.spawnYTop = Mth.clamp(spawnYTop, getSpawnYBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getSpawnZBot() {
        return spawnZBot;
    }

    public void setSpawnZBot(float spawnZBot) {
        this.spawnZBot = Mth.clamp(spawnZBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnZTop());
        owner.setChanged();
    }

    public float getSpawnZTop() {
        return spawnZTop;
    }

    public void setSpawnZTop(float spawnZTop) {
        this.spawnZTop = Mth.clamp(spawnZTop, getSpawnZBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getMotionXBot() {
        return motionXBot;
    }

    public void setMotionXBot(float motionXBot) {
        this.motionXBot = Mth.clamp(motionXBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionXTop());
        owner.setChanged();
    }

    public float getMotionXTop() {
        return motionXTop;
    }

    public void setMotionXTop(float motionXTop) {
        this.motionXTop = Mth.clamp(motionXTop, getMotionXBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public float getMotionYBot() {
        return motionYBot;
    }

    public void setMotionYBot(float motionYBot) {
        this.motionYBot = Mth.clamp(motionYBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionYTop());
        owner.setChanged();
    }

    public float getMotionYTop() {
        return motionYTop;
    }

    public void setMotionYTop(float motionYTop) {
        this.motionYTop = Mth.clamp(motionYTop, getMotionYBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public float getMotionZBot() {
        return motionZBot;
    }

    public void setMotionZBot(float motionZBot) {
        this.motionZBot = Mth.clamp(motionZBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionZTop());
        owner.setChanged();
    }

    public float getMotionZTop() {
        return motionZTop;
    }

    public void setMotionZTop(float motionZTop) {
        this.motionZTop = Mth.clamp(motionZTop, getMotionZBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = Mth.clamp(delay, Constants.ParticleConstants.Values.MIN_DELAY,
                               Constants.ParticleConstants.Values.MAX_DELAY
        );
        owner.setChanged();
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = Mth.clamp(gravity, Constants.ParticleConstants.Values.MIN_GRAVITY,
                                 Constants.ParticleConstants.Values.MAX_GRAVITY
        );
        owner.setChanged();
    }

    public boolean hasCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
        owner.setChanged();
    }

    public boolean isFullBright() {
        return fullBright;
    }

    public void setFullBright(boolean fullBright) {
        this.fullBright = fullBright;
        owner.setChanged();
    }
}
