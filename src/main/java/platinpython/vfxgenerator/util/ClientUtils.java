package platinpython.vfxgenerator.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.block.VFXGeneratorBlock;
import platinpython.vfxgenerator.block.entity.VFXGeneratorBlockEntity;
import platinpython.vfxgenerator.client.gui.screen.ParticleOptionsScreen;
import platinpython.vfxgenerator.client.particle.VFXParticle;
import platinpython.vfxgenerator.util.registries.BlockRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@EventBusSubscriber(modid = VFXGenerator.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
    private static final Path CACHE_DIRECTORY = FMLPaths.GAMEDIR.get().resolve(".cache").resolve(VFXGenerator.MOD_ID);
    private static final byte[] PNG_SIGNATURE = {(byte) 137, 80, 78, 71, 13, 10, 26, 10};
    private static final HashMap<ResourceLocation, HashCode> HASH_CACHE = new HashMap<>();

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

        buildHashCache();
    }

    private static void buildHashCache() {
        HASH_CACHE.clear();
        if (Files.notExists(CACHE_DIRECTORY)) {
            return;
        }
        try (Stream<Path> subpaths = Files.walk(CACHE_DIRECTORY, 128)) {
            subpaths.forEach(path -> {
                if (Files.isDirectory(path) || !path.toString().endsWith(".png")) {
                    return;
                }
                Path relativePath = CACHE_DIRECTORY.relativize(path);
                if (relativePath.getNameCount() < 2) {
                    return;
                }
                String namespace = relativePath.getName(0).toString();
                String location = relativePath.subpath(1, relativePath.getNameCount()).toString().replace('\\', '/');
                location = location.substring(0, location.length() - 4);
                ResourceLocation resourceLocation = new ResourceLocation(namespace, location);
                try (InputStream file = Files.newInputStream(path)) {
                    byte[] signature = file.readNBytes(8);
                    if (!Arrays.equals(signature, PNG_SIGNATURE)) {
                        VFXGenerator.LOGGER.error("{} is not a valid png file.", path);
                        return;
                    }
                    byte[] bytes = file.readAllBytes();
                    //noinspection UnstableApiUsage
                    HashCode hash = Util.HASH_FUNCTION.newHasher().putBytes(signature).putBytes(bytes).hash();
                    HASH_CACHE.put(resourceLocation, hash);
                } catch (IOException e) {
                    VFXGenerator.LOGGER.error("Failed to hash file {}", path.getFileName(), e);
                }
            });
        } catch (IOException e) {
            VFXGenerator.LOGGER.error("Failed to access cache directory", e);
        }
    }

    public static ImmutableList<ResourceLocation> getMissingTextures(ImmutableMap<ResourceLocation, HashCode> requiredTextures) {
        return requiredTextures.entrySet()
                               .stream()
                               .filter(entry -> !HASH_CACHE.containsKey(entry.getKey()) || !Objects.equals(
                                       HASH_CACHE.get(entry.getKey()), entry.getValue()))
                               .map(Map.Entry::getKey)
                               .collect(ImmutableList.toImmutableList());
    }

    public static void addToCache(ResourceLocation resourceLocation, byte[] image) {
        Path relativePath = Path.of(resourceLocation.getNamespace()).resolve(resourceLocation.getPath() + ".png");
        Path path = CACHE_DIRECTORY.resolve(relativePath);
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            VFXGenerator.LOGGER.error("Failed to create directory {}", path.getParent(), e);
            return;
        }
        try (OutputStream file = Files.newOutputStream(path)) {
            file.write(image);
            HASH_CACHE.put(resourceLocation, Util.HASH_FUNCTION.hashBytes(image));
        } catch (IOException e) {
            VFXGenerator.LOGGER.error("Failed to write image {}", path, e);
        }
    }

    public static void addParticle(
            ResourceLocation spriteLocation,
            int color,
            int lifetime,
            float size,
            Vec3 pos,
            Vec3 motion,
            float gravity,
            boolean collision,
            boolean fullBright
    ) {
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

    public static MutableComponent getGuiTranslationTextComponent(String suffix) {
        return Component.translatable("gui." + VFXGenerator.MOD_ID + "." + suffix);
    }
}
