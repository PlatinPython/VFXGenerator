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
import platinpython.vfxgenerator.util.Util.VoidFunction;

public class VFXGeneratorOptionsList extends AbstractOptionList<VFXGeneratorOptionsList.VFXGeneratorOptionsListEntry> {
	public VFXGeneratorOptionsList(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
		super(minecraft, width, height, top, bottom, itemHeight);
		this.setRenderBackground(false);
	}

	public void addToggleButton(ITextComponent displayTextFalse, ITextComponent displayTextTrue, Consumer<Boolean> setValueFunction, Supplier<Boolean> valueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addToggleButton(this.width, displayTextFalse, displayTextTrue, setValueFunction, valueSupplier, applyValueFunction));
	}

	public void addSlider(ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addSlider(this.width, displayText, minValue, maxValue, stepSize, setValueFunction, valueSupplier, applyValueFunction));
	}

	public void addRangeSlider(ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setLeftValueFunction, Consumer<Float> setRightValueFunction, Supplier<Float> leftValueSupplier, Supplier<Float> rightValueSupplier, VoidFunction applyValueFunction) {
		this.addEntry(VFXGeneratorOptionsListEntry.addRangeSlider(this.width, displayText, minValue, maxValue, stepSize, setLeftValueFunction, setRightValueFunction, leftValueSupplier, rightValueSupplier, applyValueFunction));
	}

	public void addToggleableRangeSlider(float stepSize, ITextComponent displayTextFirst, double minValueFirst, double maxValueFirst, Consumer<Float> setLeftValueFunctionFirst, Consumer<Float> setRightValueFunctionFirst, Supplier<Float> leftValueSupplierFirst, Supplier<Float> rightValueSupplierFirst, ITextComponent displayTextSecond, double minValueSecond, double maxValueSecond, Consumer<Float> setLeftValueFunctionSecond, Consumer<Float> setRightValueFunctionSecond, Supplier<Float> leftValueSupplierSecond, Supplier<Float> rightValueSupplierSecond, VoidFunction applyValueFunction, Supplier<Boolean> toggleValueSupplier) {
		this.addEntry(ToggleableVFXGeneratorOptionsListEntry.addToggleableRangeSlider(this.width, stepSize, displayTextFirst, minValueFirst, maxValueFirst, setLeftValueFunctionFirst, setRightValueFunctionFirst, leftValueSupplierFirst, rightValueSupplierFirst, displayTextSecond, minValueSecond, maxValueSecond, setLeftValueFunctionSecond, setRightValueFunctionSecond, leftValueSupplierSecond, rightValueSupplierSecond, applyValueFunction, toggleValueSupplier));
	}

	public int getRowWidth() {
		return 400;
	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 32;
	}

	public static class VFXGeneratorOptionsListEntry extends AbstractOptionList.Entry<VFXGeneratorOptionsListEntry> {
		private final UpdateableWidget child;

		private VFXGeneratorOptionsListEntry(UpdateableWidget child) {
			this.child = child;
		}

		public static VFXGeneratorOptionsListEntry addToggleButton(int guiWidth, ITextComponent displayTextFalse, ITextComponent displayTextTrue, Consumer<Boolean> setValueFunction, Supplier<Boolean> valueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new ToggleButton(guiWidth / 2 - 155, 0, 310, 20, displayTextFalse, displayTextTrue, setValueFunction, valueSupplier, applyValueFunction));
		}

		public static VFXGeneratorOptionsListEntry addSlider(int guiWidth, ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setValueFunction, Supplier<Float> valueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new FloatSlider(guiWidth / 2 - 155, 0, 310, 20, displayText, minValue, maxValue, stepSize, setValueFunction, valueSupplier, applyValueFunction));
		}

		public static VFXGeneratorOptionsListEntry addRangeSlider(int guiWidth, ITextComponent displayText, double minValue, double maxValue, float stepSize, Consumer<Float> setLeftValueFunction, Consumer<Float> setRightValueFunction, Supplier<Float> leftValueSupplier, Supplier<Float> rightValueSupplier, VoidFunction applyValueFunction) {
			return new VFXGeneratorOptionsListEntry(new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, displayText, minValue, maxValue, stepSize, setLeftValueFunction, setRightValueFunction, leftValueSupplier, rightValueSupplier, applyValueFunction));
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

	public static class ToggleableVFXGeneratorOptionsListEntry extends VFXGeneratorOptionsListEntry {
		private final UpdateableWidget firstChild;
		private final UpdateableWidget secondChild;

		private final Supplier<Boolean> toggleValueSupplier;

		public ToggleableVFXGeneratorOptionsListEntry(UpdateableWidget firstChild, UpdateableWidget secondChild, Supplier<Boolean> toggleValueSupplier) {
			super(firstChild);
			this.firstChild = firstChild;
			this.secondChild = secondChild;
			this.toggleValueSupplier = toggleValueSupplier;
			this.updateValue();
		}

		public static ToggleableVFXGeneratorOptionsListEntry addToggleableRangeSlider(int guiWidth, float stepSize, ITextComponent displayTextFirst, double minValueFirst, double maxValueFirst, Consumer<Float> setLeftValueFunctionFirst, Consumer<Float> setRightValueFunctionFirst, Supplier<Float> leftValueSupplierFirst, Supplier<Float> rightValueSupplierFirst, ITextComponent displayTextSecond, double minValueSecond, double maxValueSecond, Consumer<Float> setLeftValueFunctionSecond, Consumer<Float> setRightValueFunctionSecond, Supplier<Float> leftValueSupplierSecond, Supplier<Float> rightValueSupplierSecond, VoidFunction applyValueFunction, Supplier<Boolean> toggleValueSupplier) {
			return new ToggleableVFXGeneratorOptionsListEntry(new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, displayTextFirst, minValueFirst, maxValueFirst, stepSize, setLeftValueFunctionFirst, setRightValueFunctionFirst, leftValueSupplierFirst, rightValueSupplierFirst, applyValueFunction), new FloatRangeSlider(guiWidth / 2 - 155, 0, 310, 20, displayTextSecond, minValueSecond, maxValueSecond, stepSize, setLeftValueFunctionSecond, setRightValueFunctionSecond, leftValueSupplierSecond, rightValueSupplierSecond, applyValueFunction), toggleValueSupplier);
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
}
