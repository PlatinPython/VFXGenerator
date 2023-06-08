package platinpython.vfxgenerator.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraftforge.registries.RegistryObject;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.util.RegistryHandler;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

import java.util.Collections;
import java.util.List;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output) {
        super(output, Collections.emptySet(),
              List.of(new LootTableProvider.SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK))
        );
    }

    public static class Blocks extends BlockLootSubProvider {
        protected Blocks() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
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
