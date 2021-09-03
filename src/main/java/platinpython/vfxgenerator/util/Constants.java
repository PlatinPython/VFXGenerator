package platinpython.vfxgenerator.util;

import com.google.common.collect.ImmutableList;

public final class Constants {
	public static final class ParticleConstants {
		public static final ImmutableList<String> PARTICLE_OPTIONS = ImmutableList.of("circle", "square", "spark_small", "spark_mid", "spark_big");
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
