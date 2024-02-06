package platinpython.vfxgenerator.util.registries;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;
import platinpython.vfxgenerator.util.RegistryHandler;

public class BlockEntityRegistry {
    public static final RegistryObject<BlockEntityType<VFXGeneratorBlockEntity>> VFX_GENERATOR =
        RegistryHandler.BLOCK_ENTITY_TYPES.register(
        "vfx_generator",
        () -> BlockEntityType.Builder.of(VFXGeneratorBlockEntity::new, BlockRegistry.VFX_GENERATOR.get()).build(null)
    );

    public static void register() {
    }
}
