package platinpython.vfxgenerator.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.registries.BlockEntityRegistry;
import platinpython.vfxgenerator.util.registries.BlockRegistry;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

public class RegistryHandler {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, VFXGenerator.MOD_ID);

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, VFXGenerator.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VFXGenerator.MOD_ID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        BlockRegistry.register();
        ItemRegistry.register();
        BlockEntityRegistry.register();
    }
}
