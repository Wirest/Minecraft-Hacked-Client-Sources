package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;

import org.lwjgl.Sys;

public class GuiShaders extends GuiScreen
{
    protected GuiScreen parentGui;
    protected String screenTitle = "Shaders";
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private static float[] QUALITY_MULTIPLIERS = new float[] {0.5F, 0.70710677F, 1.0F, 1.4142135F, 2.0F};
    private static String[] QUALITY_MULTIPLIER_NAMES = new String[] {"0.5x", "0.7x", "1x", "1.5x", "2x"};
    private static float[] HAND_DEPTH_VALUES = new float[] {0.0625F, 0.125F, 0.25F};
    private static String[] HAND_DEPTH_NAMES = new String[] {"0.5x", "1x", "2x"};
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.parentGui = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);

        if (Shaders.shadersConfig == null)
        {
            Shaders.loadConfig();
        }

        byte btnWidth = 120;
        byte btnHeight = 20;
        int btnX = this.width - btnWidth - 10;
        byte baseY = 30;
        byte stepY = 20;
        int shaderListWidth = this.width - btnWidth - 20;
        this.shaderList = new GuiSlotShaders(this, shaderListWidth, this.height, baseY, this.height - 50, 16);
        this.shaderList.registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, btnX, 0 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, btnX, 1 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, btnX, 2 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, btnX, 3 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, btnX, 4 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, btnX, 5 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, btnX, 6 * stepY + baseY, btnWidth, btnHeight));
        int btnFolderWidth = Math.min(150, shaderListWidth / 2 - 10);
        this.buttonList.add(new GuiButton(201, shaderListWidth / 4 - btnFolderWidth / 2, this.height - 25, btnFolderWidth, btnHeight, Lang.get("of.options.shaders.shadersFolder")));
        this.buttonList.add(new GuiButton(202, shaderListWidth / 4 * 3 - btnFolderWidth / 2, this.height - 25, btnFolderWidth, btnHeight, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(203, btnX, this.height - 25, btnWidth, btnHeight, Lang.get("of.options.shaders.shaderOptions")));
        this.updateButtons();
    }

    public void updateButtons()
    {
        boolean shaderActive = Config.isShaders();
        Iterator it = this.buttonList.iterator();

        while (it.hasNext())
        {
            GuiButton button = (GuiButton)it.next();

            if (button.id != 201 && button.id != 202 && button.id != EnumShaderOption.ANTIALIASING.ordinal())
            {
                button.enabled = shaderActive;
            }
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.shaderList.func_178039_p();
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button instanceof GuiButtonEnumShaderOption)
            {
                GuiButtonEnumShaderOption var12 = (GuiButtonEnumShaderOption)button;
                String[] names;
                int index;
                float var13;
                float[] var15;

                switch (GuiShaders.NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[var12.getEnumShaderOption().ordinal()])
                {
                    case 1:
                        Shaders.nextAntialiasingLevel();
                        Shaders.uninit();
                        break;

                    case 2:
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        this.mc.func_175603_A();
                        break;

                    case 3:
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        this.mc.func_175603_A();
                        break;

                    case 4:
                        var13 = Shaders.configRenderResMul;
                        var15 = QUALITY_MULTIPLIERS;
                        names = QUALITY_MULTIPLIER_NAMES;
                        index = getValueIndex(var13, var15);

                        if (isShiftKeyDown())
                        {
                            --index;

                            if (index < 0)
                            {
                                index = var15.length - 1;
                            }
                        }
                        else
                        {
                            ++index;

                            if (index >= var15.length)
                            {
                                index = 0;
                            }
                        }

                        Shaders.configRenderResMul = var15[index];
                        Shaders.scheduleResize();
                        break;

                    case 5:
                        var13 = Shaders.configShadowResMul;
                        var15 = QUALITY_MULTIPLIERS;
                        names = QUALITY_MULTIPLIER_NAMES;
                        index = getValueIndex(var13, var15);

                        if (isShiftKeyDown())
                        {
                            --index;

                            if (index < 0)
                            {
                                index = var15.length - 1;
                            }
                        }
                        else
                        {
                            ++index;

                            if (index >= var15.length)
                            {
                                index = 0;
                            }
                        }

                        Shaders.configShadowResMul = var15[index];
                        Shaders.scheduleResizeShadow();
                        break;

                    case 6:
                        var13 = Shaders.configHandDepthMul;
                        var15 = HAND_DEPTH_VALUES;
                        names = HAND_DEPTH_NAMES;
                        index = getValueIndex(var13, var15);

                        if (isShiftKeyDown())
                        {
                            --index;

                            if (index < 0)
                            {
                                index = var15.length - 1;
                            }
                        }
                        else
                        {
                            ++index;

                            if (index >= var15.length)
                            {
                                index = 0;
                            }
                        }

                        Shaders.configHandDepthMul = var15[index];
                        break;

                    case 7:
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;

                    case 8:
                        Shaders.configOldLighting.nextValue();
                        Shaders.updateBlockLightLevel();
                        this.mc.func_175603_A();
                        break;

                    case 9:
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;

                    case 10:
                        Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
                        Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
                        button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case 11:
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case 12:
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case 13:
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                }

                var12.updateButtonText();
            }
            else
            {
                switch (button.id)
                {
                    case 201:
                        switch (getOSType())
                        {
                            case 1:
                                String gbeso = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {Shaders.shaderpacksdir.getAbsolutePath()});

                                try
                                {
                                    Runtime.getRuntime().exec(gbeso);
                                    return;
                                }
                                catch (IOException var9)
                                {
                                    var9.printStackTrace();
                                    break;
                                }

                            case 2:
                                try
                                {
                                    Runtime.getRuntime().exec(new String[] {"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                                    return;
                                }
                                catch (IOException var10)
                                {
                                    var10.printStackTrace();
                                }
                        }

                        boolean var11 = false;

                        try
                        {
                            Class val = Class.forName("java.awt.Desktop");
                            Object var14 = val.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                            val.getMethod("browse", new Class[] {URI.class}).invoke(var14, new Object[] {(new File(this.mc.mcDataDir, Shaders.shaderpacksdirname)).toURI()});
                        }
                        catch (Throwable var8)
                        {
                            var8.printStackTrace();
                            var11 = true;
                        }

                        if (var11)
                        {
                            Config.dbg("Opening via system class!");
                            Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
                        }

                        break;

                    case 202:
                        new File(Shaders.shadersdir, "current.cfg");

                        try
                        {
                            Shaders.storeConfig();
                        }
                        catch (Exception var7)
                        {
                            ;
                        }

                        this.mc.displayGuiScreen(this.parentGui);
                        break;

                    case 203:
                        GuiShaderOptions values = new GuiShaderOptions(this, Config.getGameSettings());
                        Config.getMinecraft().displayGuiScreen(values);
                        break;

                    default:
                        this.shaderList.actionPerformed(button);
                }
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);

        if (this.updateTimer <= 0)
        {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }

        this.drawCenteredString(this.fontRendererObj, this.screenTitle + " ", this.width / 2, 15, 16777215);
        String info = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        int infoWidth = this.fontRendererObj.getStringWidth(info);

        if (infoWidth < this.width - 5)
        {
            this.drawCenteredString(this.fontRendererObj, info, this.width / 2, this.height - 40, 8421504);
        }
        else
        {
            this.drawString(this.fontRendererObj, info, 5, this.height - 40, 8421504);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        --this.updateTimer;
    }

    public Minecraft getMc()
    {
        return this.mc;
    }

    public void drawCenteredString(String text, int x, int y, int color)
    {
        this.drawCenteredString(this.fontRendererObj, text, x, y, color);
    }

    public static String toStringOnOff(boolean value)
    {
        String on = Lang.getOn();
        String off = Lang.getOff();
        return value ? on : off;
    }

    public static String toStringAa(int value)
    {
        return value == 2 ? "FXAA 2x" : (value == 4 ? "FXAA 4x" : Lang.getOff());
    }

    public static String toStringValue(float val, float[] values, String[] names)
    {
        int index = getValueIndex(val, values);
        return names[index];
    }

    public static int getValueIndex(float val, float[] values)
    {
        for (int i = 0; i < values.length; ++i)
        {
            float value = values[i];

            if (value >= val)
            {
                return i;
            }
        }

        return values.length - 1;
    }

    public static String toStringQuality(float val)
    {
        return toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
    }

    public static String toStringHandDepth(float val)
    {
        return toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
    }

    public static int getOSType()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("win") ? 1 : (osName.contains("mac") ? 2 : (osName.contains("solaris") ? 3 : (osName.contains("sunos") ? 3 : (osName.contains("linux") ? 4 : (osName.contains("unix") ? 4 : 0)))));
    }

    static class NamelessClass1647571870
    {
        static final int[] $SwitchMap$shadersmod$client$EnumShaderOption = new int[EnumShaderOption.values().length];

        static
        {
            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
            }
            catch (NoSuchFieldError var13)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var12)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
            }
            catch (NoSuchFieldError var11)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.OLD_LIGHTING.ordinal()] = 8;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 9;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_B.ordinal()] = 10;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_N.ordinal()] = 11;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_S.ordinal()] = 12;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 13;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
