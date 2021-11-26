package platinpython.vfxgenerator.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import platinpython.vfxgenerator.client.gui.widget.TextureOptionsList;

public class ParticleTextureSelectionScreen extends Screen {
    private final ParticleOptionsScreen parent;

    private TextureOptionsList textureOptionsList;

    protected ParticleTextureSelectionScreen(ParticleOptionsScreen parent) {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.textureOptionsList = new TextureOptionsList(this.minecraft,
                                                         this.width,
                                                         this.height,
                                                         32,
                                                         this.height - 32,
                                                         50,
                                                         this.parent.particleData::setSelected,
                                                         this.parent.particleData::getSelected,
                                                         this.parent::sendToServer);

        this.addWidget(this.textureOptionsList);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.textureOptionsList.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        if (minecraft.player.distanceToSqr(Vector3d.atCenterOf(this.parent.tileEntity.getBlockPos())) > 64.0D) {
            this.parent.onClose();
        }
        this.textureOptionsList.children().forEach(TextureOptionsList.TextureOptionsListEntry::updateValue);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
