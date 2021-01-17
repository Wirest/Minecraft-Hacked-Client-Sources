package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.lwjgl.Sys;

import net.minecraft.client.Mineman;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.optifine.Config;
import shadersmod.client.GuiSlotShaders;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersTex;

public class GuiShaders extends GuiScreen
{
    protected GuiScreen parentGui;
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private static final String ANTIALIASING = "Antialiasing: ";
    private static final String NORMAL_MAP = "Normap Map: ";
    private static final String SPECULAR_MAP = "Specular Map: ";
    private static final String RENDER_RES_MUL = "MotionBlur Quality: ";
    private static final String SHADOW_RES_MUL = "Shadow Quality: ";
    private static final String HAND_DEPTH = "Hand Depth: ";
    private static final String CLOUD_SHADOW = "Cloud Shadow: ";
    private static final String OLD_LIGHTING = "Classic Lighting: ";
    private static float[] QUALITY_MULTIPLIERS = new float[] {0.5F, 0.70710677F, 1.0F, 1.4142135F, 2.0F};
    private static String[] QUALITY_MULTIPLIER_NAMES = new String[] {"0.5x", "0.7x", "1x", "1.5x", "2x"};
    private static float[] HAND_DEPTH_VALUES = new float[] {0.0625F, 0.125F, 0.25F, 0.5F, 1.0F};
    private static String[] HAND_DEPTH_NAMES = new String[] {"0.5x", "1x", "2x", "4x", "8x"};
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.parentGui = par1GuiScreen;
    }

    private static String toStringOnOff(boolean value)
    {
        String on = "ON";
        String off = "OFF";
        return value ? on : off;
    }

    private static String toStringAa(int value)
    {
        return value == 2 ? "FXAA 2x" : (value == 4 ? "FXAA 4x" : "OFF");
    }

    private static String toStringValue(float val, float[] values, String[] names)
    {
        int index = getValueIndex(val, values);
        return names[index];
    }

    private static int getValueIndex(float val, float[] values)
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

    private static String toStringQuality(float val)
    {
        return toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
    }

    private static String toStringHandDepth(float val)
    {
        return toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (Shaders.shadersConfig == null)
        {
            Shaders.loadConfig();
        }

        byte baseY = 30;
        byte stepY = 20;
        int btnX = this.width - 130;
        byte btnWidth = 120;
        byte btnHeight = 20;
        int shaderListWidth = this.width - btnWidth - 20;
        this.shaderList = new GuiSlotShaders(this, shaderListWidth, this.height, baseY, this.height - 50, 16);
        this.shaderList.registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButton(20, btnX, 0 * stepY + baseY, btnWidth, btnHeight, "Antialiasing: " + toStringAa(Shaders.configAntialiasingLevel)));
        this.buttonList.add(new GuiButton(17, btnX, 1 * stepY + baseY, btnWidth, btnHeight, "Normap Map: " + toStringOnOff(Shaders.configNormalMap)));
        this.buttonList.add(new GuiButton(18, btnX, 2 * stepY + baseY, btnWidth, btnHeight, "Specular Map: " + toStringOnOff(Shaders.configSpecularMap)));
        this.buttonList.add(new GuiButton(15, btnX, 3 * stepY + baseY, btnWidth, btnHeight, "MotionBlur Quality: " + toStringQuality(Shaders.configRenderResMul)));
        this.buttonList.add(new GuiButton(16, btnX, 4 * stepY + baseY, btnWidth, btnHeight, "Shadow Quality: " + toStringQuality(Shaders.configShadowResMul)));
        this.buttonList.add(new GuiButton(10, btnX, 5 * stepY + baseY, btnWidth, btnHeight, "Hand Depth: " + toStringHandDepth(Shaders.configHandDepthMul)));
        this.buttonList.add(new GuiButton(9, btnX, 6 * stepY + baseY, btnWidth, btnHeight, "Cloud Shadow: " + toStringOnOff(Shaders.configCloudShadow)));
        this.buttonList.add(new GuiButton(19, btnX, 7 * stepY + baseY, btnWidth, btnHeight, "Classic Lighting: " + toStringOnOff(Shaders.configOldLighting)));
        this.buttonList.add(new GuiButton(6, btnX, this.height - 25, btnWidth, btnHeight, "Done"));
        short btnFolderWidth = 160;
        this.buttonList.add(new GuiButton(5, shaderListWidth / 2 - btnFolderWidth / 2, this.height - 25, btnFolderWidth, btnHeight, "Open shader packs folder"));
        this.updateButtons();
    }

    public void updateButtons()
    {
        boolean shaderActive = Config.isShaders();
        Iterator it = this.buttonList.iterator();

        while (it.hasNext())
        {
            GuiButton button = (GuiButton)it.next();

            if (button.id > 8 && button.id != 20)
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
            float var8;
            float[] confshader;
            String[] ex;
            int index;

            switch (button.id)
            {
                case 4:
                    Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                    button.displayString = "tweakBlockDamage: " + toStringOnOff(Shaders.configTweakBlockDamage);
                    break;

                case 5:
                    switch (getOSType())
                    {
                        case 1:
                            String var10 = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());

                            try
                            {
                                Runtime.getRuntime().exec(var10);
                                return;
                            }
                            catch (IOException var81)
                            {
                                var81.printStackTrace();
                                break;
                            }

                        case 2:
                            try
                            {
                                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                                return;
                            }
                            catch (IOException var9)
                            {
                                var9.printStackTrace();
                            }
                    }

                    boolean var11 = false;

                    try
                    {
                        Class var12 = Class.forName("java.awt.Desktop");
                        Object var13 = var12.getMethod("getDesktop", new Class[0]).invoke(null);
                        var12.getMethod("browse", new Class[] {URI.class}).invoke(var13, (new File(this.mc.mcDataDir, Shaders.shaderpacksdirname)).toURI());
                    }
                    catch (Throwable var7)
                    {
                        var7.printStackTrace();
                        var11 = true;
                    }

                    if (var11)
                    {
                        System.out.println("Opening via system class!");
                        Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
                    }

                    break;

                case 6:
                    new File(Shaders.shadersdir, "current.cfg");

                    try
                    {
                        Shaders.storeConfig();
                    }
                    catch (Exception var6)
                    {
                    }

                    this.mc.displayGuiScreen(this.parentGui);
                    break;

                case 7:
                case 8:
                default:
                    this.shaderList.actionPerformed(button);
                    break;

                case 9:
                    Shaders.configCloudShadow = !Shaders.configCloudShadow;
                    button.displayString = "Cloud Shadow: " + toStringOnOff(Shaders.configCloudShadow);
                    break;

                case 10:
                    var8 = Shaders.configHandDepthMul;
                    confshader = HAND_DEPTH_VALUES;
                    ex = HAND_DEPTH_NAMES;
                    index = getValueIndex(var8, confshader);

                    if (isShiftKeyDown())
                    {
                        --index;

                        if (index < 0)
                        {
                            index = confshader.length - 1;
                        }
                    }
                    else
                    {
                        ++index;

                        if (index >= confshader.length)
                        {
                            index = 0;
                        }
                    }

                    Shaders.configHandDepthMul = confshader[index];
                    button.displayString = "Hand Depth: " + ex[index];
                    break;

                case 11:
                    Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
                    Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
                    button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                    ShadersTex.updateTextureMinMagFilter();
                    break;

                case 12:
                    Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                    button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                    ShadersTex.updateTextureMinMagFilter();
                    break;

                case 13:
                    Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                    button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                    ShadersTex.updateTextureMinMagFilter();
                    break;

                case 14:
                    Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                    button.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
                    ShadersTex.updateTextureMinMagFilter();
                    break;

                case 15:
                    var8 = Shaders.configRenderResMul;
                    confshader = QUALITY_MULTIPLIERS;
                    ex = QUALITY_MULTIPLIER_NAMES;
                    index = getValueIndex(var8, confshader);

                    if (isShiftKeyDown())
                    {
                        --index;

                        if (index < 0)
                        {
                            index = confshader.length - 1;
                        }
                    }
                    else
                    {
                        ++index;

                        if (index >= confshader.length)
                        {
                            index = 0;
                        }
                    }

                    Shaders.configRenderResMul = confshader[index];
                    button.displayString = "MotionBlur Quality: " + ex[index];
                    Shaders.scheduleResize();
                    break;

                case 16:
                    var8 = Shaders.configShadowResMul;
                    confshader = QUALITY_MULTIPLIERS;
                    ex = QUALITY_MULTIPLIER_NAMES;
                    index = getValueIndex(var8, confshader);

                    if (isShiftKeyDown())
                    {
                        --index;

                        if (index < 0)
                        {
                            index = confshader.length - 1;
                        }
                    }
                    else
                    {
                        ++index;

                        if (index >= confshader.length)
                        {
                            index = 0;
                        }
                    }

                    Shaders.configShadowResMul = confshader[index];
                    button.displayString = "Shadow Quality: " + ex[index];
                    Shaders.scheduleResizeShadow();
                    break;

                case 17:
                    Shaders.configNormalMap = !Shaders.configNormalMap;
                    button.displayString = "Normap Map: " + toStringOnOff(Shaders.configNormalMap);
                    this.mc.func_175603_A();
                    break;

                case 18:
                    Shaders.configSpecularMap = !Shaders.configSpecularMap;
                    button.displayString = "Specular Map: " + toStringOnOff(Shaders.configSpecularMap);
                    this.mc.func_175603_A();
                    break;

                case 19:
                    Shaders.configOldLighting = !Shaders.configOldLighting;
                    button.displayString = "Classic Lighting: " + toStringOnOff(Shaders.configOldLighting);
                    Shaders.updateBlockLightLevel();
                    this.mc.func_175603_A();
                    break;

                case 20:
                    Shaders.nextAntialiasingLevel();
                    button.displayString = "Antialiasing: " + toStringAa(Shaders.configAntialiasingLevel);
                    Shaders.uninit();
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

        this.drawCenteredString(this.fontRendererObj, "Shaders ", this.width / 2, 15, 16777215);
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

    public Mineman getMc()
    {
        return this.mc;
    }

    public void drawCenteredString(String text, int x, int y, int color)
    {
        this.drawCenteredString(this.fontRendererObj, text, x, y, color);
    }

    public static int getOSType()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("win") ? 1 : (osName.contains("mac") ? 2 : (osName.contains("solaris") ? 3 : (osName.contains("sunos") ? 3 : (osName.contains("linux") ? 4 : (osName.contains("unix") ? 4 : 0)))));
    }
}
