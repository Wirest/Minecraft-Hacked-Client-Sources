package net.minecraft.client.gui;

import cedo.ui.screens.MainMenu;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.josephworks.GLSLSandbox.GLSLSandboxShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import net.optifine.reflect.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    /** Counts the number of screen updates. */
    private float updateCounter;

    /** The splash message. */
    private String splashText;

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private DynamicTexture viewportTexture;
    private boolean field_175375_v = true;

    /**
     * The Object object utilized as a thread lock when performing non thread-safe operations
     */
    private final Object threadLock = new Object();

    /** OpenGL graphics card warning. */
    private String openGLWarning1;

    /** OpenGL graphics card warning. */
    private String openGLWarning2;

    /** Link to the Mojang Support about minimum requirements */
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

    /** An array of all the paths to the panorama pictures. */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;

    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe

    private boolean field_183502_L;
    private GuiScreen field_183503_M;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;

    public GuiMainMenu()
    {
        this.openGLWarning2 = field_96138_a;
        this.field_183502_L = false;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            List<String> list = Lists.<String>newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }

            if (!list.isEmpty())
            {
                while (true)
                {
                    this.splashText = list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783)
                    {
                        break;
                    }
                }
            }
        }
        catch (IOException ignored)
        {
        }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException ignored)
                {
                }
            }
        }

        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
            this.openGLWarning1 = I18n.format("title.oldgl1");
            this.openGLWarning2 = I18n.format("title.oldgl2");
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    private boolean func_183501_a()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.panoramaTimer;

        if (this.func_183501_a())
        {
            this.field_183503_M.updateScreen();
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        mc.displayGuiScreen(new MainMenu());
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = 24;
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j, 24);

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));

        synchronized (this.threadLock)
        {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }

        this.mc.func_181537_a(false);

        if (this.func_183501_a())
        {
            this.field_183503_M.func_183500_a(this.width, this.height);
            this.field_183503_M.initGui();
        }

        initTime = System.currentTimeMillis();
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4)
        {
            this.mc.shutdown();
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result && id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13)
        {
            if (result)
            {
                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable)
                {
                    logger.error("Couldn\'t open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;
        int j = 64;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null)
        {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k)
        {
            GlStateManager.pushMatrix();
            float f = ((float)(k % i) / (float)i - 0.5F) / 64.0F;
            float f1 = ((float)(k / i) / (float)i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l)
            {
                GlStateManager.pushMatrix();

                if (l == 1)
                {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3)
                {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4)
                {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5)
                {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = titlePanoramaPaths;

                if (custompanoramaproperties != null)
                {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                int i1 = 255 / (k + 1);
                float f3 = 0.0F;
                worldrenderer.func_181662_b(-1.0D, -1.0D, 1.0D).func_181673_a(0.0D, 0.0D).func_181669_b(255, 255, 255, i1).func_181675_d();
                worldrenderer.func_181662_b(1.0D, -1.0D, 1.0D).func_181673_a(1.0D, 0.0D).func_181669_b(255, 255, 255, i1).func_181675_d();
                worldrenderer.func_181662_b(1.0D, 1.0D, 1.0D).func_181673_a(1.0D, 1.0D).func_181669_b(255, 255, 255, i1).func_181675_d();
                worldrenderer.func_181662_b(-1.0D, 1.0D, 1.0D).func_181673_a(0.0D, 1.0D).func_181669_b(255, 255, 255, i1).func_181675_d();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        GlStateManager.disableAlpha();
        int i = 3;
        int j = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null)
        {
            j = custompanoramaproperties.getBlur2();
        }

        for (int k = 0; k < j; ++k)
        {
            float f = 1.0F / (float)(k + 1);
            int l = this.width;
            int i1 = this.height;
            float f1 = (float)(k - i / 2) / 256.0F;
            worldrenderer.func_181662_b(l, i1, zLevel).func_181673_a(0.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).func_181675_d();
            worldrenderer.func_181662_b(l, 0.0D, zLevel).func_181673_a(1.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).func_181675_d();
            worldrenderer.func_181662_b(0.0D, 0.0D, zLevel).func_181673_a(1.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).func_181675_d();
            worldrenderer.func_181662_b(0.0D, i1, zLevel).func_181673_a(0.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).func_181675_d();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null)
        {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j)
        {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f2 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f = (float)this.height * f2 / 256.0F;
        float f1 = (float)this.width * f2 / 256.0F;
        int k = this.width;
        int l = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0D, l, zLevel).func_181673_a(0.5F - f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).func_181675_d();
        worldrenderer.func_181662_b(k, l, zLevel).func_181673_a(0.5F - f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).func_181675_d();
        worldrenderer.func_181662_b(k, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).func_181675_d();
        worldrenderer.func_181662_b(0.0D, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).func_181675_d();
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = 30;

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);

        // Stuff enabled done by the skybox rendering

        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double)this.updateCounter < 1.0E-4D)
        {
            this.drawTexturedModalRect(j, k, 0, 0, 99, 44);
            this.drawTexturedModalRect(j + 99, k, 129, 0, 27, 44);
            this.drawTexturedModalRect(j + 99 + 26, k, 126, 0, 3, 44);
            this.drawTexturedModalRect(j + 99 + 26 + 3, k, 99, 0, 26, 44);
            this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        }
        else
        {
            this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
            this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.8.9";

        this.drawString(s, 2, this.height - 10, -1);

        String s2 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(s2, this.width - this.fontRendererObj.getStringWidth(s2) - 2, this.height - 10, -1);

        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.openGLWarning2, (this.width - this.field_92024_r) / 2, this.buttonList.get(0).yPosition - 12, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.func_183501_a())
        {
            this.field_183503_M.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (this.modUpdateNotification != null)
        {
            this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.threadLock)
        {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }

        if (this.func_183501_a())
        {
            this.field_183503_M.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        if (this.field_183503_M != null)
        {
            this.field_183503_M.onGuiClosed();
        }
    }
}
