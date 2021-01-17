// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import java.io.File;
import java.net.URI;
import java.io.IOException;
import java.util.Iterator;
import optifine.Config;
import net.minecraft.client.gui.GuiButton;
import optifine.Lang;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;

public class GuiShaders extends GuiScreen
{
    protected GuiScreen parentGui;
    protected String screenTitle;
    private int updateTimer;
    private GuiSlotShaders shaderList;
    private boolean saved;
    private static float[] QUALITY_MULTIPLIERS;
    private static String[] QUALITY_MULTIPLIER_NAMES;
    private static float[] HAND_DEPTH_VALUES;
    private static String[] HAND_DEPTH_NAMES;
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;
    
    static {
        GuiShaders.QUALITY_MULTIPLIERS = new float[] { 0.5f, 0.70710677f, 1.0f, 1.4142135f, 2.0f };
        GuiShaders.QUALITY_MULTIPLIER_NAMES = new String[] { "0.5x", "0.7x", "1x", "1.5x", "2x" };
        GuiShaders.HAND_DEPTH_VALUES = new float[] { 0.0625f, 0.125f, 0.25f };
        GuiShaders.HAND_DEPTH_NAMES = new String[] { "0.5x", "1x", "2x" };
    }
    
    public GuiShaders(final GuiScreen par1GuiScreen, final GameSettings par2GameSettings) {
        this.screenTitle = "Shaders";
        this.updateTimer = -1;
        this.saved = false;
        this.parentGui = par1GuiScreen;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);
        if (Shaders.shadersConfig == null) {
            Shaders.loadConfig();
        }
        final int i = 120;
        final int j = 20;
        final int k = this.width - i - 10;
        final int l = 30;
        final int i2 = 20;
        final int j2 = this.width - i - 20;
        (this.shaderList = new GuiSlotShaders(this, j2, this.height, l, this.height - 50, 16)).registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i2 + l, i, j));
        final int k2 = Math.min(150, j2 / 2 - 10);
        this.buttonList.add(new GuiButton(201, j2 / 4 - k2 / 2, this.height - 25, k2, j, Lang.get("of.options.shaders.shadersFolder")));
        this.buttonList.add(new GuiButton(202, j2 / 4 * 3 - k2 / 2, this.height - 25, k2, j, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(203, k, this.height - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
        this.updateButtons();
    }
    
    public void updateButtons() {
        final boolean flag = Config.isShaders();
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton.id != 201 && guibutton.id != 202 && guibutton.id != EnumShaderOption.ANTIALIASING.ordinal()) {
                guibutton.enabled = flag;
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.shaderList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button instanceof GuiButtonEnumShaderOption) {
                final GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
                switch (guibuttonenumshaderoption.getEnumShaderOption()) {
                    case ANTIALIASING: {
                        Shaders.nextAntialiasingLevel();
                        Shaders.uninit();
                        break;
                    }
                    case NORMAL_MAP: {
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case SPECULAR_MAP: {
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case RENDER_RES_MUL: {
                        final float f2 = Shaders.configRenderResMul;
                        final float[] afloat2 = GuiShaders.QUALITY_MULTIPLIERS;
                        final String[] astring2 = GuiShaders.QUALITY_MULTIPLIER_NAMES;
                        int k = getValueIndex(f2, afloat2);
                        if (isShiftKeyDown()) {
                            if (--k < 0) {
                                k = afloat2.length - 1;
                            }
                        }
                        else if (++k >= afloat2.length) {
                            k = 0;
                        }
                        Shaders.configRenderResMul = afloat2[k];
                        Shaders.scheduleResize();
                        break;
                    }
                    case SHADOW_RES_MUL: {
                        final float f3 = Shaders.configShadowResMul;
                        final float[] afloat3 = GuiShaders.QUALITY_MULTIPLIERS;
                        final String[] astring3 = GuiShaders.QUALITY_MULTIPLIER_NAMES;
                        int j = getValueIndex(f3, afloat3);
                        if (isShiftKeyDown()) {
                            if (--j < 0) {
                                j = afloat3.length - 1;
                            }
                        }
                        else if (++j >= afloat3.length) {
                            j = 0;
                        }
                        Shaders.configShadowResMul = afloat3[j];
                        Shaders.scheduleResizeShadow();
                        break;
                    }
                    case HAND_DEPTH_MUL: {
                        final float f4 = Shaders.configHandDepthMul;
                        final float[] afloat4 = GuiShaders.HAND_DEPTH_VALUES;
                        final String[] astring4 = GuiShaders.HAND_DEPTH_NAMES;
                        int i = getValueIndex(f4, afloat4);
                        if (isShiftKeyDown()) {
                            if (--i < 0) {
                                i = afloat4.length - 1;
                            }
                        }
                        else if (++i >= afloat4.length) {
                            i = 0;
                        }
                        Shaders.configHandDepthMul = afloat4[i];
                        break;
                    }
                    case CLOUD_SHADOW: {
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;
                    }
                    case OLD_HAND_LIGHT: {
                        Shaders.configOldHandLight.nextValue();
                        break;
                    }
                    case OLD_LIGHTING: {
                        Shaders.configOldLighting.nextValue();
                        Shaders.updateBlockLightLevel();
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case TWEAK_BLOCK_DAMAGE: {
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;
                    }
                    case TEX_MIN_FIL_B: {
                        Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
                        Shaders.configTexMinFilN = (Shaders.configTexMinFilS = Shaders.configTexMinFilB);
                        button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_N: {
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_S: {
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case SHADOW_CLIP_FRUSTRUM: {
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                }
                guibuttonenumshaderoption.updateButtonText();
            }
            else {
                switch (button.id) {
                    case 201: {
                        switch (getOSType()) {
                            case 1: {
                                final String s = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());
                                try {
                                    Runtime.getRuntime().exec(s);
                                    return;
                                }
                                catch (IOException ioexception) {
                                    ioexception.printStackTrace();
                                    break;
                                }
                            }
                            case 2: {
                                try {
                                    Runtime.getRuntime().exec(new String[] { "/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath() });
                                    return;
                                }
                                catch (IOException ioexception2) {
                                    ioexception2.printStackTrace();
                                }
                                break;
                            }
                        }
                        boolean flag = false;
                        try {
                            final Class oclass = Class.forName("java.awt.Desktop");
                            final Object object = oclass.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
                            oclass.getMethod("browse", URI.class).invoke(object, new File(this.mc.mcDataDir, Shaders.shaderpacksdirname).toURI());
                        }
                        catch (Throwable throwable) {
                            throwable.printStackTrace();
                            flag = true;
                        }
                        if (flag) {
                            Config.dbg("Opening via system class!");
                            Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
                            break;
                        }
                        break;
                    }
                    case 202: {
                        new File(Shaders.shadersdir, "current.cfg");
                        Shaders.storeConfig();
                        this.saved = true;
                        this.mc.displayGuiScreen(this.parentGui);
                        break;
                    }
                    case 203: {
                        final GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, Config.getGameSettings());
                        Config.getMinecraft().displayGuiScreen(guishaderoptions);
                        break;
                    }
                    default: {
                        this.shaderList.actionPerformed(button);
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (!this.saved) {
            Shaders.storeConfig();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateTimer <= 0) {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }
        this.drawCenteredString(this.fontRendererObj, String.valueOf(this.screenTitle) + " ", this.width / 2, 15, 16777215);
        final String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        final int i = this.fontRendererObj.getStringWidth(s);
        if (i < this.width - 5) {
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, this.height - 40, 8421504);
        }
        else {
            this.drawString(this.fontRendererObj, s, 5, this.height - 40, 8421504);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        --this.updateTimer;
    }
    
    public Minecraft getMc() {
        return this.mc;
    }
    
    public void drawCenteredString(final String text, final int x, final int y, final int color) {
        this.drawCenteredString(this.fontRendererObj, text, x, y, color);
    }
    
    public static String toStringOnOff(final boolean value) {
        final String s = Lang.getOn();
        final String s2 = Lang.getOff();
        return value ? s : s2;
    }
    
    public static String toStringAa(final int value) {
        return (value == 2) ? "FXAA 2x" : ((value == 4) ? "FXAA 4x" : Lang.getOff());
    }
    
    public static String toStringValue(final float val, final float[] values, final String[] names) {
        final int i = getValueIndex(val, values);
        return names[i];
    }
    
    public static int getValueIndex(final float val, final float[] values) {
        for (int i = 0; i < values.length; ++i) {
            final float f = values[i];
            if (f >= val) {
                return i;
            }
        }
        return values.length - 1;
    }
    
    public static String toStringQuality(final float val) {
        return toStringValue(val, GuiShaders.QUALITY_MULTIPLIERS, GuiShaders.QUALITY_MULTIPLIER_NAMES);
    }
    
    public static String toStringHandDepth(final float val) {
        return toStringValue(val, GuiShaders.HAND_DEPTH_VALUES, GuiShaders.HAND_DEPTH_NAMES);
    }
    
    public static int getOSType() {
        final String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? 1 : (s.contains("mac") ? 2 : (s.contains("solaris") ? 3 : (s.contains("sunos") ? 3 : (s.contains("linux") ? 4 : (s.contains("unix") ? 4 : 0)))));
    }
}
