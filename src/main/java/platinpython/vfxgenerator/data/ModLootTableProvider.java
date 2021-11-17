package platinpython.vfxgenerator.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.functions.CopyBlockState;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.CopyNbt.Source;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.RegistryHandler;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
            this.add(BlockRegistry.VFX_GENERATOR.get(),
                     createSingleItemTable(BlockRegistry.VFX_GENERATOR.get()).apply(CopyBlockState.copyState(
                                                                                     BlockRegistry.VFX_GENERATOR.get()).copy(VFXGeneratorBlock.INVERTED))
                                                                             .apply(CopyNbt.copyData(Source.BLOCK_ENTITY)
                                                                                           .copy("particleData",
                                                                                                 "particleData")));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return RegistryHandler.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }
}
