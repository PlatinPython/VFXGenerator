package platinpython.vfxgenerator.util.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import platinpython.vfxgenerator.util.Constants;
import platinpython.vfxgenerator.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ParticleData {
    private final TileEntity owner;

    private boolean enabled = true;
    private TreeSet<ResourceLocation> selected = Util.createTreeSetFromCollectionWithComparator(
            Arrays.asList(Util.createNamespacedResourceLocation("particle/spark_small"),
                          Util.createNamespacedResourceLocation("particle/spark_mid"),
                          Util.createNamespacedResourceLocation("particle/spark_big")
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

    public ParticleData(TileEntity owner) {
        this.owner = owner;
    }

    public CompoundNBT saveToTag() {
        CompoundNBT particleTag = new CompoundNBT();
        particleTag.putBoolean(Constants.ParticleConstants.Keys.ENABLED, isEnabled());
        ListNBT listNBT = new ListNBT();
        getSelected().forEach((location) -> listNBT.add(StringNBT.valueOf(location.toString())));
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

    public void loadFromTag(CompoundNBT particleTag) {
        enabled = particleTag.getBoolean(Constants.ParticleConstants.Keys.ENABLED);
        if (particleTag.getTagType(
                Constants.ParticleConstants.Keys.SELECTED) == net.minecraftforge.common.util.Constants.NBT.TAG_LIST) {
            selected = Util.createTreeSetFromCollectionWithComparator(
                    ((ListNBT) particleTag.get(Constants.ParticleConstants.Keys.SELECTED)).stream()
                                                                                          .map((nbt) -> ResourceLocation.tryParse(
                                                                                                  nbt.getAsString()))
                                                                                          .filter(Constants.ParticleConstants.Values.PARTICLE_OPTIONS::contains)
                                                                                          .collect(Collectors.toList()),
                    ResourceLocation::compareNamespaced
            );
            if (selected.isEmpty()) selected = Util.createTreeSetFromCollectionWithComparator(
                    Collections.singletonList(Util.createNamespacedResourceLocation("particle/circle")),
                    ResourceLocation::compareNamespaced
            );
            if (selected.size() > Constants.ParticleConstants.Values.PARTICLE_OPTIONS.size())
                selected = Util.createTreeSetFromCollectionWithComparator(
                        Constants.ParticleConstants.Values.PARTICLE_OPTIONS, ResourceLocation::compareNamespaced);
        } else {
            selected = Util.createTreeSetFromCollectionWithComparator(Collections.singletonList(
                    Constants.ParticleConstants.Values.PARTICLE_OPTIONS.contains(Util.createNamespacedResourceLocation(
                            "particle/" + particleTag.getString(Constants.ParticleConstants.Keys.SELECTED))) ?
                    Util.createNamespacedResourceLocation(
                            "particle/" + particleTag.getString(Constants.ParticleConstants.Keys.SELECTED)) :
                    Util.createNamespacedResourceLocation("particle/circle")), ResourceLocation::compareNamespaced);
        }
        useHSB = particleTag.getBoolean(Constants.ParticleConstants.Keys.USE_HSB);
        RGBColorBot = MathHelper.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.RGB_COLOR_BOT), 0xFF000000,
                                       0xFFFFFFFF
        );
        RGBColorTop = MathHelper.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.RGB_COLOR_TOP), 0xFF000000,
                                       0xFFFFFFFF
        );
        hueBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.HUE_BOT), 0F, 1F);
        saturationBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SATURATION_BOT), 0F, 1F);
        brightnessBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_BOT), 0F, 1F);
        hueTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.HUE_TOP), 0F, 1F);
        saturationTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SATURATION_TOP), 0F, 1F);
        brightnessTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.BRIGHTNESS_TOP), 0F, 1F);
        lifetimeBot = MathHelper.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.LIFETIME_BOT),
                                       Constants.ParticleConstants.Values.MIN_LIFETIME,
                                       Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        lifetimeTop = MathHelper.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.LIFETIME_TOP),
                                       Constants.ParticleConstants.Values.MIN_LIFETIME,
                                       Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        sizeBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SIZE_BOT),
                                   Constants.ParticleConstants.Values.MIN_SIZE,
                                   Constants.ParticleConstants.Values.MAX_SIZE
        );
        sizeTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SIZE_TOP),
                                   Constants.ParticleConstants.Values.MIN_SIZE,
                                   Constants.ParticleConstants.Values.MAX_SIZE
        );
        spawnXBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_X_BOT),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnXTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_X_TOP),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnYBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Y_BOT),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnYTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Y_TOP),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnZBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Z_BOT),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        spawnZTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.SPAWN_Z_TOP),
                                     Constants.ParticleConstants.Values.MIN_SPAWN,
                                     Constants.ParticleConstants.Values.MAX_SPAWN
        );
        motionXBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_X_BOT),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionXTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_X_TOP),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionYBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Y_BOT),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionYTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Y_TOP),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionZBot = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Z_BOT),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        motionZTop = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.MOTION_Z_TOP),
                                      Constants.ParticleConstants.Values.MIN_MOTION,
                                      Constants.ParticleConstants.Values.MAX_MOTION
        );
        delay = MathHelper.clamp(particleTag.getInt(Constants.ParticleConstants.Keys.DELAY),
                                 Constants.ParticleConstants.Values.MIN_DELAY,
                                 Constants.ParticleConstants.Values.MAX_DELAY
        );
        gravity = MathHelper.clamp(particleTag.getFloat(Constants.ParticleConstants.Keys.GRAVITY),
                                   Constants.ParticleConstants.Values.MIN_GRAVITY,
                                   Constants.ParticleConstants.Values.MAX_GRAVITY
        );
        collision = particleTag.getBoolean(Constants.ParticleConstants.Keys.COLLISION);
        fullBright = particleTag.getBoolean(Constants.ParticleConstants.Keys.FULLBRIGHT);

        ensureDataOrder();
    }

    private void ensureDataOrder() {
        RGBColorBot = MathHelper.clamp(getRGBColorBot(), 0xFF000000, getRGBColorTop());
        RGBColorTop = MathHelper.clamp(getRGBColorTop(), getRGBColorBot(), 0xFFFFFFFF);
        hueBot = MathHelper.clamp(getHueBot(), 0F, getHueTop());
        saturationBot = MathHelper.clamp(getSaturationBot(), 0F, getSaturationTop());
        brightnessBot = MathHelper.clamp(getBrightnessBot(), 0F, getBrightnessTop());
        hueTop = MathHelper.clamp(getHueTop(), getHueBot(), 1F);
        saturationTop = MathHelper.clamp(getSaturationTop(), getSaturationBot(), 1F);
        brightnessTop = MathHelper.clamp(getBrightnessTop(), getBrightnessBot(), 1F);
        lifetimeBot = MathHelper.clamp(getLifetimeBot(), Constants.ParticleConstants.Values.MIN_LIFETIME,
                                       getLifetimeTop()
        );
        lifetimeTop = MathHelper.clamp(getLifetimeTop(), getLifetimeBot(),
                                       Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        sizeBot = MathHelper.clamp(getSizeBot(), Constants.ParticleConstants.Values.MIN_SIZE, getSizeTop());
        sizeTop = MathHelper.clamp(getSizeTop(), getSizeBot(), Constants.ParticleConstants.Values.MAX_SIZE);
        spawnXBot = MathHelper.clamp(getSpawnXBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnXTop());
        spawnXTop = MathHelper.clamp(getSpawnXTop(), getSpawnXBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        spawnYBot = MathHelper.clamp(getSpawnYBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnYTop());
        spawnYTop = MathHelper.clamp(getSpawnYTop(), getSpawnYBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        spawnZBot = MathHelper.clamp(getSpawnZBot(), Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnZTop());
        spawnZTop = MathHelper.clamp(getSpawnZTop(), getSpawnZBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        motionXBot = MathHelper.clamp(getMotionXBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionXTop());
        motionXTop = MathHelper.clamp(getMotionXTop(), getMotionXBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        motionYBot = MathHelper.clamp(getMotionYBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionYTop());
        motionYTop = MathHelper.clamp(getMotionYTop(), getMotionYBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        motionZBot = MathHelper.clamp(getMotionZBot(), Constants.ParticleConstants.Values.MIN_MOTION, getMotionZTop());
        motionZTop = MathHelper.clamp(getMotionZTop(), getMotionZBot(), Constants.ParticleConstants.Values.MAX_MOTION);
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
        if (this.selected.isEmpty()) this.selected = Util.createTreeSetFromCollectionWithComparator(
                Collections.singletonList(Util.createNamespacedResourceLocation("particle/circle")),
                ResourceLocation::compareNamespaced
        );
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
        this.RGBColorBot = MathHelper.clamp(RGBColorBot, 0xFF000000, getRGBColorTop());
        owner.setChanged();
    }

    public int getRGBColorTop() {
        return RGBColorTop;
    }

    public void setRGBColorTop(int RGBColorTop) {
        this.RGBColorTop = MathHelper.clamp(RGBColorTop, getRGBColorBot(), 0xFFFFFFFF);
        owner.setChanged();
    }

    public float getHueBot() {
        return hueBot;
    }

    public void setHueBot(float hueBot) {
        this.hueBot = MathHelper.clamp(hueBot, 0F, getHueTop());
    }

    public float getSaturationBot() {
        return saturationBot;
    }

    public void setSaturationBot(float saturationBot) {
        this.saturationBot = MathHelper.clamp(saturationBot, 0F, getSaturationTop());
    }

    public float getBrightnessBot() {
        return brightnessBot;
    }

    public void setBrightnessBot(float brightnessBot) {
        this.brightnessBot = MathHelper.clamp(brightnessBot, 0F, getBrightnessTop());
    }

    public float getHueTop() {
        return hueTop;
    }

    public void setHueTop(float hueTop) {
        this.hueTop = MathHelper.clamp(hueTop, getHueBot(), 1F);
    }

    public float getSaturationTop() {
        return saturationTop;
    }

    public void setSaturationTop(float saturationTop) {
        this.saturationTop = MathHelper.clamp(saturationTop, getSaturationBot(), 1F);
    }

    public float getBrightnessTop() {
        return brightnessTop;
    }

    public void setBrightnessTop(float brightnessTop) {
        this.brightnessTop = MathHelper.clamp(brightnessTop, getBrightnessBot(), 1F);
    }

    public int getLifetimeBot() {
        return lifetimeBot;
    }

    public void setLifetimeBot(int lifetimeBot) {
        this.lifetimeBot = MathHelper.clamp(lifetimeBot, Constants.ParticleConstants.Values.MIN_LIFETIME,
                                            getLifetimeTop()
        );
        owner.setChanged();
    }

    public int getLifetimeTop() {
        return lifetimeTop;
    }

    public void setLifetimeTop(int lifetimeTop) {
        this.lifetimeTop = MathHelper.clamp(lifetimeTop, getLifetimeBot(),
                                            Constants.ParticleConstants.Values.MAX_LIFETIME
        );
        owner.setChanged();
    }

    public float getSizeBot() {
        return sizeBot;
    }

    public void setSizeBot(float sizeBot) {
        this.sizeBot = MathHelper.clamp(sizeBot, Constants.ParticleConstants.Values.MIN_SIZE, getSizeTop());
        owner.setChanged();
    }

    public float getSizeTop() {
        return sizeTop;
    }

    public void setSizeTop(float sizeTop) {
        this.sizeTop = MathHelper.clamp(sizeTop, getSizeBot(), Constants.ParticleConstants.Values.MAX_SIZE);
        owner.setChanged();
    }

    public float getSpawnXBot() {
        return spawnXBot;
    }

    public void setSpawnXBot(float spawnXBot) {
        this.spawnXBot = MathHelper.clamp(spawnXBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnXTop());
        owner.setChanged();
    }

    public float getSpawnXTop() {
        return spawnXTop;
    }

    public void setSpawnXTop(float spawnXTop) {
        this.spawnXTop = MathHelper.clamp(spawnXTop, getSpawnXBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getSpawnYBot() {
        return spawnYBot;
    }

    public void setSpawnYBot(float spawnYBot) {
        this.spawnYBot = MathHelper.clamp(spawnYBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnYTop());
        owner.setChanged();
    }

    public float getSpawnYTop() {
        return spawnYTop;
    }

    public void setSpawnYTop(float spawnYTop) {
        this.spawnYTop = MathHelper.clamp(spawnYTop, getSpawnYBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getSpawnZBot() {
        return spawnZBot;
    }

    public void setSpawnZBot(float spawnZBot) {
        this.spawnZBot = MathHelper.clamp(spawnZBot, Constants.ParticleConstants.Values.MIN_SPAWN, getSpawnZTop());
        owner.setChanged();
    }

    public float getSpawnZTop() {
        return spawnZTop;
    }

    public void setSpawnZTop(float spawnZTop) {
        this.spawnZTop = MathHelper.clamp(spawnZTop, getSpawnZBot(), Constants.ParticleConstants.Values.MAX_SPAWN);
        owner.setChanged();
    }

    public float getMotionXBot() {
        return motionXBot;
    }

    public void setMotionXBot(float motionXBot) {
        this.motionXBot = MathHelper.clamp(motionXBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionXTop());
        owner.setChanged();
    }

    public float getMotionXTop() {
        return motionXTop;
    }

    public void setMotionXTop(float motionXTop) {
        this.motionXTop = MathHelper.clamp(motionXTop, getMotionXBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public float getMotionYBot() {
        return motionYBot;
    }

    public void setMotionYBot(float motionYBot) {
        this.motionYBot = MathHelper.clamp(motionYBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionYTop());
        owner.setChanged();
    }

    public float getMotionYTop() {
        return motionYTop;
    }

    public void setMotionYTop(float motionYTop) {
        this.motionYTop = MathHelper.clamp(motionYTop, getMotionYBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public float getMotionZBot() {
        return motionZBot;
    }

    public void setMotionZBot(float motionZBot) {
        this.motionZBot = MathHelper.clamp(motionZBot, Constants.ParticleConstants.Values.MIN_MOTION, getMotionZTop());
        owner.setChanged();
    }

    public float getMotionZTop() {
        return motionZTop;
    }

    public void setMotionZTop(float motionZTop) {
        this.motionZTop = MathHelper.clamp(motionZTop, getMotionZBot(), Constants.ParticleConstants.Values.MAX_MOTION);
        owner.setChanged();
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = MathHelper.clamp(delay, Constants.ParticleConstants.Values.MIN_DELAY,
                                      Constants.ParticleConstants.Values.MAX_DELAY
        );
        owner.setChanged();
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = MathHelper.clamp(gravity, Constants.ParticleConstants.Values.MIN_GRAVITY,
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
