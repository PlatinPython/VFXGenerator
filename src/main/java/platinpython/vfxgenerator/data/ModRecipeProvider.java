package platinpython.vfxgenerator.data;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import platinpython.vfxgenerator.util.registries.BlockRegistry;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ItemRegistry.VFX_GENERATOR_CORE.get()).define('E', Blocks.END_ROD).define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE).define('G', Tags.Items.GLASS).pattern("ERE").pattern("RGR").pattern("ERE").unlockedBy("has_end_rod", has(Blocks.END_ROD)).save(consumer);

		ShapedRecipeBuilder.shaped(BlockRegistry.VFX_GENERATOR.get()).define('C', ItemRegistry.VFX_GENERATOR_CORE.get()).define('S', Blocks.SMOOTH_STONE).pattern(" S ").pattern("SCS").pattern(" S ").unlockedBy("has_vfx_generator_core", has(ItemRegistry.VFX_GENERATOR_CORE.get())).save(consumer);
	}
}
