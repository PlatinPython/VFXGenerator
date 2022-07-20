package platinpython.vfxgenerator.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraftforge.registries.RegistryObject;
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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
    }

    public static class Blocks extends BlockLoot {
        @Override
        protected void addTables() {
            this.add(BlockRegistry.VFX_GENERATOR.get(), createSingleItemTable(BlockRegistry.VFX_GENERATOR.get()).apply(
                                                                                                                        CopyBlockState.copyState(BlockRegistry.VFX_GENERATOR.get()).copy(VFXGeneratorBlock.INVERTED))
                                                                                                                .apply(CopyNbtFunction.copyData(
                                                                                                                                              ContextNbtProvider.BLOCK_ENTITY)
                                                                                                                                      .copy("particleData",
                                                                                                                                            "particleData"
                                                                                                                                      )));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return RegistryHandler.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }
}
