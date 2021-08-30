package platinpython.vfxgenerator.util.registries;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import platinpython.vfxgenerator.item.VFXGeneratorCoreItem;
import platinpython.vfxgenerator.util.RegistryHandler;

public class ItemRegistry {
	public static final RegistryObject<Item> VFX_GENERATOR_CORE = RegistryHandler.ITEMS.register("vfx_generator_core", () -> new VFXGeneratorCoreItem(new Properties().rarity(Rarity.RARE).tab(ItemGroup.TAB_REDSTONE)));

	public static void register() {
	}
}
