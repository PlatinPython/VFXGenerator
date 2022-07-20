package platinpython.vfxgenerator.util.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import platinpython.vfxgenerator.item.VFXGeneratorCoreItem;
import platinpython.vfxgenerator.util.RegistryHandler;

public class ItemRegistry {
    public static final RegistryObject<Item> VFX_GENERATOR_CORE = RegistryHandler.ITEMS.register("vfx_generator_core",
                                                                                                 () -> new VFXGeneratorCoreItem(
                                                                                                         new Properties().rarity(
                                                                                                                                 Rarity.RARE)
                                                                                                                         .tab(CreativeModeTab.TAB_REDSTONE))
    );

    public static void register() {
    }
}
