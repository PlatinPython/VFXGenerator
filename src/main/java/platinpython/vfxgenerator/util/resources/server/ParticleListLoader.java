package platinpython.vfxgenerator.util.resources.server;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.resources.ResourceOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ParticleListLoader extends
                                SimplePreparableReloadListener<Multimap<ResourceLocation, Pair<String, Pair<ParticleListFile, Map<ResourceLocation, ParticleType>>>>> {
    public static final Logger LOGGER = LogUtils.getLogger();

    public ParticleListLoader() {
    }

    @Override
    protected Multimap<ResourceLocation, Pair<String, Pair<ParticleListFile, Map<ResourceLocation, ParticleType>>>> prepare(
            ResourceManager resourceManager, ProfilerFiller profiler
    ) {
        FileToIdConverter particleListConverter = FileToIdConverter.json(VFXGenerator.MOD_ID);
        Multimap<ResourceLocation, Pair<String, ParticleListFile>> optionsMap = ArrayListMultimap.create();
        Map<ResourceLocation, List<Resource>> resourceStacks = resourceManager.listResourceStacks(
                VFXGenerator.MOD_ID, resourceLocation -> resourceLocation.getPath()
                                                                         .equals(VFXGenerator.MOD_ID + "/particle.json"));
        resourceStacks.forEach((key, value) -> value.forEach(
                resource -> parseJsonResource(JsonOps.INSTANCE, ParticleListFile.CODEC, key, resource, LOGGER::error,
                                              options -> optionsMap.put(
                                                      particleListConverter.fileToId(key),
                                                      Pair.of(resource.sourcePackId(), options)
                                              )
                )));
        FileToIdConverter particleConverter = FileToIdConverter.json(VFXGenerator.MOD_ID + "/particle");
        Multimap<ResourceLocation, Pair<String, Pair<ParticleListFile, Map<ResourceLocation, ParticleType>>>> particleTypeMap = ArrayListMultimap.create();
        optionsMap.forEach((key, value) -> {
            Map<ResourceLocation, ParticleType> map = new HashMap<>();
            value.getSecond()
                 .particles()
                 .forEach(location -> resourceManager.getResource(particleConverter.idToFile(location))
                                                     .ifPresentOrElse(resource -> parseJsonResource(
                                                             new ResourceOps<>(JsonOps.INSTANCE, resourceManager,
                                                                               new FileToIdConverter(
                                                                                       VFXGenerator.MOD_ID + "/particle/textures",
                                                                                       ".png"
                                                                               )
                                                             ), ParticleType.CODEC,
                                                             particleConverter.idToFile(location), resource,
                                                             LOGGER::error, type -> map.put(location, type)
                                                     ), () -> LOGGER.error(
                                                             "Failed to load resource {}, specified in {} from {}",
                                                             particleConverter.idToFile(location),
                                                             particleListConverter.idToFile(key), value.getFirst()
                                                     )));
            particleTypeMap.put(key, Pair.of(value.getFirst(), Pair.of(value.getSecond(), map)));
        });
        return particleTypeMap;
    }

    @Override
    protected void apply(
            Multimap<ResourceLocation, Pair<String, Pair<ParticleListFile, Map<ResourceLocation, ParticleType>>>> data,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        data.forEach((optionsLocation, sourceOptionsTypesPairPair) -> {
            sourceOptionsTypesPairPair.getSecond()
                                      .getFirst()
                                      .debug(optionsLocation, sourceOptionsTypesPairPair.getFirst());
            sourceOptionsTypesPairPair.getSecond()
                                      .getSecond()
                                      .forEach((typeLocation, particleType) -> particleType.debug(
                                              typeLocation,
                                              optionsLocation,
                                              sourceOptionsTypesPairPair.getFirst()
                                      ));
        });
    }

    private <T> void parseJsonResource(
            DynamicOps<JsonElement> ops,
            Codec<T> codec,
            ResourceLocation origin,
            Resource resource,
            Consumer<String> failureAction,
            Consumer<? super T> successfulAction
    ) {
        try (BufferedReader reader = resource.openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            codec.parse(ops, jsonElement).resultOrPartial(failureAction).ifPresent(successfulAction);
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Failed to parse data file {} from {}", origin, resource.sourcePackId(), e);
        }
    }
}
