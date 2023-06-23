package platinpython.vfxgenerator.util.resources;

import com.mojang.serialization.DataResult;
import de.matthiasmann.twl.utils.PNGDecoder;
import net.minecraft.server.packs.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ResourceUtil {
    public static DataResult<Boolean> supportsColor(Resource texture) {
        try {
            return DataResult.success(testIfGrayscale(texture.open()));
        } catch (IOException e) {
            return DataResult.error(() -> "Failed to test if image is grayscale: " + e.getMessage());
        }
    }

    private static boolean testIfGrayscale(InputStream inputStream) throws IOException {
        PNGDecoder pngDecoder = new PNGDecoder(inputStream);
        if (!pngDecoder.isRGB()) {
            return true;
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * pngDecoder.getWidth() * pngDecoder.getHeight());
        pngDecoder.decode(buffer, pngDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i += 4) {
            if (buffer.get(i + 3) == 0) {
                continue;
            }
            if (buffer.get(i) != buffer.get(i + 1) || buffer.get(i + 1) != buffer.get(i + 2)) {
                return false;
            }
        }
        return true;
    }
}
