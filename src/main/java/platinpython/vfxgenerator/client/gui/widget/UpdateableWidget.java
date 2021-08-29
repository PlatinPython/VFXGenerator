package platinpython.vfxgenerator.client.gui.widget;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import platinpython.vfxgenerator.util.Util.VoidFunction;

public abstract class UpdateableWidget extends Widget {
	protected final VoidFunction applyValueFunction;

	protected final ITextComponent displayText;

	public UpdateableWidget(int x, int y, int width, int height, ITextComponent displayText, VoidFunction applyValueFunction) {
		super(x, y, width, height, displayText);
		this.applyValueFunction = applyValueFunction;
		this.displayText = displayText;
	}

	public abstract void updateValue();

	protected abstract void updateMessage();

	protected void applyValue() {
		this.applyValueFunction.apply();
	}
}
