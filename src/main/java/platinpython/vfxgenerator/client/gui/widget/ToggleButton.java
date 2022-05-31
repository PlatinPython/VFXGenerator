package platinpython.vfxgenerator.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.fml.client.gui.GuiUtils;
import platinpython.vfxgenerator.util.Util;

public class ToggleButton extends UpdateableWidget {
    private final Util.BooleanConsumer setValueFunction;
    private final Util.BooleanSupplier valueSupplier;

    public ToggleButton(int x, int y, int width, int height, Util.BooleanConsumer setValueFunction,
                        Util.BooleanSupplier valueSupplier, Runnable applyValueFunction) {
        super(x, y, width, height, applyValueFunction);
        this.setValueFunction = setValueFunction;
        this.valueSupplier = valueSupplier;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFF000000);
        if (this.valueSupplier.get()) {
            AbstractGui.fill(matrixStack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1,
                             0xFF00FF00
            );
            GuiUtils.drawContinuousTexturedBox(matrixStack, WIDGETS_LOCATION, this.x + this.width / 2, this.y, 0,
                                               46 + this.getYImage(isHovered) * 20, this.width / 2, this.height, 200,
                                               20, 2, 2, 2, 2, this.getBlitOffset()
            );
        } else {
            AbstractGui.fill(matrixStack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1,
                             0xFFFF0000
            );
            GuiUtils.drawContinuousTexturedBox(matrixStack, WIDGETS_LOCATION, this.x, this.y, 0,
                                               46 + this.getYImage(isHovered) * 20, this.width / 2, this.height, 200,
                                               20, 2, 2, 2, 2, this.getBlitOffset()
            );
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFunction.accept(!this.valueSupplier.get());
        this.applyValue();
    }

    @Override
    public void updateValue() {
    }

    @Override
    protected void updateMessage() {
    }
}
