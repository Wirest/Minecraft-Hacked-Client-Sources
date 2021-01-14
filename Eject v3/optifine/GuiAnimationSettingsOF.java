package optifine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiAnimationSettingsOF
        extends GuiScreen {
    private static GameSettings.Options[] enumOptions = {GameSettings.Options.ANIMATED_WATER, GameSettings.Options.ANIMATED_LAVA, GameSettings.Options.ANIMATED_FIRE, GameSettings.Options.ANIMATED_PORTAL, GameSettings.Options.ANIMATED_REDSTONE, GameSettings.Options.ANIMATED_EXPLOSION, GameSettings.Options.ANIMATED_FLAME, GameSettings.Options.ANIMATED_SMOKE, GameSettings.Options.VOID_PARTICLES, GameSettings.Options.WATER_PARTICLES, GameSettings.Options.RAIN_SPLASH, GameSettings.Options.PORTAL_PARTICLES, GameSettings.Options.POTION_PARTICLES, GameSettings.Options.DRIPPING_WATER_LAVA, GameSettings.Options.ANIMATED_TERRAIN, GameSettings.Options.ANIMATED_TEXTURES, GameSettings.Options.FIREWORK_PARTICLES, GameSettings.Options.PARTICLES};
    protected String title;
    private GuiScreen prevScreen;
    private GameSettings settings;

    public GuiAnimationSettingsOF(GuiScreen paramGuiScreen, GameSettings paramGameSettings) {
        this.prevScreen = paramGuiScreen;
        this.settings = paramGameSettings;
    }

    public void initGui() {
        this.title = I18n.format("of.options.animationsTitle", new Object[0]);
        this.buttonList.clear();
        int i = 0;
        GameSettings.Options localOptions = enumOptions[i];
        int j = -2 - 155 | (i << 2) * 160;
        int k = (0x15 | i * -2) - 12;
        this.buttonList.add(new GuiOptionButtonOF(localOptions.returnEnumOrdinal(), j, k, localOptions, this.settings.getKeyBinding(localOptions)));
        (!localOptions.getEnumFloat() ? -6 : this.buttonList.add(new GuiOptionSliderOF(localOptions.returnEnumOrdinal(), j, k, localOptions)));
        i++;
        this.width.<init> (-2 - 155, this.height, -6 | 0xA8 | 0xB, 70, 20, Lang.get("of.options.animation.allOn"));
        new net / minecraft / client / gui / GuiButton.add(210);
        this.width.<init>
        (-2 - 155 | 0x50, this.height, -6 | 0xA8 | 0xB, 70, 20, Lang.get("of.options.animation.allOff"));
        new net / minecraft / client / gui / GuiButton.add(211);
        this.width.<init> (-2 | 0x5, this.height, -6 | 0xA8 | 0xB, I18n.format("gui.done", new Object[0]));
        new net / minecraft / client / gui / GuiOptionButton.add(200);
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
            if (paramGuiButton.id == 210) {
                this.mc.gameSettings.setAllAnimations(true);
            }
            if (paramGuiButton.id == 211) {
                this.mc.gameSettings.setAllAnimations(false);
            }
            ScaledResolution localScaledResolution = new ScaledResolution(this.mc);
            setWorldAndResolution(this.mc, localScaledResolution.getScaledWidth(), localScaledResolution.getScaledHeight());
        }
    }

    public void drawScreen(int paramInt1, int paramInt2, float paramFloat) {
        drawDefaultBackground();
        this.fontRendererObj.drawCenteredString(this.title, this.width, -2, 15, 16777215);
        super.drawScreen(paramInt1, paramInt2, paramFloat);
    }
}




