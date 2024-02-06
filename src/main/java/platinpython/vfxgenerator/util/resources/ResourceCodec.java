package platinpython.vfxgenerator.util.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class ResourceCodec implements Codec<Resource> {
    @Override
    public <T> DataResult<T> encode(Resource input, DynamicOps<T> ops, T prefix) {
        return DataResult.error(() -> "Encoding not supported");
    }

    @Override
    public <T> DataResult<Pair<Resource, T>> decode(DynamicOps<T> ops, T input) {
        if (ops instanceof ResourceOps<T> resourceOps) {
            return ResourceLocation.CODEC.parse(ops, input).flatMap(resourceLocation -> resourceOps.getResource(
                    resourceLocation)
                .map(resource -> DataResult.success(Pair.of(resource, input)))
                .orElseGet(() -> DataResult.error(
                    () -> String.format("Resource %s not found", resourceOps.idToFile(resourceLocation)))));
        } else {
            return DataResult.error(() -> "Not a resource ops");
        }
    }
}
