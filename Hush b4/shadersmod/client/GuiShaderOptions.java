// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.StrUtils;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import optifine.Lang;
import optifine.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import optifine.GuiScreenOF;

public class GuiShaderOptions extends GuiScreenOF
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    private String screenName;
    private String screenText;
    private boolean changed;
    public static final String OPTION_PROFILE = "<profile>";
    public static final String OPTION_EMPTY = "<empty>";
    public static final String OPTION_REST = "*";
    
    public GuiShaderOptions(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.screenName = null;
        this.screenText = null;
        this.changed = false;
        this.title = "Shader Options";
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    public GuiShaderOptions(final GuiScreen guiscreen, final GameSettings gamesettings, final String screenName) {
        this(guiscreen, gamesettings);
        this.screenName = screenName;
        if (screenName != null) {
            this.screenText = Shaders.translate("screen." + screenName, screenName);
        }
    }
    
    @Override
    public void initGui() {
        this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
        final int i = 100;
        int j = 0;
        final int k = 30;
        final int l = 20;
        final int i2 = this.width - 130;
        final int j2 = 120;
        final int k2 = 20;
        int l2 = 2;
        final ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);
        if (ashaderoption != null) {
            if (ashaderoption.length > 18) {
                l2 = ashaderoption.length / 9 + 1;
            }
            for (int i3 = 0; i3 < ashaderoption.length; ++i3) {
                final ShaderOption shaderoption = ashaderoption[i3];
                if (shaderoption != null && shaderoption.isVisible()) {
                    final int j3 = i3 % l2;
                    final int k3 = i3 / l2;
                    final int l3 = Math.min(this.width / l2, 200);
                    j = (this.width - l3 * l2) / 2;
                    final int i4 = j3 * l3 + 5 + j;
                    final int j4 = k + k3 * l;
                    final int k4 = l3 - 10;
                    final String s = this.getButtonText(shaderoption, k4);
                    final GuiButtonShaderOption guibuttonshaderoption = new GuiButtonShaderOption(i + i3, i4, j4, k4, k2, shaderoption, s);
                    guibuttonshaderoption.enabled = shaderoption.isEnabled();
                    this.buttonList.add(guibuttonshaderoption);
                }
            }
        }
        this.buttonList.add(new GuiButton(201, this.width / 2 - j2 - 20, this.height / 6 + 168 + 11, j2, k2, I18n.format("controls.reset", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, j2, k2, I18n.format("gui.done", new Object[0])));
    }
    
    private String getButtonText(final ShaderOption so, final int btnWidth) {
        String s = so.getNameText();
        if (so instanceof ShaderOptionScreen) {
            final ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
            return String.valueOf(s) + "...";
        }
        final FontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        for (int i = fontrenderer.getStringWidth(": " + Lang.getOff()) + 5; fontrenderer.getStringWidth(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1)) {}
        final String s2 = so.isChanged() ? so.getValueColor(so.getValue()) : "";
        final String s3 = so.getValueText(so.getValue());
        return String.valueOf(s) + ": " + s2 + s3;
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption) {
                final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
                final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
                if (shaderoption instanceof ShaderOptionScreen) {
                    final String s = shaderoption.getName();
                    final GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, this.settings, s);
                    this.mc.displayGuiScreen(guishaderoptions);
                    return;
                }
                if (isShiftKeyDown()) {
                    shaderoption.resetValue();
                }
                else {
                    shaderoption.nextValue();
                }
                this.updateAllButtons();
                this.changed = true;
            }
            if (guibutton.id == 201) {
                final ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());
                for (int i = 0; i < ashaderoption.length; ++i) {
                    final ShaderOption shaderoption2 = ashaderoption[i];
                    shaderoption2.resetValue();
                    this.changed = true;
                }
                this.updateAllButtons();
            }
            if (guibutton.id == 200) {
                if (this.changed) {
                    Shaders.saveShaderPackOptions();
                    this.changed = false;
                    Shaders.uninit();
                }
                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }
    
    @Override
    protected void actionPerformedRightClick(final GuiButton btn) {
        if (btn instanceof GuiButtonShaderOption) {
            final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
            final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
            if (isShiftKeyDown()) {
                shaderoption.resetValue();
            }
            else {
                shaderoption.prevValue();
            }
            this.updateAllButtons();
            this.changed = true;
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.changed) {
            Shaders.saveShaderPackOptions();
            this.changed = false;
            Shaders.uninit();
        }
    }
    
    private void updateAllButtons() {
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton instanceof GuiButtonShaderOption) {
                final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
                final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
                if (shaderoption instanceof ShaderOptionProfile) {
                    final ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
                    shaderoptionprofile.updateProfile();
                }
                guibuttonshaderoption.displayString = this.getButtonText(shaderoption, guibuttonshaderoption.getButtonWidth());
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        this.drawDefaultBackground();
        if (this.screenText != null) {
            this.drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
        }
        else {
            this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        }
        super.drawScreen(x, y, f);
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            this.drawTooltips(x, y, this.buttonList);
        }
        else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }
    
    private void drawTooltips(final int x, final int y, final List buttons) {
        final int i = 700;
        if (System.currentTimeMillis() >= this.mouseStillTime + i) {
            final int j = this.width / 2 - 150;
            int k = this.height / 6 - 7;
            if (y <= k + 98) {
                k += 105;
            }
            final int l = j + 150 + 150;
            final int i2 = k + 84 + 10;
            final GuiButton guibutton = GuiScreenOF.getSelectedButton(buttons, x, y);
            if (guibutton instanceof GuiButtonShaderOption) {
                final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
                final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
                final String[] astring = this.makeTooltipLines(shaderoption, l - j);
                if (astring == null) {
                    return;
                }
                this.drawGradientRect(j, k, l, i2, -536870912, -536870912);
                for (int j2 = 0; j2 < astring.length; ++j2) {
                    final String s = astring[j2];
                    int k2 = 14540253;
                    if (s.endsWith("!")) {
                        k2 = 16719904;
                    }
                    this.fontRendererObj.drawStringWithShadow(s, (float)(j + 5), (float)(k + 5 + j2 * 11), k2);
                }
            }
        }
    }
    
    private String[] makeTooltipLines(final ShaderOption so, final int width) {
        if (so instanceof ShaderOptionProfile) {
            return null;
        }
        final String s = so.getNameText();
        final String s2 = Config.normalize(so.getDescriptionText()).trim();
        final String[] astring = this.splitDescription(s2);
        String s3 = null;
        if (!s.equals(so.getName()) && this.settings.advancedItemTooltips) {
            s3 = "§8" + Lang.get("of.general.id") + ": " + so.getName();
        }
        String s4 = null;
        if (so.getPaths() != null && this.settings.advancedItemTooltips) {
            s4 = "§8" + Lang.get("of.general.from") + ": " + Config.arrayToString(so.getPaths());
        }
        String s5 = null;
        if (so.getValueDefault() != null && this.settings.advancedItemTooltips) {
            final String s6 = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get("of.general.ambiguous");
            s5 = "§8" + Lang.getDefault() + ": " + s6;
        }
        final List<String> list = new ArrayList<String>();
        list.add(s);
        list.addAll(Arrays.asList(astring));
        if (s3 != null) {
            list.add(s3);
        }
        if (s4 != null) {
            list.add(s4);
        }
        if (s5 != null) {
            list.add(s5);
        }
        final String[] astring2 = this.makeTooltipLines(width, list);
        return astring2;
    }
    
    private String[] splitDescription(String desc) {
        if (desc.length() <= 0) {
            return new String[0];
        }
        desc = StrUtils.removePrefix(desc, "//");
        final String[] astring = desc.split("\\. ");
        for (int i = 0; i < astring.length; ++i) {
            astring[i] = "- " + astring[i].trim();
            astring[i] = StrUtils.removeSuffix(astring[i], ".");
        }
        return astring;
    }
    
    private String[] makeTooltipLines(final int width, final List<String> args) {
        final FontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < args.size(); ++i) {
            final String s = args.get(i);
            if (s != null && s.length() > 0) {
                for (final Object s2 : fontrenderer.listFormattedStringToWidth(s, width)) {
                    list.add((String)s2);
                }
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
}
