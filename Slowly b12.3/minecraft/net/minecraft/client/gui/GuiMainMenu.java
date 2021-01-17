package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import me.slowly.client.Client;
import me.slowly.client.shaders.fragment.SlowlyParticleShader;
import me.slowly.client.ui.altmanager.GuiAltManager;
import me.slowly.client.ui.changelog.UIChangelog;
import me.slowly.client.ui.menu.UIMenu;
import me.slowly.client.ui.menu.UIMenuSlot;
import me.slowly.client.ui.particle.Particle;
import me.slowly.client.ui.particles.ParticleManager;
import me.slowly.client.ui.particles.particle.ParticleSnow;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
   private ArrayList particles;
   private Random random = new Random();
   private static final AtomicInteger field_175373_f = new AtomicInteger(0);
   private static final Logger logger = LogManager.getLogger();
   private static final Random RANDOM = new Random();
   private int bgstate;
   private float updateCounter;
   private String splashText;
   private GuiButton buttonResetDemo;
   private int panoramaTimer;
   private DynamicTexture viewportTexture;
   private boolean field_175375_v = true;
   private final Object threadLock = new Object();
   private String openGLWarning1;
   private String openGLWarning2;
   private String openGLWarningLink;
   private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
   public static final String field_96138_a;
   private int field_92024_r;
   private int field_92023_s;
   private int field_92022_t;
   private int field_92021_u;
   private int field_92020_v;
   private int field_92019_w;
   private ResourceLocation backgroundTexture;
   private UIMenu menu;
   private GuiButton realmsButton;
   public static ParticleManager particleManager;
   public static SlowlyParticleShader particleShader;
   private double animationX;
   private boolean started;

   static {
      field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   }

   public GuiMainMenu() {
      this.openGLWarning2 = field_96138_a;
      this.splashText = "missingno";
      BufferedReader bufferedreader = null;

      try {
         List list = Lists.newArrayList();
         bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

         String s;
         while((s = bufferedreader.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) {
               list.add(s);
            }
         }

         if (!list.isEmpty()) {
            do {
               this.splashText = (String)list.get(RANDOM.nextInt(list.size()));
            } while(this.splashText.hashCode() == 125780783);
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
         this.openGLWarning1 = I18n.format("title.oldgl1");
         this.openGLWarning2 = I18n.format("title.oldgl2");
         this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

   }

   public void updateScreen() {
      ++this.panoramaTimer;
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void initGui() {
      this.particles = new ArrayList();
      ScaledResolution resolution = new ScaledResolution(this.mc);

      for(int i = 0; i < 150; ++i) {
         this.particles.add(new Particle(this.random.nextInt(resolution.getScaledWidth()) + 10, this.random.nextInt(resolution.getScaledHeight())));
      }

      this.mc.gameSettings.guiScale = 2;
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

      boolean i = true;
      int j = this.height / 4 + 48;
      if (this.mc.isDemo()) {
         this.addDemoButtons(j, 24);
      } else {
         this.addSingleplayerMultiplayerButtons(j, 24);
      }

      Object var5 = this.threadLock;
      synchronized(this.threadLock) {
         this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
         this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
         int k = Math.max(this.field_92023_s, this.field_92024_r);
         this.field_92022_t = (this.width - k) / 2;
         this.field_92020_v = this.field_92022_t + k;
         this.field_92019_w = this.field_92021_u + 24;
      }

      this.mc.func_181537_a(false);
      particleManager = new ParticleManager(new ParticleSnow(), 100);
      ArrayList slots = new ArrayList();
      slots.add(new UIMenuSlot("Single L", new GuiSelectWorld(this)));
      slots.add(new UIMenuSlot("Multi L", new GuiMultiplayer(this)));
      slots.add(new UIMenuSlot("AltManager", new GuiAltManager(this)));
      slots.add(new UIMenuSlot("Options", new GuiOptions(this, this.mc.gameSettings)));
      slots.add(new UIMenuSlot("Language", new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager())));
      slots.add(new UIMenuSlot("Changelog", new UIChangelog(this)));
      slots.add(new UIMenuSlot("Quit", (GuiScreen)null));
      this.menu = new UIMenu(slots);
      particleShader = new SlowlyParticleShader();
      this.started = false;
   }

   private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
      boolean width = true;
      boolean height = true;
   }

   private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
      this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
      this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo")));
      ISaveFormat isaveformat = this.mc.getSaveLoader();
      WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
      if (worldinfo == null) {
         this.buttonResetDemo.enabled = false;
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
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

      if (button.id == 14 && this.realmsButton.visible) {
         this.switchToRealms();
      }

      if (button.id == 4) {
         this.mc.displayGuiScreen(new GuiAltManager(this));
      }

      if (button.id == 3) {
         this.mc.shutdown();
      }

      if (button.id == 11) {
         this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
      }

      if (button.id == 12) {
         ISaveFormat isaveformat = this.mc.getSaveLoader();
         WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
         if (worldinfo != null) {
            GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
            this.mc.displayGuiScreen(guiyesno);
         }
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
               Class oclass = Class.forName("java.awt.Desktop");
               Object object = oclass.getMethod("getDesktop").invoke((Object)null);
               oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
            } catch (Throwable var5) {
               logger.error("Couldn't open link", var5);
            }
         }

         this.mc.displayGuiScreen(this);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution res = new ScaledResolution(this.mc);
      RenderUtil.drawImage(new ResourceLocation("slowly/wallpaper.jpg"), (int)this.animationX, 0, res.getScaledWidth(), res.getScaledHeight());
      Iterator var6 = this.particles.iterator();

      while(var6.hasNext()) {
         Particle particle = (Particle)var6.next();
         particle.drawScreen(mouseX, mouseY, res.getScaledHeight());
      }

      UnicodeFontRenderer font = Client.getInstance().getFontManager().Gotham60;
      int xStart = (int)this.animationX + (this.width - font.getStringWidth("Slowly")) / 2;
      int yMid = this.height / 2 - 40;
      int oldX = xStart;
      font.drawString("L COPY".toUpperCase(), (float)xStart + 0.5F, (float)yMid - (float)font.FONT_HEIGHT / 1.25F + 0.5F, 1);
      font.drawString("L COPY".toUpperCase(), (float)xStart, (float)yMid - (float)font.FONT_HEIGHT / 1.25F, -1);
      xStart += font.getStringWidth("Slowly");
      Gui.drawRect((float)oldX, (float)(yMid + 15), (float)(xStart + 30), (float)(yMid + 15) + 0.5F, Colors.BLACK.c);
      font = Client.getInstance().getFontManager().simpleton20;
      font.drawString("CTRL+C CTRL+V = YES! \nNice Copyer! \nLLLLLLLLLLLLLLLLLLLLLLL", (float)((int)this.animationX + (res.getScaledWidth() - font.getStringWidth("CTRL+C CTRL+V = YES! \nNice Copyer! \nLLLLLLLLLLLLLLLLLLLLLLL")) / 2 + 15) + 0.5F, (float)(yMid + 25) + 0.5F, 1);
      this.menu.draw(mouseX, mouseY);
      if (this.menu.x == 0.0F) {
         this.animationX = RenderUtil.getAnimationState(this.animationX, 100.0D, (100.0D - this.animationX + 1.0D) * 10.0D);
      } else {
         this.animationX = RenderUtil.getAnimationState(this.animationX, 0.0D, (Math.abs(this.animationX) + 1.0D) * 10.0D);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      Object var4 = this.threadLock;
      synchronized(this.threadLock) {
         if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
            GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
            guiconfirmopenlink.disableSecurityWarning();
            this.mc.displayGuiScreen(guiconfirmopenlink);
         }

      }
   }
}
