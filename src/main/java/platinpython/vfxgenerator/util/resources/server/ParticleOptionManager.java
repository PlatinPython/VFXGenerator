package platinpython.vfxgenerator.util.resources.server;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.resources.common.ParticleOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ParticleOptionManager
        extends SimplePreparableReloadListener<Multimap<ResourceLocation, Pair<String, ParticleOptions>>> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ParticleOptionManager() {
    }

    @Override
    protected Multimap<ResourceLocation, Pair<String, ParticleOptions>> prepare(
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        FileToIdConverter converter = FileToIdConverter.json(VFXGenerator.MOD_ID);
        Multimap<ResourceLocation, Pair<String, ParticleOptions>> map = ArrayListMultimap.create();
        Map<ResourceLocation, List<Resource>> resourceStacks = resourceManager.listResourceStacks(
                VFXGenerator.MOD_ID, resourceLocation -> resourceLocation.getPath()
                                                                         .equals(VFXGenerator.MOD_ID + "/particle.json"));
        resourceStacks.forEach((key, value) -> value.forEach(
                resource -> parseJsonResource(ParticleOptions.CODEC, key, resource, LOGGER::error,
                                              options -> map.put(
                                                      converter.fileToId(key),
                                                      Pair.of(resource.sourcePackId(), options)
                                              )
                )));
        return map;
    }

    @Override
    protected void apply(
            Multimap<ResourceLocation, Pair<String, ParticleOptions>> data,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        data.forEach((location, sourceOptionsPair) -> sourceOptionsPair.getSecond()
                                                                       .debug(location, sourceOptionsPair.getFirst()));
    }

    private <T> void parseJsonResource(
            Codec<T> codec,
            ResourceLocation origin,
            Resource resource,
            Consumer<String> failureAction,
            Consumer<? super T> successfulAction
    ) {
        try (BufferedReader reader = resource.openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            codec.parse(JsonOps.INSTANCE, jsonElement).resultOrPartial(failureAction).ifPresent(successfulAction);
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Failed to parse data file {} from {}", origin, resource.sourcePackId(), e);
        }
    }
}
