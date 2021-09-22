package platinpython.vfxgenerator.client.gui.widget;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.StringUtils;
import platinpython.vfxgenerator.util.Util.VoidFunction;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MultipleStringChoiceButton extends UpdateableWidget {
	private final ImmutableList<String> options;

	private final Consumer<String> setValueFunction;
	private final Supplier<String> valueSupplier;

	public MultipleStringChoiceButton(int x, int y, int width, int height, ImmutableList<String> options, Consumer<String> setValueFunction, Supplier<String> valueSupplier, VoidFunction applyValueFunction) {
		super(x, y, width, height, applyValueFunction);
		this.options = options;
		this.setValueFunction = setValueFunction;
		this.valueSupplier = valueSupplier;
		this.updateMessage();
	}

	@Override
	protected int getYImage(boolean pIsHovered) {
		if (!this.active)
			return 1;
		return isHovered ? 2 : 1;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (this.options.contains(this.valueSupplier.get())) {
			this.setValueFunction.accept(this.options.get((this.options.indexOf(this.valueSupplier.get()) + 1) % this.options.size()));
		} else {
			this.setValueFunction.accept(this.options.size() != 0 ? this.options.get(0) : "");
		}
		this.updateMessage();
		this.applyValue();
	}

	@Override
	public void updateValue() {
		this.updateMessage();
	}

	@Override
	protected void updateMessage() {
		this.setMessage(new StringTextComponent(StringUtils.capitalize(this.valueSupplier.get())));
	}
}
