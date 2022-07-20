package platinpython.vfxgenerator.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;

public final class Constants {
    public static final class ParticleConstants {
        public static final class Values {
            public static final ImmutableSet<ResourceLocation> PARTICLE_OPTIONS = ImmutableSet.of(
                    Util.createNamespacedResourceLocation("particle/circle"),
                    Util.createNamespacedResourceLocation("particle/square"),
                    Util.createNamespacedResourceLocation("particle/spark_small"),
                    Util.createNamespacedResourceLocation("particle/spark_mid"),
                    Util.createNamespacedResourceLocation("particle/spark_big"),
                    Util.createNamespacedResourceLocation("particle/spark_cross"),
                    Util.createNamespacedResourceLocation("particle/heart"),
                    Util.createNamespacedResourceLocation("particle/big_heart"),
                    Util.createNamespacedResourceLocation("particle/bubble"),
                    Util.createNamespacedResourceLocation("particle/bubble_big"),
                    Util.createNamespacedResourceLocation("particle/drop_1"),
                    Util.createNamespacedResourceLocation("particle/drop_2"),
                    Util.createNamespacedResourceLocation("particle/drop_3"),
                    Util.createNamespacedResourceLocation("particle/totem_otter")
            );
            public static final int MIN_LIFETIME = 0;
            public static final int MAX_LIFETIME = 200;
            public static final float MIN_SIZE = 0F;
            public static final float MAX_SIZE = 50F;
            public static final float MIN_SPAWN = -20F;
            public static final float MAX_SPAWN = 20F;
            public static final float MIN_MOTION = -3F;
            public static final float MAX_MOTION = 3F;
            public static final int MIN_DELAY = 1;
            public static final int MAX_DELAY = 200;
            public static final float MIN_GRAVITY = -2F;
            public static final float MAX_GRAVITY = 2F;
        }

        public static final class Keys {
            public static final String ENABLED = "enabled";
            public static final String SELECTED = "selected";
            public static final String USE_HSB = "useHSB";
            public static final String RGB_COLOR_BOT = "RGBColorBot";
            public static final String RGB_COLOR_TOP = "RGBColorTop";
            public static final String HUE_BOT = "hueBot";
            public static final String SATURATION_BOT = "saturationBot";
            public static final String BRIGHTNESS_BOT = "brightnessBot";
            public static final String HUE_TOP = "hueTop";
            public static final String SATURATION_TOP = "saturationTop";
            public static final String BRIGHTNESS_TOP = "brightnessTop";
            public static final String LIFETIME_BOT = "lifetimeBot";
            public static final String LIFETIME_TOP = "lifetimeTop";
            public static final String SIZE_BOT = "sizeBot";
            public static final String SIZE_TOP = "sizeTop";
            public static final String SPAWN_X_BOT = "spawnXBot";
            public static final String SPAWN_X_TOP = "spawnXTop";
            public static final String SPAWN_Y_BOT = "spawnYBot";
            public static final String SPAWN_Y_TOP = "spawnYTop";
            public static final String SPAWN_Z_BOT = "spawnZBot";
            public static final String SPAWN_Z_TOP = "spawnZTop";
            public static final String MOTION_X_BOT = "motionXBot";
            public static final String MOTION_X_TOP = "motionXTop";
            public static final String MOTION_Y_BOT = "motionYBot";
            public static final String MOTION_Y_TOP = "motionYTop";
            public static final String MOTION_Z_BOT = "motionZBot";
            public static final String MOTION_Z_TOP = "motionZTop";
            public static final String DELAY = "delay";
            public static final String GRAVITY = "gravity";
            public static final String COLLISION = "collision";
            public static final String FULLBRIGHT = "fullbright";
        }
    }
}
