package platinpython.vfxgenerator.util.resources.server;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.particle.ParticleType;
import platinpython.vfxgenerator.util.resources.DataManager;
import platinpython.vfxgenerator.util.resources.EventHandling;
import platinpython.vfxgenerator.util.resources.ResourceOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ParticleListLoader extends SimplePreparableReloadListener<Map<ResourceLocation, ParticleType>> {
    @Override
    protected Map<ResourceLocation, ParticleType> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        if (EventHandling.loadingDisabled()) {
            return Map.of();
        }
        FileToIdConverter particleListConverter = FileToIdConverter.json(VFXGenerator.MOD_ID);
        Multimap<ResourceLocation, ParticleListFile> optionsMap = ArrayListMultimap.create();
        Map<ResourceLocation, List<Resource>> resourceStacks = resourceManager.listResourceStacks(
            VFXGenerator.MOD_ID,
            resourceLocation -> resourceLocation.getPath().equals(VFXGenerator.MOD_ID + "/particle.json")
        );
        resourceStacks.forEach((key, value) -> value.forEach(
            resource -> parseJsonResource(JsonOps.INSTANCE, ParticleListFile.FILE_DECODER, key, resource,
                VFXGenerator.LOGGER::error, options -> {
                    if (options.replace()) {
                        optionsMap.removeAll(particleListConverter.fileToId(key));
                    }
                    optionsMap.put(particleListConverter.fileToId(key), options);
                }
            )));
        FileToIdConverter particleConverter = FileToIdConverter.json(VFXGenerator.MOD_ID + "/particle");
        Map<ResourceLocation, ParticleType> particleTypeMap = new HashMap<>();
        optionsMap.forEach((key, value) -> value.particles()
            .forEach(location -> resourceManager.getResource(particleConverter.idToFile(location))
                .ifPresentOrElse(
                    resource -> parseJsonResource(
                        new ResourceOps<>(JsonOps.INSTANCE, resourceManager,
                            new FileToIdConverter(VFXGenerator.MOD_ID + "/particle/textures", ".png")
                        ), ParticleType.FILE_DECODER, particleConverter.idToFile(location), resource,
                        VFXGenerator.LOGGER::error, type -> particleTypeMap.put(location, type)
                    ), () -> VFXGenerator.LOGGER.error("Failed to load resource {}, specified in {}",
                        particleConverter.idToFile(location), particleListConverter.idToFile(key)
                    ))));
        return particleTypeMap;
    }

    @Override
    protected void apply(
        Map<ResourceLocation, ParticleType> data,
        ResourceManager resourceManager,
        ProfilerFiller profiler
    ) {
        if (EventHandling.loadingDisabled()) {
            return;
        }
        DataManager.setSelectableParticles(ImmutableMap.copyOf(data));
        FileToIdConverter imageFileToIdConverter = new FileToIdConverter(
            VFXGenerator.MOD_ID + "/particle/textures", ".png");
        DataManager.setRequiredImages(data.values()
            .stream()
            .flatMap(ParticleType::images)
            .map(resourceLocation -> Pair.of(resourceLocation,
                resourceManager.getResource(imageFileToIdConverter.idToFile(resourceLocation))
            ))
            .filter(pair -> pair.getSecond().isPresent())
            .<Pair<ResourceLocation, IoSupplier<InputStream>>>map(
                pair -> Pair.of(pair.getFirst(), pair.getSecond().get()::open))
            .collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond)));
    }

    private <T> void parseJsonResource(
        DynamicOps<JsonElement> ops,
        Decoder<T> codec,
        ResourceLocation origin,
        Resource resource,
        Consumer<String> failureAction,
        Consumer<? super T> successfulAction
    ) {
        try (BufferedReader reader = resource.openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            codec.parse(ops, jsonElement).resultOrPartial(failureAction).ifPresent(successfulAction);
        } catch (IOException | JsonParseException e) {
            VFXGenerator.LOGGER.error("Failed to parse data file {} from {}", origin, resource.sourcePackId(), e);
        }
    }
}
