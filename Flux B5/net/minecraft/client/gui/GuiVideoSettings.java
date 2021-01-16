package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.GuiAnimationSettingsOF;
import optifine.GuiDetailSettingsOF;
import optifine.GuiOptionButtonOF;
import optifine.GuiOptionSliderOF;
import optifine.GuiOtherSettingsOF;
import optifine.GuiPerformanceSettingsOF;
import optifine.GuiQualitySettingsOF;
import optifine.Lang;
import optifine.TooltipManager;
import shadersmod.client.GuiShaders;

public class GuiVideoSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;

    /** An array of all of GameSettings.Options's video options. */
    private static GameSettings.Options[] videoOptions = new GameSettings.Options[] {GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START};
    private static final String __OBFID = "CL_00000718";
    private TooltipManager tooltipManager = new TooltipManager(this);

    public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        int y;

        for (y = 0; y < videoOptions.length; ++y)
        {
            GameSettings.Options x = videoOptions[y];

            if (x != null)
            {
                int x1 = this.width / 2 - 155 + y % 2 * 160;
                int y1 = this.height / 6 + 21 * (y / 2) - 12;

                if (x.getEnumFloat())
                {
                    this.buttonList.add(new GuiOptionSliderOF(x.returnEnumOrdinal(), x1, y1, x));
                }
                else
                {
                    this.buttonList.add(new GuiOptionButtonOF(x.returnEnumOrdinal(), x1, y1, x, this.guiGameSettings.getKeyBinding(x)));
                }
            }
        }

        y = this.height / 6 + 21 * (videoOptions.length / 2) - 12;
        boolean var5 = false;
        int var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(231, var6, y, Lang.get("of.options.shaders")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, var6, y, Lang.get("of.options.quality")));
        y += 21;
        var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(201, var6, y, Lang.get("of.options.details")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, var6, y, Lang.get("of.options.performance")));
        y += 21;
        var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(211, var6, y, Lang.get("of.options.animations")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, var6, y, Lang.get("of.options.other")));
        y += 21;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            int guiScale = this.guiGameSettings.guiScale;

            if (button.id < 200 && button instanceof GuiOptionButton)
            {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 200)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }

            if (this.guiGameSettings.guiScale != guiScale)
            {
                ScaledResolution scr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int var4 = scr.getScaledWidth();
                int var5 = scr.getScaledHeight();
                this.setWorldAndResolution(this.mc, var4, var5);
            }

            if (button.id == 201)
            {
                this.mc.gameSettings.saveOptions();
                GuiDetailSettingsOF scr1 = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr1);
            }

            if (button.id == 202)
            {
                this.mc.gameSettings.saveOptions();
                GuiQualitySettingsOF scr2 = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr2);
            }

            if (button.id == 211)
            {
                this.mc.gameSettings.saveOptions();
                GuiAnimationSettingsOF scr3 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr3);
            }

            if (button.id == 212)
            {
                this.mc.gameSettings.saveOptions();
                GuiPerformanceSettingsOF scr4 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr4);
            }

            if (button.id == 222)
            {
                this.mc.gameSettings.saveOptions();
                GuiOtherSettingsOF scr5 = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr5);
            }

            if (button.id == 231)
            {
                if (Config.isAntialiasing() || Config.isAntialiasingConfigured())
                {
                    Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
                    return;
                }

                if (Config.isAnisotropicFiltering())
                {
                    Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
                    return;
                }

                if (Config.isFastRender())
                {
                    Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
                    return;
                }

                this.mc.gameSettings.saveOptions();
                GuiShaders scr6 = new GuiShaders(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr6);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int x, int y, float z)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
        String ver = Config.getVersion();
        String ed = "HD_U";

        if (ed.equals("HD"))
        {
            ver = "OptiFine HD H6";
        }

        if (ed.equals("HD_U"))
        {
            ver = "OptiFine HD H6 Ultra";
        }

        if (ed.equals("L"))
        {
            ver = "OptiFine H6 Light";
        }

        this.drawString(this.fontRendererObj, ver, 2, this.height - 10, 8421504);
        String verMc = "Minecraft 1.8";
        int lenMc = this.fontRendererObj.getStringWidth(verMc);
        this.drawString(this.fontRendererObj, verMc, this.width - lenMc - 2, this.height - 10, 8421504);
        super.drawScreen(x, y, z);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }

    public static int getButtonWidth(GuiButton btn)
    {
        return btn.width;
    }

    public static int getButtonHeight(GuiButton btn)
    {
        return btn.height;
    }

    public static void drawGradientRect(GuiScreen guiScreen, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        guiScreen.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }
}
