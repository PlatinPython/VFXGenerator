package platinpython.vfxgenerator.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;
import platinpython.vfxgenerator.client.gui.screen.ParticleOptionsScreen;
import platinpython.vfxgenerator.client.model.FullbrightBakedModel;
import platinpython.vfxgenerator.client.particle.VFXParticle;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

@EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.VFX_GENERATOR.get(), RenderType.cutout());

        event.enqueueWork(() -> ItemProperties.register(BlockRegistry.VFX_GENERATOR.get().asItem(),
                                                        Util.createNamespacedResourceLocation(
                                                                VFXGeneratorBlock.INVERTED_KEY),
                                                        (stack, world, entity, seed) -> Boolean.parseBoolean(
                                                                stack.getOrCreateTagElement("BlockStateTag")
                                                                     .getString(VFXGeneratorBlock.INVERTED_KEY)) ?
                                                                                        1F :
                                                                                        0F
        ));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        makeEmissive(BlockRegistry.VFX_GENERATOR.get(), event);
    }

    private static void makeEmissive(Block block, ModelBakeEvent event) {
        for (BlockState blockState : block.getStateDefinition().getPossibleStates()) {
            ModelResourceLocation modelResourceLocation = BlockModelShaper.stateToModelLocation(blockState);
            BakedModel existingModel = event.getModelRegistry().get(modelResourceLocation);
            if (existingModel == null) {
                VFXGenerator.LOGGER.warn(
                        "Did not find the expected vanilla baked model(s) for" + block + "in registry");
            } else if (existingModel instanceof FullbrightBakedModel) {
                VFXGenerator.LOGGER.warn("Tried to replace FullBrightModel twice");
            } else {
                FullbrightBakedModel customModel = new FullbrightBakedModel(existingModel);
                event.getModelRegistry().put(modelResourceLocation, customModel);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onTextureStich(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_PARTICLES)) {
            Constants.ParticleConstants.Values.PARTICLE_OPTIONS.forEach(event::addSprite);
        }
    }

    public static void addParticle(ResourceLocation spriteLocation, int color, int lifetime, float size, Vec3 pos,
                                   Vec3 motion, float gravity, boolean collision, boolean fullBright) {
        Minecraft minecraft = Minecraft.getInstance();
        VFXParticle particle = new VFXParticle(minecraft.level,
                                               minecraft.particleEngine.textureAtlas.getSprite(spriteLocation), color,
                                               lifetime, size, pos, motion, gravity, collision, fullBright
        );
        minecraft.particleEngine.add(particle);
    }

    public static void openVFXGeneratorScreen(VFXGeneratorBlockEntity tileEntity) {
        Minecraft.getInstance().setScreen(new ParticleOptionsScreen(tileEntity));
    }

    public static TranslatableComponent getGuiTranslationTextComponent(String suffix) {
        return new TranslatableComponent("gui." + VFXGenerator.MOD_ID + "." + suffix);
    }
}
