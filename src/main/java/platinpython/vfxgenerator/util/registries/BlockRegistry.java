package platinpython.vfxgenerator.util.registries;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.item.VFXGeneratorBlockItem;
import platinpython.vfxgenerator.util.RegistryHandler;

public class BlockRegistry {
	public static final RegistryObject<Block> VFX_GENERATOR = register("vfx_generator", VFXGeneratorBlock::new);

	public static void register() {
	}

	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
		return RegistryHandler.BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		RegistryObject<T> ret = registerNoItem(name, block);
		RegistryHandler.ITEMS.register(name, () -> new VFXGeneratorBlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE).rarity(Rarity.RARE)));
		return ret;
	}
}
