package platinpython.vfxgenerator.util;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import platinpython.vfxgenerator.VFXGenerator;

public final class Constants {
	public static final class ParticleConstants {
		public static final ImmutableSet<ResourceLocation> PARTICLE_OPTIONS = ImmutableSet.of(new ResourceLocation(VFXGenerator.MOD_ID, "particle/circle"), new ResourceLocation(VFXGenerator.MOD_ID, "particle/square"), new ResourceLocation(VFXGenerator.MOD_ID, "particle/spark_small"), new ResourceLocation(VFXGenerator.MOD_ID, "particle/spark_mid"), new ResourceLocation(VFXGenerator.MOD_ID, "particle/spark_big"));
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
}
