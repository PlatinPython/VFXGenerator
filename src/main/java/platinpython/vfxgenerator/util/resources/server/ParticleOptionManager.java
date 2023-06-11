package platinpython.vfxgenerator.util.resources.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.resources.common.ParticleOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticleOptionManager extends SimplePreparableReloadListener<List<ParticleOptions>> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogUtils.getLogger();

    public ParticleOptionManager() {
    }

    @Override
    protected List<ParticleOptions> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        List<ParticleOptions> list = new ArrayList<>();
        Map<ResourceLocation, Resource> resourceLocationResourceMap = resourceManager.listResources(VFXGenerator.MOD_ID,
                                                                                                    resourceLocation -> resourceLocation.getPath()
                                                                                                                                        .equals(VFXGenerator.MOD_ID + "/particle.json")
        );
        for (Map.Entry<ResourceLocation, Resource> resourceLocationResourceEntry : resourceLocationResourceMap.entrySet()) {
            try (BufferedReader bufferedReader = resourceLocationResourceEntry.getValue().openAsReader()) {
                JsonElement jsonElement = GsonHelper.fromJson(GSON, bufferedReader, JsonElement.class);
                ParticleOptions.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                                     .resultOrPartial(LOGGER::error)
                                     .ifPresent(list::add);
            } catch (IllegalArgumentException | IOException | JsonParseException ignored) {
            }
        }
        return list;
    }

    @Override
    protected void apply(List<ParticleOptions> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        data.forEach(ParticleOptions::debug);
    }
}
