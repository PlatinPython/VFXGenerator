package platinpython.vfxgenerator.client.gui.widget;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import platinpython.vfxgenerator.util.Util.VoidFunction;

public abstract class UpdateableWidget extends Widget {
    protected final VoidFunction applyValueFunction;

    public UpdateableWidget(int x, int y, int width, int height, VoidFunction applyValueFunction) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.applyValueFunction = applyValueFunction;
    }

    public abstract void updateValue();

    protected abstract void updateMessage();

    protected void applyValue() {
        this.applyValueFunction.apply();
    }
}
