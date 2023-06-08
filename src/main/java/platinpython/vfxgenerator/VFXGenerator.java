package platinpython.vfxgenerator;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import platinpython.vfxgenerator.data.DataGatherer;
import platinpython.vfxgenerator.util.RegistryHandler;
import platinpython.vfxgenerator.util.network.NetworkHandler;
import platinpython.vfxgenerator.util.registries.BlockRegistry;
import platinpython.vfxgenerator.util.registries.ItemRegistry;

@Mod(VFXGenerator.MOD_ID)
public class VFXGenerator {
    public static final String MOD_ID = "vfxgenerator";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public VFXGenerator() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGatherer::onGatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(VFXGenerator::addItemsToTab);

        RegistryHandler.register();

        NetworkHandler.register();
    }

    public static void addItemsToTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() != CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            return;
        }
        event.accept(BlockRegistry.VFX_GENERATOR);
        event.accept(ItemRegistry.VFX_GENERATOR_CORE);
    }
}
