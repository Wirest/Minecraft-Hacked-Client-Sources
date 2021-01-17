package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import me.rigamortis.faurax.login.alts.GuiLogin;
import me.rigamortis.faurax.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random field_175374_h = new Random();
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object field_104025_t;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + (Object)((Object)EnumChatFormatting.UNDERLINE) + "here" + (Object)((Object)EnumChatFormatting.RESET) + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;
    private static final String __OBFID = "CL_00001154";
    public static String ipAddress;
    public int updateTime;
    public static ArrayList<String> changelogs;

    static {
        changelogs = new ArrayList();
    }

    public GuiMainMenu() {
        block18 : {
            BufferedReader var1;
            this.field_175375_v = true;
            this.field_104025_t = new Object();
            this.field_146972_A = field_96138_a;
            this.splashText = "missingno";
            var1 = null;
            try {
                try {
                    String var3;
                    ArrayList var2 = Lists.newArrayList();
                    var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
                    while ((var3 = var1.readLine()) != null) {
                        if ((var3 = var3.trim()).isEmpty()) continue;
                        var2.add(var3);
                    }
                    if (!var2.isEmpty()) {
                        do {
                            this.splashText = (String)var2.get(field_175374_h.nextInt(var2.size()));
                        } while (this.splashText.hashCode() == 125780783);
                    }
                }
                catch (IOException var2) {
                    break block18;
                }
            }
            catch (Throwable var4_8) {
                if (var1 != null) {
                    try {
                        var1.close();
                    }
                    catch (IOException var5_6) {
                        // empty catch block
                    }
                }
                throw var4_8;
            }
            if (var1 != null) {
                try {
                    var1.close();
                }
                catch (IOException var5_7) {
                    // empty catch block
                }
            }
        }
        this.updateCounter = field_175374_h.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());
        if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        boolean var2 = true;
        int var3 = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(var3, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(var3, 24);
        }
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0]));
        this.buttonList.add(this.buttonResetDemo);
        ISaveFormat var3 = this.mc.getSaveLoader();
        WorldInfo var4 = var3.getWorldInfo("Demo_World");
        if (var4 == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        WorldInfo var3;
        ISaveFormat var2;
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 1337) {
            this.mc.displayGuiScreen(new GuiLogin(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 14 && this.field_175372_K.visible) {
            this.switchToRealms();
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12 && (var3 = (var2 = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
            this.mc.displayGuiScreen(var4);
        }
    }

    private void switchToRealms() {
        RealmsBridge var1 = new RealmsBridge();
        var1.switchToRealms(this);
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class var3 = Class.forName("java.awt.Desktop");
                    Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    var3.getMethod("browse", URI.class).invoke(var4, new URI(this.field_104024_v));
                }
                catch (Throwable var5) {
                    logger.error("Couldn't open link", var5);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int var6 = 8;
        int var7 = 0;
        while (var7 < var6 * var6) {
            GlStateManager.pushMatrix();
            float var8 = ((float)(var7 % var6) / (float)var6 - 0.5f) / 64.0f;
            float var9 = ((float)(var7 / var6) / (float)var6 - 0.5f) / 64.0f;
            float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((- (float)this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            int var11 = 0;
            while (var11 < 6) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.func_178974_a(16777215, 255 / (var7 + 1));
                float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                var4.draw();
                GlStateManager.popMatrix();
                ++var11;
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
            ++var7;
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        int var4 = 3;
        int var5 = 0;
        while (var5 < var4) {
            var3.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f / (float)(var5 + 1));
            int var6 = this.width;
            int var7 = this.height;
            float var8 = (float)(var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV(var6, var7, zLevel, 0.0f + var8, 1.0);
            var3.addVertexWithUV(var6, 0.0, zLevel, 1.0f + var8, 1.0);
            var3.addVertexWithUV(0.0, 0.0, zLevel, 1.0f + var8, 0.0);
            var3.addVertexWithUV(0.0, var7, zLevel, 0.0f + var8, 0.0);
            ++var5;
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        float var6 = this.width > this.height ? 120.0f / (float)this.width : 120.0f / (float)this.height;
        float var7 = (float)this.height * var6 / 256.0f;
        float var8 = (float)this.width * var6 / 256.0f;
        var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
        int var9 = this.width;
        int var10 = this.height;
        var5.addVertexWithUV(0.0, var10, zLevel, 0.5f - var7, 0.5f + var8);
        var5.addVertexWithUV(var9, var10, zLevel, 0.5f - var7, 0.5f - var8);
        var5.addVertexWithUV(var9, 0.0, zLevel, 0.5f + var7, 0.5f - var8);
        var5.addVertexWithUV(0.0, 0.0, zLevel, 0.5f + var7, 0.5f + var8);
        var4.draw();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        int var6 = 274;
        int var7 = this.width / 2 - var6 / 2;
        int var8 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float var9 = 1.8f - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000) / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
        var9 = var9 * 100.0f / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
        String var10 = "Minecraft 1.8";
        if (this.mc.isDemo()) {
            var10 = String.valueOf(var10) + " Demo";
        }
        String var11 = "Copyright Mojang AB. Do not distribute!";
        if (this.field_92025_p != null) {
            this.field_92025_p.length();
        }
        ScaledResolution var2 = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int w = var2.getScaledWidth();
        int h = var2.getScaledHeight();
        ++this.updateTime;
        if (this.updateTime >= 50) {
            new Thread(){

                @Override
                public void run() {
                    try {
                        String ip;
                        URL whatismyip = new URL("http://checkip.amazonaws.com");
                        BufferedReader input = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                        GuiMainMenu.ipAddress = ip = input.readLine();
                        input.close();
                    }
                    catch (Exception whatismyip) {
                        // empty catch block
                    }
                }
            }.start();
            new Thread(){

                @Override
                public void run() {
                    try {
                        String inputLine;
                        URL changelog = new URL("https://dl.dropboxusercontent.com/s/q5ege0qhpzqjzit/Changelog.txt?dl=0");
                        BufferedReader changelogInput = new BufferedReader(new InputStreamReader(changelog.openStream()));
                        while ((inputLine = changelogInput.readLine()) != null) {
                            if (GuiMainMenu.changelogs.size() > 12 || GuiMainMenu.changelogs.contains(inputLine)) continue;
                            GuiMainMenu.changelogs.add(inputLine);
                        }
                        changelogInput.close();
                    }
                    catch (Exception changelog) {
                        // empty catch block
                    }
                }
            }.start();
            this.updateTime = 0;
        }
        GL11.glPushMatrix();
        try {
            int offset = 0;
            for (String s : changelogs) {
                int height = 12 * offset;
                GuiUtils.drawRect(2, h - height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 4, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) + 5, h - height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 8, -869980891);
                GuiUtils.drawRect(Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) + 5, h - height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 4, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) + 6, h - height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 8, -15636873);
                Minecraft.getMinecraft().fontRendererObj.func_175063_a(s, 4.0f, h - height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 2, -1);
                ++offset;
            }
        }
        catch (Exception offset) {
            // empty catch block
        }
        if (mouseX >= w - 100 && mouseY >= 0 && mouseX <= w && mouseY <= 19) {
            GuiUtils.drawRect(w - 110, 0, w, 20, -868928203);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Single Player", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Single Player") - 5, 6.5f, -1);
        } else {
            GuiUtils.drawRect(w - 100, 0, w, 20, -869980891);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Single Player", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Single Player") - 2, 6.5f, -3355444);
        }
        if (mouseX >= w - 100 && mouseY >= 20 && mouseX <= w && mouseY <= 39) {
            GuiUtils.drawRect(w - 110, 20, w, 40, -868928203);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Multi Player", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Multi Player") - 5, 26.0f, -1);
        } else {
            GuiUtils.drawRect(w - 100, 20, w, 40, -869980891);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Multi Player", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Multi Player") - 2, 26.0f, -3355444);
        }
        if (mouseX >= w - 100 && mouseY >= 40 && mouseX <= w && mouseY <= 59) {
            GuiUtils.drawRect(w - 110, 40, w, 60, -868928203);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Options", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Options") - 5, 46.899998f, -1);
        } else {
            GuiUtils.drawRect(w - 100, 40, w, 60, -869980891);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Options", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Options") - 2, 46.899998f, -3355444);
        }
        if (mouseX >= w - 100 && mouseY >= 60 && mouseX <= w && mouseY <= 79) {
            GuiUtils.drawRect(w - 110, 60, w, 80, -868928203);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Login", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Login") - 5, 66.0f, -1);
        } else {
            GuiUtils.drawRect(w - 100, 60, w, 80, -869980891);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Login", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Login") - 2, 66.0f, -3355444);
        }
        if (mouseX >= w - 100 && mouseY >= 80 && mouseX <= w && mouseY <= 99) {
            GuiUtils.drawRect(w - 110, 80, w, 100, -868928203);
            GuiUtils.drawRect(w - 110, 100.0, w, 101.5, -15636873);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Quit", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Quit") - 5, 87.119995f, -1);
        } else {
            GuiUtils.drawRect(w - 100, 80, w, 100, -869980891);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Quit", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Quit") - 2, 87.119995f, -3355444);
        }
        GuiUtils.drawRect(w - 100, 100.0, w, 101.5, -15636873);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)3.0f, (float)3.0f, (float)3.0f);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("Faurax \u00a763.6", 2.0f, 2.0f, -1);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glPopMatrix();
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(Minecraft.getMinecraft().session.getUsername(), w - Minecraft.getMinecraft().fontRendererObj.getStringWidth(Minecraft.getMinecraft().session.getUsername()) - 2, h - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 24, -1);
        if (ipAddress == null) {
            Minecraft.getMinecraft().fontRendererObj.func_175063_a("Connecting...", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Connecting...") - 2, h - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 12, -1);
        } else {
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(ipAddress, w - Minecraft.getMinecraft().fontRendererObj.getStringWidth(ipAddress) - 2, h - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 12, -1);
        }
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("\u00a73Release \u00a7f2", w - Minecraft.getMinecraft().fontRendererObj.getStringWidth("Release 2") - 2, h - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 2, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution var2 = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int w = var2.getScaledWidth();
        int h = var2.getScaledHeight();
        if (mouseButton == 0) {
            if (mouseX >= w - 100 && mouseY >= 0 && mouseX <= w && mouseY <= 19) {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
            }
            if (mouseX >= w - 100 && mouseY >= 21 && mouseX <= w && mouseY <= 39) {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
            }
            if (mouseX >= w - 100 && mouseY >= 41 && mouseX <= w && mouseY <= 59) {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            }
            if (mouseX >= w - 100 && mouseY >= 61 && mouseX <= w && mouseY <= 79) {
                this.mc.displayGuiScreen(new GuiLogin(this));
            }
            if (mouseX >= w - 100 && mouseY >= 81 && mouseX <= w && mouseY <= 99) {
                this.mc.shutdown();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object var4 = this.field_104025_t;
        Object object = this.field_104025_t;
        synchronized (object) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
    }

}

