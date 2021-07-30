package platinpython.vfxgenerator.util.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import platinpython.vfxgenerator.tileentity.ParticleGeneratorTileEntity;
import platinpython.vfxgenerator.util.RegistryHandler;

public class TileEntityRegistry {
	public static final RegistryObject<TileEntityType<?>> PARTICLE_GENERATOR = RegistryHandler.TILE_ENTITY_TYPES.register("vfx_generator", () -> TileEntityType.Builder.of(ParticleGeneratorTileEntity::new, BlockRegistry.VFX_GENERATOR.get()).build(null));

	public static void register() {
	}
}
