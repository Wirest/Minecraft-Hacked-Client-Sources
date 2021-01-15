package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import dev.astroclient.client.Client;
import dev.astroclient.client.ui.menu.MenuButton;
import dev.astroclient.client.ui.menu.account.LoginGui;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import dev.astroclient.security.indirection.MethodIndirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    /**
     * Counts the number of screen updates.
     */
    private float updateCounter;

    /**
     * The splash message.
     */
    private String splashText;
    private GuiButton buttonResetDemo;

    /**
     * Timer used to rotate the panorama, increases every tick.
     */
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

    /**
     * OpenGL graphics card warning.
     */
    private String openGLWarning1;

    /**
     * OpenGL graphics card warning.
     */
    private String openGLWarning2;

    /**
     * Link to the Mojang Support about minimum requirements
     */
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

    private final ResourceLocation[] menuButtons = new ResourceLocation[]{
            new ResourceLocation("client/images/menu/white.png"),
            new ResourceLocation("client/images/menu/white2.png"),
            new ResourceLocation("client/images/menu/white3.png"),
            new ResourceLocation("client/images/menu/white4.png"),
            new ResourceLocation("client/images/menu/white5.png")
    };
    /**
     * An array of all the paths to the panorama pictures.
     */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;

    /**
     * Minecraft Realms button.
     */
    private GuiButton realmsButton;

    public GuiMainMenu() {
        this.openGLWarning2 = field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try {
            List<String> list = Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();

                if (!s.isEmpty()) {
                    list.add(s);
                }
            }

            if (!list.isEmpty()) {
                while (true) {
                    this.splashText = (String) list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783) {
                        break;
                    }
                }
            }
        } catch (IOException var12) {
            ;
        } finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                } catch (IOException var11) {
                    ;
                }
            }
        }

        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        ++this.panoramaTimer;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    //private String sessionServer = "Session Server: \247e Checking...";
    //private String authServer = "Auth Server: \247e Checking...";
    //private String webServer = "Web Server: \247e Checking...";
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
       /* try {
            URL url = new URL("https://status.mojang.com/check");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(bufferedReader.readLine(), JsonArray.class);
            JsonElement session = jsonArray.get(4);
            JsonElement auth = jsonArray.get(3);
            JsonElement web = jsonArray.get(7);
            switch (session.getAsJsonObject().get("sessionserver.mojang.com").getAsString()) {
                case "green":
                    sessionServer = "Session Server: \247aGood";
                    break;
                case "yellow":
                    sessionServer = "Session Server: \247eIssue";
                    break;
                case "red":
                    sessionServer = "Session Server: \247cDown";
                    break;
            }
            switch (auth.getAsJsonObject().get("authserver.mojang.com").getAsString()) {
                case "green":
                    authServer = "Auth Server: \247aGood";
                    break;
                case "yellow":
                    authServer = "Auth Server: \247eIssue";
                    break;
                case "red":
                    authServer = "Auth Server: \247cDown";
                    break;
            }
            switch (web.getAsJsonObject().get("mojang.com").getAsString()) {
                case "green":
                    webServer = "Web Server: \247aGood";
                    break;
                case "yellow":
                    webServer = "Web Server: \247eIssue";
                    break;
                case "red":
                    webServer = "Web Server: \247cDown";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        int height = this.height / 2 - 40;
        this.buttonList.add(new MenuButton(0, this.width / 2 - 100 + 10, height, 180, 20, I18n.format("\247rSingleplayer", new Object[0])));
        this.buttonList.add(new MenuButton(1, this.width / 2 - 100 + 10, height + 22, 180, 20, I18n.format("\247rMultiplayer", new Object[0])));
        this.buttonList.add(new MenuButton(2, this.width / 2 - 100 + 10, height + 22 * 2, 180, 20, I18n.format("\247rLogin", new Object[0])));
        this.buttonList.add(new MenuButton(3, this.width / 2 - 100 + 10, height + 22 * 3, 180, 20, I18n.format("\247rOptions", new Object[0])));
        this.buttonList.add(new MenuButton(4, this.width / 2 - 100 + 10, height + 22 * 5, 180, 20, I18n.format("\247rExit Game", new Object[0])));
        this.buttonList.add(new MenuButton(5, this.width / 2 - 100 + 10, height + 22 * 4, 180, 20, I18n.format("\247rLanguage", new Object[0])));


        synchronized (this.threadLock) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }

        this.mc.func_181537_a(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                mc.displayGuiScreen(new LoginGui(this));
                break;
            case 3:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
            case 4:
                mc.shutdown();
                break;
            case 5:
                mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                break;
        }
    }

    private void switchToRealms() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI(this.openGLWarningLink)});
                } catch (Throwable throwable) {
                    logger.error("Couldn\'t open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Render2DUtil.drawMenuBackground(scaledResolution);
        int width = 200;
        int height = 220;
        int posX = this.width / 2 - width / 2;
        int posY = this.height / 2 - height / 3 - 20;

        Render2DUtil.drawRect(posX, posY, posX + width, posY + height, new Color(5, 5, 5, 255).getRGB());
        Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width - .5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width - 2, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - 2.5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + 3, posY + 3, posX + (width / 3), posY + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + (width / 3), posY + 3, posX + ((width / 3) * 2), posY + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + ((width / 3) * 2), posY + 3, posX + ((width / 3) * 3) - 1, posY + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());
        //Client.INSTANCE.boldFontRenderer.drawCenteredStringWithShadow("Cracked By PokeS.", this.width / 2, posY + 15, new Color(195, 195, 195).getRGB());
        mc.fontRendererObj.drawStringWithShadow("Cracked By PokeS." , 2, this.height - 20, -1);
        mc.fontRendererObj.drawStringWithShadow("Cracked By PokeS." , 2, this.height - 10, -1);
        // TODO: Change Log
        String[] changes = new String[]{
                ": Version: v2.2.1",
                "+ Added lockview option to killaura",
                "+ Added disable option to killaura",
                "- Removed hvh option from killaura",
                "* Fixed step with jump boost",
                "+ Added fade color mode to HUD",
                "+ Added no hurt camera",
                "* Fixed antibot",
                "* Actually fixed auto sword",
                "+ Added auto say",
                "+ Added chat bypass",
                "* Improved speed slightly",
                "* Renamed all \"Hypixel\" modes to \"Watchdog\"",
                "* Increased speedy distance on flight",
                "+ Added fonts to hud",
                "+ Added rainbow brightness option to HUD",
                "+ Added block distance option to killaura",
                "* Fixed speed with jump boost",
                "+ Added through walls option to killaura",
                "- Removed watchdog ban with certain killaura and criticals settings",
                "+ Added sexier name tags (in 2DESP)",
                "+ Added visible and hidden opacity to chams",
                "* Fixed criticals flagging"
        };
        String title = "" +
                "\247F< \247CChangelog \247F>";
        mc.fontRendererObj.drawStringWithShadow(title, 2, 2, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, 2, 2, mc.fontRendererObj.getStringWidth(title), 9)) {
            for (int i = 0; i < changes.length; i++) {
                String change = changes[i];

                int color = -1;

                switch (change.charAt(0)) {
                    case ':':
                        color = new Color(0x5A73FF).getRGB();
                        break;
                    case '+':
                        color = new Color(0x2FFF11).getRGB();
                        break;
                    case '-':
                        color = new Color(0xFF5560).getRGB();
                        break;
                    case '*':
                        color = new Color(0xFFF33E).getRGB();
                        break;
                }
                change = change.substring(1);
                mc.fontRendererObj.drawStringWithShadow(change, 2, 12, color);

                GlStateManager.translate(0, Client.INSTANCE.smallFontRenderer.getStringHeight(change) + 5, 0);
            }
        }
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }
}
