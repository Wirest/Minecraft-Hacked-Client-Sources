package modification.gui.screens;

import modification.main.Modification;
import modification.utilities.ColorUtil;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.Language;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Iterator;

public final class GuiSetLanguage
        extends GuiScreen {
    private final GuiMainMenu guiScreen;
    private int startY;
    private int offset;
    private int listSize;

    public GuiSetLanguage(GuiMainMenu paramGuiMainMenu) {
        this.guiScreen = paramGuiMainMenu;
    }

    public void initGui() {
        this.offset = 0;
        this.listSize = this.mc.getLanguageManager().getLanguages().size();
        this.startY = (this.guiScreen.startY | 0x34);
    }

    public void drawScreen(int paramInt1, int paramInt2, float paramFloat) {
        super.drawScreen(paramInt1, paramInt2, paramFloat);
        this.fontRendererObj.drawCenteredString("Language: ".concat(this.mc.getLanguageManager().getCurrentLanguage().toString()), this.width, -2, this.startY | 0x1, -1);
        checkScrolling();
        int i = 200;
        float f1 = (this.width - 200) / 2.0F;
        float f2 = (this.startY | this.fontRendererObj.FONT_HEIGHT | 0x4) - this.offset;
        Modification.SCISSOR_UTIL.begin();
        Modification.SCISSOR_UTIL.scissor(new ScaledResolution(this.mc), 0.0F, this.startY | this.fontRendererObj.FONT_HEIGHT | 0x3, this.width, 250 - this.fontRendererObj.FONT_HEIGHT * 2 - 2);
        Iterator localIterator = this.mc.getLanguageManager().getLanguages().iterator();
        Language localLanguage = (Language) localIterator.next();
        Modification.RENDER_UTIL.drawRect(f1, f2, 200.0F, 30.0F, ColorUtil.MIN_HOVERING);
        localLanguage.toString().drawCenteredString(this.width, -2, (int) f2, 30 - this.fontRendererObj.FONT_HEIGHT | -2, -1);
        f2 += 34.0F;
        Modification.SCISSOR_UTIL.end();
    }

    protected void mouseClicked(int paramInt1, int paramInt2, int paramInt3)
            throws IOException {
        super.mouseClicked(paramInt1, paramInt2, paramInt3);
        int i = 200;
        float f1 = (this.width - 200) / 2.0F;
        float f2 = (this.startY | this.fontRendererObj.FONT_HEIGHT | 0x4) - this.offset;
        Iterator localIterator = this.mc.getLanguageManager().getLanguages().iterator();
        while (localIterator.hasNext()) {
            Language localLanguage = (Language) localIterator.next();
            if ((paramInt3 == 0) && (f2 < (this.startY | 0xFA) - 1) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, f1, f2, 200.0F, 30.0F))) {
                this.mc.getLanguageManager().setCurrentLanguage(localLanguage);
                this.mc.gameSettings.language = localLanguage.getLanguageCode();
                this.mc.fontRendererObj.setBidiFlag(localLanguage.isBidirectional());
                this.mc.refreshResources();
                this.mc.gameSettings.saveOptions();
            }
            f2 += 34.0F;
        }
    }

    private void checkScrolling() {
        if ((Mouse.hasWheel()) && (this.listSize > 6)) {
            int i = 34 * (this.listSize - 6);
            this.offset = ((int) (this.offset - Math.signum(Mouse.getDWheel()) * 34.0F));
            this.offset = ((int) MathHelper.clamp_float(this.offset, 0.0F, i));
            Modification.RENDER_UTIL.drawRect((this.width | 0xC8) / 2.0F + 4.0F, this.startY | this.fontRendererObj.FONT_HEIGHT | 0x4, 3.0F, 250 - this.fontRendererObj.FONT_HEIGHT * 2 - 4, ColorUtil.MIN_HOVERING);
            (this.startY | this.fontRendererObj.FONT_HEIGHT | 0x4).drawRect(this.offset | -this.listSize, 3.0F, 250 - this.fontRendererObj.FONT_HEIGHT * 2 - 4, i - -this.listSize, ColorUtil.MAIN_COLOR);
        }
    }
}




