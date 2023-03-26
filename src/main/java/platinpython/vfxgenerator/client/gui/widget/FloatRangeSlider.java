package platinpython.vfxgenerator.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.ScreenUtils;
import org.lwjgl.glfw.GLFW;
import platinpython.vfxgenerator.util.Util;

import java.text.DecimalFormat;

public class FloatRangeSlider extends UpdateableWidget {
    private final double minValue, maxValue;
    private final float stepSize;
    private final DecimalFormat format;
    private final Component prefix;
    private final Component suffix;
    private final Util.FloatConsumer setLeftValueFunction;
    private final Util.FloatConsumer setRightValueFunction;
    private final Util.FloatSupplier leftValueSupplier;
    private final Util.FloatSupplier rightValueSupplier;
    private double leftSliderValue, rightSliderValue;
    private boolean isLeftSelected;
    private boolean stopped;

    public FloatRangeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue,
                            double maxValue, float stepSize, Util.FloatConsumer setLeftValueFunction,
                            Util.FloatConsumer setRightValueFunction, Util.FloatSupplier leftValueSupplier,
                            Util.FloatSupplier rightValueSupplier, Runnable applyValueFunction) {
        super(x, y, width, height, applyValueFunction);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = stepSize;
        this.format = Float.toString(this.stepSize).split("\\.")[1].length() == 1 && Float.toString(this.stepSize)
                                                                                          .split("\\.")[1].equals("0") ?
                      new DecimalFormat("0") :
                      new DecimalFormat(Float.toString(this.stepSize).replaceAll("\\d", "0"));
        this.prefix = prefix;
        this.suffix = suffix;
        this.setLeftValueFunction = setLeftValueFunction;
        this.setRightValueFunction = setRightValueFunction;
        this.leftValueSupplier = leftValueSupplier;
        this.rightValueSupplier = rightValueSupplier;
        this.setupSliderValues(this.leftValueSupplier.get(), this.rightValueSupplier.get());
    }

    private void setupSliderValues(double leftValue, double rightValue) {
        this.leftSliderValue = Util.clamp(leftValue, this.minValue, this.maxValue, this.stepSize);
        this.rightSliderValue = Util.clamp(rightValue, this.minValue, this.maxValue, this.stepSize);
        this.leftSliderValue = Util.toValue(Mth.clamp(this.leftSliderValue, 0D, this.rightSliderValue), this.minValue,
                                            this.maxValue, this.stepSize
        );
        this.rightSliderValue = Util.toValue(Mth.clamp(this.rightSliderValue, this.leftSliderValue, 1D), this.minValue,
                                             this.maxValue, this.stepSize
        );
        this.setLeftValueFunction.accept((float) this.getLeftSliderValue());
        this.setRightValueFunction.accept((float) this.getRightSliderValue());
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return 0;
    }

    private int getYImageNoDisabled(boolean isHovered) {
        if (!this.active) {
            return 1;
        }
        return isHovered ? 2 : 1;
    }

    public boolean isLeftHovered(int mouseX) {
        return this.isHovered && mouseX < (this.x + ((this.rightSliderValue + this.leftSliderValue) / 2) * this.width);
    }

    public boolean isRightHovered(int mouseX) {
        return this.isHovered && mouseX > (this.x + ((this.rightSliderValue + this.leftSliderValue) / 2) * this.width);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, Minecraft minecraft, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ScreenUtils.blitWithBorder(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 4,
                                   this.y + 3, 0, 66,
                                   ((int) (this.rightSliderValue * (double) (this.width - 8))) - ((int) (this.leftSliderValue * (double) (this.width - 8))),
                                   this.height - 6, 200, 20, 2, 2, 2, 2, this.getBlitOffset()
        );
        if (isLeftHovered(mouseX)) {
            renderRightBg(matrixStack, mouseX);
            renderLeftBg(matrixStack, mouseX);
        } else {
            renderLeftBg(matrixStack, mouseX);
            renderRightBg(matrixStack, mouseX);
        }
    }

    private void renderRightBg(PoseStack matrixStack, int mouseX) {
        this.blit(matrixStack, this.x + (int) (this.rightSliderValue * (double) (this.width - 8)), this.y, 0,
                  46 + this.getYImageNoDisabled(isRightHovered(mouseX)) * 20, 4, this.height
        );
        this.blit(matrixStack, this.x + (int) (this.rightSliderValue * (double) (this.width - 8)) + 4, this.y, 196,
                  46 + this.getYImageNoDisabled(isRightHovered(mouseX)) * 20, 4, this.height
        );
    }

    private void renderLeftBg(PoseStack matrixStack, int mouseX) {
        this.blit(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)), this.y, 0,
                  46 + this.getYImageNoDisabled(isLeftHovered(mouseX)) * 20, 4, this.height
        );
        this.blit(matrixStack, this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 4, this.y, 196,
                  46 + this.getYImageNoDisabled(isLeftHovered(mouseX)) * 20, 4, this.height
        );
    }

    private boolean getIsLeftClicked(double mouseX) {
        return (mouseX < this.x + (int) (this.leftSliderValue * (double) (this.width - 8)) + 8 || mouseX < ((this.x + this.leftSliderValue * this.width) + (this.x + this.rightSliderValue * this.width)) / 2);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isLeftSelected = getIsLeftClicked(mouseX);
        this.stopped = false;
        if (this.isLeftSelected) {
            this.setLeftSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
        } else {
            this.setRightSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        if (this.leftSliderValue == this.rightSliderValue && !stopped) {
            this.isLeftSelected = dragX < 0;
        }
        if (this.isLeftSelected) {
            this.setLeftSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
        } else {
            this.setRightSliderValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
        }
        this.stopped = this.leftSliderValue == this.rightSliderValue;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = keyCode == GLFW.GLFW_KEY_LEFT;
        if (flag || keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (flag) {
                if (this.leftSliderValue != 0D) {
                    this.setLeftSliderValue(
                            Util.clamp((this.getLeftSliderValue() - this.stepSize), this.minValue, this.maxValue,
                                       this.stepSize
                            ));
                    this.setRightSliderValue(
                            Util.clamp((this.getRightSliderValue() - this.stepSize), this.minValue, this.maxValue,
                                       this.stepSize
                            ));
                }
            } else {
                if (this.rightSliderValue != 1D) {
                    this.setRightSliderValue(
                            Util.clamp((this.getRightSliderValue() + this.stepSize), this.minValue, this.maxValue,
                                       this.stepSize
                            ));
                    this.setLeftSliderValue(
                            Util.clamp((this.getLeftSliderValue() + this.stepSize), this.minValue, this.maxValue,
                                       this.stepSize
                            ));
                }
            }
        }
        return false;
    }

    @Override
    public void updateValue() {
        if (this.leftValueSupplier.get() != this.getLeftSliderValue()) {
            this.leftSliderValue = Util.clamp(this.leftValueSupplier.get(), this.minValue, this.maxValue,
                                              this.stepSize
            );
        }
        if (this.rightValueSupplier.get() != this.getRightSliderValue()) {
            this.rightSliderValue = Util.clamp(this.rightValueSupplier.get(), this.minValue, this.maxValue,
                                               this.stepSize
            );
        }
        this.updateMessage();
    }

    private double getLeftSliderValue() {
        return this.leftSliderValue * (this.maxValue - this.minValue) + this.minValue;
    }

    private void setLeftSliderValue(double value) {
        double d0 = this.leftSliderValue;
        this.leftSliderValue = Util.toValue(Mth.clamp(value, 0.0D, this.rightSliderValue), this.minValue, this.maxValue,
                                            this.stepSize
        );
        if (d0 != this.leftSliderValue) {
            this.setLeftValueFunction.accept((float) this.getLeftSliderValue());
            this.applyValue();
        }

        this.updateMessage();
    }

    private double getRightSliderValue() {
        return this.rightSliderValue * (this.maxValue - this.minValue) + this.minValue;
    }

    private void setRightSliderValue(double value) {
        double d0 = this.rightSliderValue;
        this.rightSliderValue = Util.toValue(Mth.clamp(value, this.leftSliderValue, 1.0D), this.minValue, this.maxValue,
                                             this.stepSize
        );
        if (d0 != this.rightSliderValue) {
            this.setRightValueFunction.accept((float) this.getRightSliderValue());
            this.applyValue();
        }

        this.updateMessage();
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.empty()
                            .append(prefix)
                            .append(": ")
                            .append(format.format(getLeftSliderValue()))
                            .append(suffix.getString().isEmpty() || suffix.getString().equals("°") || suffix.getString()
                                                                                                            .equals("%") ?
                                    "" :
                                    " ")
                            .append(suffix)
                            .append(" - ")
                            .append(format.format(getRightSliderValue()))
                            .append(suffix.getString().isEmpty() || suffix.getString().equals("°") || suffix.getString()
                                                                                                            .equals("%") ?
                                    "" :
                                    " ")
                            .append(suffix));
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", this.getMessage());
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE,
                                           Component.translatable("narration.slider.usage.focused")
                );
            } else {
                narrationElementOutput.add(NarratedElementType.USAGE,
                                           Component.translatable("narration.slider.usage.hovered")
                );
            }
        }
    }
}
