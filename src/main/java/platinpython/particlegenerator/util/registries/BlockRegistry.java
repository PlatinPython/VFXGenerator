package platinpython.particlegenerator.util.registries;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import platinpython.particlegenerator.block.ParticleGeneratorBlock;
import platinpython.particlegenerator.util.RegistryHandler;

public class BlockRegistry {
	public static final RegistryObject<Block> PARTICLE_GENERATOR = register("particle_generator", ParticleGeneratorBlock::new);

	public static void register() {
	}

	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
		return RegistryHandler.BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		RegistryObject<T> ret = registerNoItem(name, block);
		RegistryHandler.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
		return ret;
	}
}
