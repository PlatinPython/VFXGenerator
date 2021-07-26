package platinpython.particlegenerator.data;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import platinpython.particlegenerator.util.registries.BlockRegistry;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(BlockRegistry.PARTICLE_GENERATOR.get()).define('#', Blocks.STONE).pattern("###").pattern("# #").pattern("###").unlockedBy("has_stone", has(Blocks.STONE)).save(consumer);
	}
}
