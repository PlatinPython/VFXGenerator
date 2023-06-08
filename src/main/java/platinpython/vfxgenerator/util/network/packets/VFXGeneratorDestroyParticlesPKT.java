package platinpython.vfxgenerator.util.network.packets;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import platinpython.vfxgenerator.VFXGenerator;
import platinpython.vfxgenerator.util.ClientUtils;
import platinpython.vfxgenerator.util.Color;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class VFXGeneratorDestroyParticlesPKT {
    private final Vec3 pos;

    public VFXGeneratorDestroyParticlesPKT(Vec3 pos) {
        this.pos = pos;
    }

    public static void encode(VFXGeneratorDestroyParticlesPKT message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.pos.x);
        buffer.writeDouble(message.pos.y);
        buffer.writeDouble(message.pos.z);
    }

    public static VFXGeneratorDestroyParticlesPKT decode(FriendlyByteBuf buffer) {
        return new VFXGeneratorDestroyParticlesPKT(
                new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
    }

    public static class Handler {
        public static void handle(VFXGeneratorDestroyParticlesPKT message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().getOriginationSide().isServer()) {
                    Random random = new Random();
                    List<ResourceLocation> list = ImmutableList.of(
                            new ResourceLocation(VFXGenerator.MOD_ID, "spark_small"),
                            new ResourceLocation(VFXGenerator.MOD_ID, "spark_mid"),
                            new ResourceLocation(VFXGenerator.MOD_ID, "spark_big")
                    );
                    for (int i = 0; i < 100; i++) {
                        double motionX = (random.nextFloat() * (.2F)) - .1F;
                        double motionY = (random.nextFloat() * (.2F)) - .1F;
                        double motionZ = (random.nextFloat() * (.2F)) - .1F;
                        Vec3 motion = new Vec3(motionX, motionY, motionZ);
                        ClientUtils.addParticle(list.get(random.nextInt(list.size())),
                                                Color.HSBtoRGB(random.nextFloat(), 1F, 1F),
                                                Math.round(5 + (random.nextFloat() * (15 - 5))), .3F, message.pos,
                                                motion, 0F, false, false
                        );
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
