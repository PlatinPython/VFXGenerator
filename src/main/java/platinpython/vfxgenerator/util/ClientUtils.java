package platinpython.vfxgenerator.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;
import platinpython.vfxgenerator.client.gui.screen.ParticleOptionsScreen;
import platinpython.vfxgenerator.client.particle.VFXParticle;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

@EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                BlockRegistry.VFX_GENERATOR.get().asItem(),
                Util.createNamespacedResourceLocation(
                        VFXGeneratorBlock.INVERTED_KEY),
                (stack, world, entity, seed) -> Boolean.parseBoolean(
                        stack.getOrCreateTagElement("BlockStateTag")
                             .getString(VFXGeneratorBlock.INVERTED_KEY)) ? 1F
                                                                         : 0F
        ));
    }

    public static void addParticle(
            ClientLevel level,
            ParticleType particleType,
            int color,
            int lifetime,
            float size,
            Vec3 pos,
            Vec3 motion,
            float gravity,
            boolean collision,
            boolean fullBright
    ) {
        VFXParticle particle = new VFXParticle(level, particleType, color, lifetime, size, pos, motion, gravity,
                                               collision, fullBright
        );
        Minecraft.getInstance().particleEngine.add(particle);
    }

    public static void openVFXGeneratorScreen(VFXGeneratorBlockEntity tileEntity) {
        Minecraft.getInstance().setScreen(new ParticleOptionsScreen(tileEntity));
    }

    public static MutableComponent getGuiTranslationTextComponent(String suffix) {
        return Component.translatable("gui." + VFXGenerator.MOD_ID + "." + suffix);
    }
}
