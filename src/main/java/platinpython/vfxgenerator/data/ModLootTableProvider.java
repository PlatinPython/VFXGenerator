package platinpython.vfxgenerator.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import platinpython.vfxgenerator.util.RegistryHandler;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

public class ModLootTableProvider extends LootTableProvider {
	public ModLootTableProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
		return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
		map.forEach((name, table) -> LootTableManager.validate(validationtracker, name, table));
	}

	public class Blocks extends BlockLootTables {
		@Override
		protected void addTables() {
			dropSelf(BlockRegistry.VFX_GENERATOR.get());
		}
		
		@Override
		protected Iterable<Block> getKnownBlocks() {
			return RegistryHandler.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
		}
	}
}
