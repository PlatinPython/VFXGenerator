package platinpython.vfxgenerator.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.gui.ScreenUtils;
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
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GuiComponent.fill(matrixStack, this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height,
                          0xFF000000
        );
        if (this.valueSupplier.get()) {
            GuiComponent.fill(matrixStack, this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1,
                              this.getY() + this.height - 1, 0xFF00FF00
            );
            ScreenUtils.blitWithBorder(matrixStack, WIDGETS_LOCATION, this.getX() + this.width / 2, this.getY(), 0,
                                       46 + this.getYImage(isHovered) * 20, this.width / 2, this.height, 200, 20, 2, 2,
                                       2, 2, this.getBlitOffset()
            );
        } else {
            GuiComponent.fill(matrixStack, this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1,
                              this.getY() + this.height - 1, 0xFFFF0000
            );
            ScreenUtils.blitWithBorder(matrixStack, WIDGETS_LOCATION, this.getX(), this.getY(), 0,
                                       46 + this.getYImage(isHovered) * 20, this.width / 2, this.height, 200, 20, 2, 2,
                                       2, 2, this.getBlitOffset()
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

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
