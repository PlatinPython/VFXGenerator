package platinpython.particlegenerator.util.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import platinpython.particlegenerator.tileentity.ParticleGeneratorTileEntity;
import platinpython.particlegenerator.util.RegistryHandler;

public class TileEntityRegistry {
	public static final RegistryObject<TileEntityType<?>> PARTICLE_GENERATOR = RegistryHandler.TILE_ENTITY_TYPES.register("particle_generator", () -> TileEntityType.Builder.of(ParticleGeneratorTileEntity::new, BlockRegistry.PARTICLE_GENERATOR.get()).build(null));

	public static void register() {
	}
}
