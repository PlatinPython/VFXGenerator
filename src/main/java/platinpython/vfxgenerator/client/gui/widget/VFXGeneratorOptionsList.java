package platinpython.vfxgenerator.client.gui.widget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import platinpython.vfxgenerator.util.Util.VoidFunction;

public class VFXGeneratorOptionsList extends AbstractOptionList<VFXGeneratorOptionsList.VFXGeneratorOptionsListEntry> {
	public VFXGeneratorOptionsList(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
		super(minecraft, width, height, top, bottom, itemHeight);
		this.setRenderBackground(false);
	}

	public void addToggleButton(ITextComponent displayTextFalse, ITextComponent displayTextTrue, Consumer<Boolean> setValueFunction, Supplier<Boolean> valueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addToggleButton(this.width, displayTextFalse, displayTextTrue, setValueFunction, valueSupplier, applyValueFunction));
	}

	public void addMultipleChoiceButton(ImmutableList<String> options, Consumer<String> setValueFunction, Supplier<String> valueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addMultipleChoiceButton(this.width, options, setValueFunction, valueSupplier, applyValueFunction));
	}

	public void addSlider(ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addSlider(this.width, prefix, suffix, minValue, maxValue, stepSize, setValueFunction, valueSupplier, applyValueFunction));
	}

	public void addRangeSlider(ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, float stepSize, Consumer<Float> setLeftValueFunction, Consumer<Float> setRightValueFunction, Supplier<Float> leftValueSupplier, Supplier<Float> rightValueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addRangeSlider(this.width, prefix, suffix, minValue, maxValue, stepSize, setLeftValueFunction, setRightValueFunction, leftValueSupplier, rightValueSupplier, applyValueFunction));
	}

	public ToggleableRangeSliderBuilder getToggleableRangeSliderBuilder() {
		return new ToggleableRangeSliderBuilder(this, this.width);
	}

	@Override
	public int getRowWidth() {
		return 400;
	}

	@Override
	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 32;
	}

	public static class VFXGeneratorOptionsListEntry extends AbstractOptionList.Entry<VFXGeneratorOptionsListEntry> {
		private final UpdateableWidget child;

		private VFXGeneratorOptionsListEntry(UpdateableWidget child) {
			this.child = child;
		}

		public static VFXGeneratorOptionsListEntry addToggleButton(int guiWidth, ITextComponent displayTextFalse, ITextComponent displayTextTrue, Consumer<Boolean> setValueFunction, Supplier<Boolean> valueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new ToggleTextButton(guiWidth / 2 - 155, 0, 310, 20, displayTextFalse, displayTextTrue, setValueFunction, valueSupplier, applyValueFunction));
		}

		public static VFXGeneratorOptionsListEntry addMultipleChoiceButton(int guiWidth, ImmutableList<String> options, Consumer<String> setValueFunction, Supplier<String> valueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new MultipleStringChoiceButton(guiWidth / 2 - 155, 0, 310, 20, options, setValueFunction, valueSupplier, applyValueFunction));
		}

		public static VFXGeneratorOptionsListEntry addSlider(int guiWidth, ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new FloatSlider(guiWidth / 2 - 155, 0, 310, 20, prefix, suffix, minValue, maxValue, stepSize, setValueFunction, valueSupplier, applyValueFunction));
		}

		public static VFXGeneratorOptionsListEntry addRangeSlider(int guiWidth, ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, float stepSize, Consumer<Float> setLeftValueFunction, Consumer<Float> setRightValueFunction, Supplier<Float> leftValueSupplier, Supplier<Float> rightValueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, prefix, suffix, minValue, maxValue, stepSize, setLeftValueFunction, setRightValueFunction, leftValueSupplier, rightValueSupplier, applyValueFunction));
		}

		@Override
		public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
			this.child.y = top;
			this.child.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		public void setActive(boolean active) {
			this.child.active = active;
		}

		public void updateValue() {
			this.child.updateValue();
		}

		@Override
		public List<? extends IGuiEventListener> children() {
			return ImmutableList.of(this.child);
		}
	}

	public class ToggleableVFXGeneratorOptionsListEntry extends VFXGeneratorOptionsListEntry {
		private final UpdateableWidget firstChild;
		private final UpdateableWidget secondChild;

		private final Supplier<Boolean> toggleValueSupplier;

		public ToggleableVFXGeneratorOptionsListEntry(UpdateableWidget firstChild, UpdateableWidget secondChild, Supplier<Boolean> toggleValueSupplier) {
			super(null);
			this.firstChild = firstChild;
			this.secondChild = secondChild;
			this.toggleValueSupplier = toggleValueSupplier;
			this.updateValue();
		}

		@Override
		public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
			this.firstChild.y = top;
			this.secondChild.y = top;
			this.firstChild.render(matrixStack, mouseX, mouseY, partialTicks);
			this.secondChild.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		@Override
		public void setActive(boolean active) {
			this.firstChild.active = active;
			this.secondChild.active = active;
		}

		@Override
		public void updateValue() {
			this.firstChild.updateValue();
			this.secondChild.updateValue();
			if (toggleValueSupplier.get()) {
				this.firstChild.visible = false;
				this.secondChild.visible = true;
			} else {
				this.firstChild.visible = true;
				this.secondChild.visible = false;
			}
		}

		@Override
		public List<? extends IGuiEventListener> children() {
			return ImmutableList.of(this.firstChild, this.secondChild);
		}
	}

	public final class ToggleableRangeSliderBuilder {
		private final VFXGeneratorOptionsList list;
		private final int guiWidth;

		private float stepSize = 1F;

		private ITextComponent prefixFirst = StringTextComponent.EMPTY;
		private ITextComponent suffixFirst = StringTextComponent.EMPTY;
		private double minValueFirst = 0F;
		private double maxValueFirst = 0F;
		private Consumer<Float> setLeftValueFunctionFirst = (value) -> {
		};
		private Consumer<Float> setRightValueFunctionFirst = (value) -> {
		};
		private Supplier<Float> leftValueSupplierFirst = () -> 0F;
		private Supplier<Float> rightValueSupplierFirst = () -> 0F;

		private ITextComponent prefixSecond = StringTextComponent.EMPTY;
		private ITextComponent suffixSecond = StringTextComponent.EMPTY;
		private double minValueSecond = 0F;
		private double maxValueSecond = 0F;
		private Consumer<Float> setLeftValueFunctionSecond = (value) -> {
		};
		private Consumer<Float> setRightValueFunctionSecond = (value) -> {
		};
		private Supplier<Float> leftValueSupplierSecond = () -> 0F;
		private Supplier<Float> rightValueSupplierSecond = () -> 0F;

		private VoidFunction applyValueFunction = () -> {
		};
		private Supplier<Boolean> toggleValueSupplier = () -> false;

		private ToggleableRangeSliderBuilder(VFXGeneratorOptionsList list, int guiWidth) {
			this.list = list;
			this.guiWidth = guiWidth;
		}

		public ToggleableRangeSliderBuilder stepSize(float stepSize) {
			this.stepSize = stepSize;
			return this;
		}

		public ToggleableRangeSliderBuilder prefixFirst(ITextComponent prefixFirst) {
			this.prefixFirst = prefixFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder suffixFirst(ITextComponent suffixFirst) {
			this.suffixFirst = suffixFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder minValueFirst(double minValueFirst) {
			this.minValueFirst = minValueFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder maxValueFirst(double maxValueFirst) {
			this.maxValueFirst = maxValueFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder setLeftValueFunctionFirst(Consumer<Float> setLeftValueFunctionFirst) {
			this.setLeftValueFunctionFirst = setLeftValueFunctionFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder setRightValueFunctionFirst(Consumer<Float> setRightValueFunctionFirst) {
			this.setRightValueFunctionFirst = setRightValueFunctionFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder leftValueSupplierFirst(Supplier<Float> leftValueSupplierFirst) {
			this.leftValueSupplierFirst = leftValueSupplierFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder rightValueSupplierFirst(Supplier<Float> rightValueSupplierFirst) {
			this.rightValueSupplierFirst = rightValueSupplierFirst;
			return this;
		}

		public ToggleableRangeSliderBuilder prefixSecond(ITextComponent prefixSecond) {
			this.prefixSecond = prefixSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder suffixSecond(ITextComponent suffixSecond) {
			this.suffixSecond = suffixSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder minValueSecond(double minValueSecond) {
			this.minValueSecond = minValueSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder maxValueSecond(double maxValueSecond) {
			this.maxValueSecond = maxValueSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder setLeftValueFunctionSecond(Consumer<Float> setLeftValueFunctionSecond) {
			this.setLeftValueFunctionSecond = setLeftValueFunctionSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder setRightValueFunctionSecond(Consumer<Float> setRightValueFunctionSecond) {
			this.setRightValueFunctionSecond = setRightValueFunctionSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder leftValueSupplierSecond(Supplier<Float> leftValueSupplierSecond) {
			this.leftValueSupplierSecond = leftValueSupplierSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder rightValueSupplierSecond(Supplier<Float> rightValueSupplierSecond) {
			this.rightValueSupplierSecond = rightValueSupplierSecond;
			return this;
		}

		public ToggleableRangeSliderBuilder applyValueFunction(VoidFunction applyValueFunction) {
			this.applyValueFunction = applyValueFunction;
			return this;
		}

		public ToggleableRangeSliderBuilder toggleValueSupplier(Supplier<Boolean> toggleValueSupplier) {
			this.toggleValueSupplier = toggleValueSupplier;
			return this;
		}

		public void build() {
			FloatRangeSlider firstSlider = new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, prefixFirst, suffixFirst, minValueFirst, maxValueFirst, stepSize, setLeftValueFunctionFirst, setRightValueFunctionFirst, leftValueSupplierFirst, rightValueSupplierFirst, applyValueFunction);
			FloatRangeSlider secondSlider = new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, prefixSecond, suffixSecond, minValueSecond, maxValueSecond, stepSize, setLeftValueFunctionSecond, setRightValueFunctionSecond, leftValueSupplierSecond, rightValueSupplierSecond, applyValueFunction);
			this.list.addEntry(new ToggleableVFXGeneratorOptionsListEntry(firstSlider, secondSlider, toggleValueSupplier));
		}
	}
}
