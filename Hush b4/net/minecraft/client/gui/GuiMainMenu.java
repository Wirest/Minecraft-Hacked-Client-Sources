// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import me.nico.hush.Client;
import me.nico.hush.utils.Fonts;
import java.net.URI;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.world.demo.DemoWorldServer;
import me.nico.hush.gui.MojangLogin;
import me.nico.hush.gui.Changelog;
import me.nico.hush.gui.TheAlteningLogin;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import me.nico.hush.gui.Button2;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random RANDOM;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object threadLock;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    
    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
    
    public GuiMainMenu() {
        this.field_175375_v = true;
        this.threadLock = new Object();
        this.openGLWarning2 = GuiMainMenu.field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;
        try {
            final List<String> list = (List<String>)Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(GuiMainMenu.RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                }
                catch (IOException ex2) {}
            }
        }
        if (bufferedreader != null) {
            try {
                bufferedreader.close();
            }
            catch (IOException ex3) {}
        }
        this.updateCounter = GuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
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
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final int i = 24;
        final int j = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        this.buttonList.add(new Button2(0, this.width / 2 - 100, j + 85 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new Button2(88, this.width / 2 - 100, j + 36 + 12, 98, 20, I18n.format("Mojang Login", new Object[0])));
        this.buttonList.add(new Button2(3, this.width / 2 + 2, j + 36 + 12, 98, 20, I18n.format("TheAltening Login", new Object[0])));
        this.buttonList.add(new Button2(4, this.width / 2 + 2, j + 85 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        synchronized (this.threadLock) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }
        // monitorexit(this.threadLock)
        this.mc.func_181537_a(false);
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new Button2(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new Button2(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new Button2(420, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("Changelog", new Object[0])));
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(new TheAlteningLogin(this));
        }
        if (button.id == 420) {
            this.mc.displayGuiScreen(new Changelog(this));
        }
        if (button.id == 88) {
            this.mc.displayGuiScreen(new MojangLogin(this));
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                final GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
    }
    
    private void switchToRealms() {
        final RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable) {
                    GuiMainMenu.logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        final ScaledResolution s1 = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("Zodiac/MainBackGround.jpg"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, s1.getScaledWidth(), s1.getScaledHeight(), (float)s1.getScaledWidth(), (float)s1.getScaledHeight());
        Gui.drawRect(0.0, 0.0, this.width, this.height, 1073741824);
        final String name = "Z\u03a6DIAC";
        Fonts.zodiacname.drawString(String.valueOf(name), this.width / 2 - Fonts.zodiacname.getStringWidth(String.valueOf(String.valueOf(Client.instance.ClientName)) + Client.instance.ClientVersion) / 3 - 0 - 1, this.height / 5 - 8 - 25, 16777215);
        Fonts.zodiacname.drawString(String.valueOf("Made by " + Client.instance.ClientCoder), this.width / 2 - Fonts.zodiacname.getStringWidth(String.valueOf(String.valueOf(Client.instance.ClientName)) + Client.instance.ClientVersion) / 1 - 15 - 1, this.height / 4 - 1 - 25, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
        // monitorexit(this.threadLock)
    }
}
