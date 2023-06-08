package platinpython.vfxgenerator.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import platinpython.vfxgenerator.util.registries.BlockRegistry;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.VFX_GENERATOR_CORE.get())
                           .define('E', Blocks.END_ROD)
                           .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                           .define('G', Tags.Items.GLASS)
                           .pattern("ERE")
                           .pattern("RGR")
                           .pattern("ERE")
                           .unlockedBy("has_end_rod", has(Blocks.END_ROD))
                           .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.VFX_GENERATOR.get())
                           .define('C', ItemRegistry.VFX_GENERATOR_CORE.get())
                           .define('S', Blocks.SMOOTH_STONE)
                           .pattern(" S ")
                           .pattern("SCS")
                           .pattern(" S ")
                           .unlockedBy("has_vfx_generator_core", has(ItemRegistry.VFX_GENERATOR_CORE.get()))
                           .save(consumer);
    }
}
