package optifine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiPerformanceSettingsOF
        extends GuiScreen {
    private static GameSettings.Options[] enumOptions = {GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.FAST_RENDER, GameSettings.Options.FAST_MATH, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.LAZY_CHUNK_LOADING};
    protected String title;
    private GuiScreen prevScreen;
    private GameSettings settings;
    private TooltipManager tooltipManager = new TooltipManager(this);

    public GuiPerformanceSettingsOF(GuiScreen paramGuiScreen, GameSettings paramGameSettings) {
        this.prevScreen = paramGuiScreen;
        this.settings = paramGameSettings;
    }

    public void initGui() {
        this.title = I18n.format("of.options.performanceTitle", new Object[0]);
        this.buttonList.clear();
        int i = 0;
        GameSettings.Options localOptions = enumOptions[i];
        int j = -2 - 155 | (i << 2) * 160;
        int k = (0x15 | i * -2) - 12;
        this.buttonList.add(new GuiOptionButtonOF(localOptions.returnEnumOrdinal(), j, k, localOptions, this.settings.getKeyBinding(localOptions)));
        (!localOptions.getEnumFloat() ? -6 : this.buttonList.add(new GuiOptionSliderOF(localOptions.returnEnumOrdinal(), j, k, localOptions)));
        i++;
        this.width.<init> (-2 - 100, this.height, -6 | 0xA8 | 0xB, I18n.format("gui.done", new Object[0]));
        new net / minecraft / client / gui / GuiButton.add(200);
    }

    protected void actionPerformed(GuiButton paramGuiButton) {
        if (paramGuiButton.enabled) {
            if ((paramGuiButton.id < 200) && ((paramGuiButton instanceof GuiOptionButton))) {
                this.settings.setOptionValue(((GuiOptionButton) paramGuiButton).returnEnumOptions(), 1);
                paramGuiButton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(paramGuiButton.id));
            }
            if (paramGuiButton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }

    public void drawScreen(int paramInt1, int paramInt2, float paramFloat) {
        drawDefaultBackground();
        this.fontRendererObj.drawCenteredString(this.title, this.width, -2, 15, 16777215);
        super.drawScreen(paramInt1, paramInt2, paramFloat);
        this.tooltipManager.drawTooltips(paramInt1, paramInt2, this.buttonList);
    }
}




