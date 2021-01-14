package modification.gui;

import modification.main.Modification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public final class GuiPictureButton
        extends GuiButton {
    private static final FontRenderer MINI_RENDERER = new FontRenderer("Arial", 1, 12, false);
    private final ResourceLocation location;
    private final int offsetX;
    private final int offsetY;
    private final int sizeX;
    private final int sizeY;
    private final int renderSize;

    public GuiPictureButton(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, String paramString1, String paramString2) {
        super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString1);
        this.offsetX = paramInt6;
        this.offsetY = paramInt7;
        this.sizeX = paramInt8;
        this.sizeY = paramInt9;
        this.renderSize = paramInt10;
        this.location = new ResourceLocation("modification/".concat("sprite_").concat(paramString2).concat(".png"));
    }

    public void drawButton(Minecraft paramMinecraft, int paramInt1, int paramInt2) {
        this.hovered = Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.xPosition, this.yPosition, this.width, this.height);
        this.location.drawSprite(this.xPosition, this.yPosition, this.height - this.renderSize | -2, this.offsetX, this.offsetY, this.sizeX, this.sizeY, this.renderSize, this.enabled ? Color.WHITE.getRGB() : Color.DARK_GRAY.getRGB());
        if (this.hovered) {
            Modification.RENDER_UTIL.drawRect(paramInt1 - MINI_RENDERER.getStringWidth(this.displayString) / 2.0F, paramInt2 - 5, MINI_RENDERER.getStringWidth(this.displayString) | 0x2, MINI_RENDERER.FONT_HEIGHT | 0x2, Color.BLACK.getRGB());
            MINI_RENDERER.drawString(this.displayString, paramInt1, MINI_RENDERER.getStringWidth(this.displayString) - -2 | 0x1, paramInt2 - 3, this.enabled ? -1 : Color.DARK_GRAY.getRGB());
        }
    }

    public void playPressSound(SoundHandler paramSoundHandler) {
    }
}




