package platinpython.vfxgenerator.client.gui.widget;

import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.ResourceLocation;
import platinpython.vfxgenerator.util.Constants.ParticleConstants;
import platinpython.vfxgenerator.util.Util;
import platinpython.vfxgenerator.util.Util.VoidFunction;

public class TextureOptionsList extends AbstractOptionList<TextureOptionsList.TextureOptionsListEntry> {
	public TextureOptionsList(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight, Consumer<TreeSet<ResourceLocation>> setValueFunction, Supplier<TreeSet<ResourceLocation>> valueSupplier, VoidFunction applyValueFunction) {
		super(minecraft, width, height, top, bottom, itemHeight);
		this.init(setValueFunction, valueSupplier, applyValueFunction);
		this.setRenderBackground(false);
	}

	private void init(Consumer<TreeSet<ResourceLocation>> setValueFunction, Supplier<TreeSet<ResourceLocation>> valueSupplier, VoidFunction applyValueFunction) {
		List<ResourceLocation> list = Util.createTreeSetFromCollectionWithComparator(ParticleConstants.PARTICLE_OPTIONS, ResourceLocation::compareNamespaced).stream().collect(Collectors.toList());
		for (int i = 0; i < list.size() - list.size() % 3; i += 3) {
			addEntry(TextureOptionsListEntry.addThreeTextures(this.width, list.get(i), list.get(i + 1), list.get(i + 2), setValueFunction, valueSupplier, applyValueFunction));
		}
		switch (list.size() % 3) {
		case 1:
			addEntry(TextureOptionsListEntry.addOneTexture(this.width, list.get(list.size() - 1), setValueFunction, valueSupplier, applyValueFunction));
			break;
		case 2:
			addEntry(TextureOptionsListEntry.addTwoTextures(this.width, list.get(list.size() - 2), list.get(list.size() - 1), setValueFunction, valueSupplier, applyValueFunction));
			break;
		default:
			break;
		}
	}

	@Override
	public int getRowWidth() {
		return 400;
	}

	@Override
	protected int getScrollbarPosition() {
		return this.width / 2 + 76;
	}

	public static class TextureOptionsListEntry extends AbstractOptionList.Entry<TextureOptionsListEntry> {
		private final ImageSelectionWidget child1, child2, child3;

		private TextureOptionsListEntry(ImageSelectionWidget child1, ImageSelectionWidget child2, ImageSelectionWidget child3) {
			this.child1 = child1;
			this.child2 = child2;
			this.child3 = child3;
		}

		public static TextureOptionsListEntry addOneTexture(int guiWidth, ResourceLocation imageName1, Consumer<TreeSet<ResourceLocation>> setValueFunction, Supplier<TreeSet<ResourceLocation>> valueSupplier, VoidFunction applyValueFunction) {
			ImageSelectionWidget child1 = new ImageSelectionWidget(guiWidth / 2 - 25, 0, 50, 50, imageName1, setValueFunction, valueSupplier, applyValueFunction);
			return new TextureOptionsListEntry(child1, null, null);
		}

		public static TextureOptionsListEntry addTwoTextures(int guiWidth, ResourceLocation imageName1, ResourceLocation imageName2, Consumer<TreeSet<ResourceLocation>> setValueFunction, Supplier<TreeSet<ResourceLocation>> valueSupplier, VoidFunction applyValueFunction) {
			ImageSelectionWidget child1 = new ImageSelectionWidget(guiWidth / 2 - 50, 0, 50, 50, imageName1, setValueFunction, valueSupplier, applyValueFunction);
			ImageSelectionWidget child2 = new ImageSelectionWidget(guiWidth / 2, 0, 50, 50, imageName2, setValueFunction, valueSupplier, applyValueFunction);
			return new TextureOptionsListEntry(child1, child2, null);
		}

		public static TextureOptionsListEntry addThreeTextures(int guiWidth, ResourceLocation imageName1, ResourceLocation imageName2, ResourceLocation imageName3, Consumer<TreeSet<ResourceLocation>> setValueFunction, Supplier<TreeSet<ResourceLocation>> valueSupplier, VoidFunction applyValueFunction) {
			ImageSelectionWidget child1 = new ImageSelectionWidget(guiWidth / 2 - 75, 0, 50, 50, imageName1, setValueFunction, valueSupplier, applyValueFunction);
			ImageSelectionWidget child2 = new ImageSelectionWidget(guiWidth / 2 - 25, 0, 50, 50, imageName2, setValueFunction, valueSupplier, applyValueFunction);
			ImageSelectionWidget child3 = new ImageSelectionWidget(guiWidth / 2 + 25, 0, 50, 50, imageName3, setValueFunction, valueSupplier, applyValueFunction);
			return new TextureOptionsListEntry(child1, child2, child3);
		}

		public void updateValue() {
			this.children().forEach((child) -> child.updateValue());
		}

		@Override
		public List<ImageSelectionWidget> children() {
			if (child2 == null) {
				return ImmutableList.of(child1);
			} else if (child3 == null) {
				return ImmutableList.of(child1, child2);
			} else {
				return ImmutableList.of(child1, child2, child3);
			}
		}

		@Override
		public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
			if (child1 != null) {
				this.child1.y = top;
				this.child1.render(matrixStack, mouseX, mouseY, partialTicks);
			}
			if (child2 != null) {
				this.child2.y = top;
				this.child2.render(matrixStack, mouseX, mouseY, partialTicks);
			}
			if (child3 != null) {
				this.child3.y = top;
				this.child3.render(matrixStack, mouseX, mouseY, partialTicks);
			}
		}
	}
}
