package platinpython.vfxgenerator.client.gui.widget;

import net.minecraft.util.text.ITextComponent;
import platinpython.vfxgenerator.util.Util;

public class ToggleTextButton extends UpdateableWidget {
    private final ITextComponent displayTextFalse;
    private final ITextComponent displayTextTrue;

    private final Util.BooleanConsumer setValueFunction;
    private final Util.BooleanSupplier valueSupplier;

    public ToggleTextButton(int x, int y, int width, int height, ITextComponent displayTextFalse,
                            ITextComponent displayTextTrue, Util.BooleanConsumer setValueFunction,
                            Util.BooleanSupplier valueSupplier, Runnable applyValueFunction) {
        super(x, y, width, height, applyValueFunction);
        this.displayTextFalse = displayTextFalse;
        this.displayTextTrue = displayTextTrue;
        this.setValueFunction = setValueFunction;
        this.valueSupplier = valueSupplier;
        this.updateMessage();
    }

    @Override
    protected int getYImage(boolean isHovered) {
        if (!this.active)
            return 1;
        return isHovered ? 2 : 1;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFunction.accept(!this.valueSupplier.get());
        this.updateMessage();
        this.applyValue();
    }

    @Override
    public void updateValue() {
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(valueSupplier.get() ? displayTextTrue : displayTextFalse);
    }
}
