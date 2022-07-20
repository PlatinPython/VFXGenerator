package platinpython.vfxgenerator.client.gui.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;

public abstract class UpdateableWidget extends AbstractWidget {
    protected final Runnable applyValueFunction;

    public UpdateableWidget(int x, int y, int width, int height, Runnable applyValueFunction) {
        super(x, y, width, height, TextComponent.EMPTY);
        this.applyValueFunction = applyValueFunction;
    }

    public abstract void updateValue();

    protected abstract void updateMessage();

    protected void applyValue() {
        this.applyValueFunction.run();
    }
}
