// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.common.collect.Lists;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.AltManager.GUI.GuiAltManager;
import me.CheerioFX.FusionX.GUI.FusionX_Options;
import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.utils.BrowserUtil;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private static final AtomicInteger field_175373_f;
	private static final Logger logger;
	private static final Random field_175374_h;
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
	private ResourceLocation field_110351_G;
	private GuiButton field_175372_K;
	private static final String __OBFID = "CL_00001154";
	private static final ResourceLocation background;

	static {
		field_175373_f = new AtomicInteger(0);
		logger = LogManager.getLogger();
		field_175374_h = new Random();
		splashTexts = new ResourceLocation("texts/splashes.txt");
		minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
		titlePanoramaPaths = new ResourceLocation[] {
				new ResourceLocation("textures/gui/title/background/panorama_0.png"),
				new ResourceLocation("textures/gui/title/background/panorama_1.png"),
				new ResourceLocation("textures/gui/title/background/panorama_2.png"),
				new ResourceLocation("textures/gui/title/background/panorama_3.png"),
				new ResourceLocation("textures/gui/title/background/panorama_4.png"),
				new ResourceLocation("textures/gui/title/background/panorama_5.png") };
		field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET
				+ " for more information.";
		background = new ResourceLocation("textures/gui/title/background/bg.png");
	}

	public GuiMainMenu() {
		this.field_175375_v = true;
		this.field_104025_t = new Object();
		this.field_146972_A = GuiMainMenu.field_96138_a;
		this.splashText = "missingno";
		BufferedReader var1 = null;
		try {
			final ArrayList<String> var2 = Lists.newArrayList();
			var1 = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.splashTexts).getInputStream(),
					Charsets.UTF_8));
			String var3;
			while ((var3 = var1.readLine()) != null) {
				var3 = var3.trim();
				if (!var3.isEmpty()) {
					var2.add(var3);
				}
			}
			if (!var2.isEmpty()) {
				do {
					this.splashText = var2.get(GuiMainMenu.field_175374_h.nextInt(var2.size()));
				} while (this.splashText.hashCode() == 125780783);
			}
		} catch (IOException ex) {
		} finally {
			if (var1 != null) {
				try {
					var1.close();
				} catch (IOException ex2) {
				}
			}
		}
		if (var1 != null) {
			try {
				var1.close();
			} catch (IOException ex3) {
			}
		}
		this.updateCounter = GuiMainMenu.field_175374_h.nextFloat();
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
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
	}

	@Override
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
		final Calendar var1 = Calendar.getInstance();
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
		final boolean var2 = true;
		final int var3 = this.height / 4 + 48;
		if (this.mc.isDemo()) {
			this.addDemoButtons(var3, 24);
		} else {
			this.addSingleplayerMultiplayerButtons(var3, 24);
		}
		if (!GhostClient.enabled) {
			this.buttonList.add(
					new GuiButton(50, this.width / 2 + 2, var3, 98, 20, I18n.format("§eAlt Manager", new Object[0])));
			this.buttonList.add(new GuiButton(99, this.width - 112, 3, 110, 20, "§aDonate to Dev"));
			this.buttonList.add(new GuiButton(100, this.width / 2 - 100, var3 + 48, 98, 20, "§6Client Options"));
			this.buttonList.add(new GuiButton(101, this.width - 112, 30, 110, 20, "§cYouTube Channel"));
			this.buttonList.add(new GuiButton(102, this.width - 112, 51, 110, 20, "§bTwitter"));
			this.buttonList.add(new GuiButton(103, this.width - 112, 72, 110, 20, "§eClient Website"));
			this.buttonList.add(new GuiButton(104, this.width - 112, 99, 110, 20, "§dChangeLog"));
			this.buttonList.add(new GuiButton(105, this.width - 112, 120, 110, 20, "§9Free Minecraft Alts"));
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, 98, 20, "§1Options..."));
			this.buttonList.add(new GuiButton(4, this.width / 2 + 2, var3 + 72 + 12, 98, 20, "§4Quit Game"));
			this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, var3 + 72 + 12));
		} else {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, 98, 20,
					I18n.format("menu.options", new Object[0])));
			this.buttonList.add(new GuiButton(4, this.width / 2 + 2, var3 + 72 + 12, 98, 20,
					I18n.format("menu.quit", new Object[0])));
			this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, var3 + 72 + 12));
		}
		final Object var4 = this.field_104025_t;
		synchronized (this.field_104025_t) {
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
			final int var5 = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - var5) / 2;
			this.field_92021_u = this.buttonList.get(0).yPosition - 24;
			this.field_92020_v = this.field_92022_t + var5;
			this.field_92019_w = this.field_92021_u + 24;
		}
		// monitorexit(this.field_104025_t)
	}

	private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
		if (!GhostClient.enabled) {
			this.buttonList
					.add(new GuiButton(106, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, "§9Discord"));
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, 98, 20, "§aSingleplayer"));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, "§bMultiplayer"));
		} else {
			this.buttonList.add(this.field_175372_K = new GuiButton(14, this.width / 2 - 100,
					p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new Object[0])));
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_,
					I18n.format("menu.singleplayer", new Object[0])));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1,
					I18n.format("menu.multiplayer", new Object[0])));
		}
	}

	private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
		this.buttonList
				.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
		this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1,
				I18n.format("menu.resetdemo", new Object[0])));
		final ISaveFormat var3 = this.mc.getSaveLoader();
		final WorldInfo var4 = var3.getWorldInfo("Demo_World");
		if (var4 == null) {
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
		if (button.id == 14 && this.field_175372_K.visible) {
			this.switchToRealms();
		}
		if (button.id == 4) {
			this.mc.shutdown();
		}
		if (button.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}
		if (button.id == 12) {
			final ISaveFormat var2 = this.mc.getSaveLoader();
			final WorldInfo var3 = var2.getWorldInfo("Demo_World");
			if (var3 != null) {
				final GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
				this.mc.displayGuiScreen(var4);
			}
		}
		if (button.id == 50) {
			final GuiAltManager altManager = new GuiAltManager();
			Wrapper.mc.displayGuiScreen(altManager);
		}
		if (button.id == 99) {
			try {
				BrowserUtil.openWebpage(new URL("https://donorbox.org/cc4e4b8b-d31c-4125-b6c5-104e28871a9d"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 100) {
			final FusionX_Options ClientOptions = new FusionX_Options(this);
			Wrapper.mc.displayGuiScreen(ClientOptions);
		}
		if (button.id == 101) {
			try {
				BrowserUtil.openWebpage(new URL("https://www.youtube.com/channel/UCXQb3R_8gpUVxS-6CNhdHgA"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 102) {
			try {
				BrowserUtil.openWebpage(new URL("https://twitter.com/CheerioFX"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 103) {
			try {
				BrowserUtil.openWebpage(new URL("http://fusionxclient.weebly.com/"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 104) {
			try {
				BrowserUtil.openWebpage(new URL("https://fusionxclient.weebly.com/client-info--updates"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 105) {
			try {
				BrowserUtil.openWebpage(new URL("https://www.youtube.com/c/DailyMcAlts"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 106) {
			try {
				BrowserUtil.openWebpage(new URL("https://discord.gg/x3ZMqVW"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchToRealms() {
		final RealmsBridge var1 = new RealmsBridge();
		var1.switchToRealms(this);
	}

	@Override
	public void confirmClicked(final boolean result, final int id) {
		if (result && id == 12) {
			final ISaveFormat var6 = this.mc.getSaveLoader();
			var6.flushCache();
			var6.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		} else if (id == 13) {
			if (result) {
				try {
					final Class var7 = Class.forName("java.awt.Desktop");
					final Object var8 = var7.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
					var7.getMethod("browse", URI.class).invoke(var8, new URI(this.field_104024_v));
				} catch (Throwable var9) {
					GuiMainMenu.logger.error("Couldn't open link", var9);
				}
			}
			this.mc.displayGuiScreen(this);
		}
	}

	private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
		final Tessellator var4 = Tessellator.getInstance();
		final WorldRenderer var5 = var4.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
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
		final byte var6 = 8;
		for (int var7 = 0; var7 < var6 * var6; ++var7) {
			GlStateManager.pushMatrix();
			final float var8 = (var7 % var6 / var6 - 0.5f) / 64.0f;
			final float var9 = (var7 / var6 / var6 - 0.5f) / 64.0f;
			final float var10 = 0.0f;
			GlStateManager.translate(var8, var9, var10);
			GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f,
					0.0f, 0.0f);
			GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
			for (int var11 = 0; var11 < 6; ++var11) {
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
				this.mc.getTextureManager().bindTexture(GuiMainMenu.titlePanoramaPaths[var11]);
				var5.startDrawingQuads();
				var5.func_178974_a(16777215, 255 / (var7 + 1));
				final float var12 = 0.0f;
				var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
				var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
				var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
				var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
				var4.draw();
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
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

	private void rotateAndBlurSkybox(final float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glTexParameteri(3553, 10241, 9729);
		GL11.glTexParameteri(3553, 10240, 9729);
		GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		final Tessellator var2 = Tessellator.getInstance();
		final WorldRenderer var3 = var2.getWorldRenderer();
		var3.startDrawingQuads();
		GlStateManager.disableAlpha();
		final byte var4 = 3;
		for (int var5 = 0; var5 < var4; ++var5) {
			var3.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f / (var5 + 1));
			final int var6 = this.width;
			final int var7 = this.height;
			final float var8 = (var5 - var4 / 2) / 256.0f;
			var3.addVertexWithUV(var6, var7, this.zLevel, 0.0f + var8, 1.0);
			var3.addVertexWithUV(var6, 0.0, this.zLevel, 1.0f + var8, 1.0);
			var3.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
			var3.addVertexWithUV(0.0, var7, this.zLevel, 0.0f + var8, 0.0);
		}
		var2.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
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
		final Tessellator var4 = Tessellator.getInstance();
		final WorldRenderer var5 = var4.getWorldRenderer();
		var5.startDrawingQuads();
		final float var6 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
		final float var7 = this.height * var6 / 256.0f;
		final float var8 = this.width * var6 / 256.0f;
		var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
		final int var9 = this.width;
		final int var10 = this.height;
		var5.addVertexWithUV(0.0, var10, this.zLevel, 0.5f - var7, 0.5f + var8);
		var5.addVertexWithUV(var9, var10, this.zLevel, 0.5f - var7, 0.5f - var8);
		var5.addVertexWithUV(var9, 0.0, this.zLevel, 0.5f + var7, 0.5f - var8);
		var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.5f + var7, 0.5f + var8);
		var4.draw();
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		if (GhostClient.enabled) {
			GlStateManager.disableAlpha();
			this.renderSkybox(mouseX, mouseY, partialTicks);
			GlStateManager.enableAlpha();
			final Tessellator var4 = Tessellator.getInstance();
			final WorldRenderer var5 = var4.getWorldRenderer();
			final short var6 = 274;
			final int var7 = this.width / 2 - var6 / 2;
			final byte var8 = 30;
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
			this.mc.getTextureManager().bindTexture(GuiMainMenu.minecraftTitleTextures);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			if (this.updateCounter < 1.0E-4) {
				this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 99, 44);
				this.drawTexturedModalRect(var7 + 99, var8 + 0, 129, 0, 27, 44);
				this.drawTexturedModalRect(var7 + 99 + 26, var8 + 0, 126, 0, 3, 44);
				this.drawTexturedModalRect(var7 + 99 + 26 + 3, var8 + 0, 99, 0, 26, 44);
				this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
			} else {
				this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 155, 44);
				this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
			}
			var5.func_178991_c(-1);
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.width / 2 + 90, 70.0f, 0.0f);
			GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
			float var9 = 1.8f - MathHelper
					.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
			var9 = var9 * 100.0f / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
			GlStateManager.scale(var9, var9, var9);
			this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
			GlStateManager.popMatrix();
		} else {
			this.drawBackground(new ScaledResolution(this.mc, mouseY, mouseY));
			GlStateManager.enableAlpha();
			final Tessellator var4 = Tessellator.getInstance();
			final WorldRenderer var5 = var4.getWorldRenderer();
			final short var6 = 274;
			final int var7 = this.width / 2 - var6 / 2;
			final byte var8 = 30;
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
			this.mc.getTextureManager().bindTexture(GuiMainMenu.minecraftTitleTextures);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			if (this.updateCounter < 1.0E-4) {
				this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 99, 44);
				this.drawTexturedModalRect(var7 + 99, var8 + 0, 129, 0, 27, 44);
				this.drawTexturedModalRect(var7 + 99 + 26, var8 + 0, 126, 0, 3, 44);
				this.drawTexturedModalRect(var7 + 99 + 26 + 3, var8 + 0, 99, 0, 26, 44);
				this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
			} else {
				this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 155, 44);
				this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
			}
			var5.func_178991_c(-1);
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.width / 2 + 90, 70.0f, 0.0f);
			GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
			float var9 = 1.8f - MathHelper
					.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
			var9 = var9 * 100.0f / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
			GlStateManager.scale(var9, var9, var9);
			this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
			GlStateManager.popMatrix();
		}
		String var10;
		String var11;
		if (GhostClient.enabled) {
			var10 = "Minecraft 1.8";
			var11 = "Copyright Mojang AB. Do not distribute!";
		} else {
			var10 = "§4§lFusion§f§lX " + FusionX.theClient.Client_Version;
			var11 = "§oCheck out §1§lDaily§f§lMC§rAlts§o on Youtube for Free Accounts Every Day!";
		}
		if (this.mc.isDemo()) {
			var10 = String.valueOf(var10) + " Demo";
		}
		this.drawString(this.fontRendererObj, var10, 2, this.height - 10, -1);
		this.drawString(this.fontRendererObj, var11, this.width - this.fontRendererObj.getStringWidth(var11) - 2,
				this.height - 10, -1);
		if (this.field_92025_p != null && this.field_92025_p.length() > 0) {
			Gui.drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1,
					1428160512);
			this.drawString(this.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRendererObj, this.field_146972_A, (this.width - this.field_92024_r) / 2,
					this.buttonList.get(0).yPosition - 12, -1);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawBackground(final ScaledResolution scaledRes) {
		final ScaledResolution var22 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		final int sw = var22.getScaledWidth();
		final int sh = var22.getScaledHeight();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableAlpha();
		this.mc.getTextureManager().bindTexture(GuiMainMenu.background);
		final Tessellator var23 = Tessellator.getInstance();
		final WorldRenderer var24 = var23.getWorldRenderer();
		var24.startDrawingQuads();
		var24.addVertexWithUV(0.0, sh, -90.0, 0.0, 1.0);
		var24.addVertexWithUV(sw, sh, -90.0, 1.0, 1.0);
		var24.addVertexWithUV(sw, 0.0, -90.0, 1.0, 0.0);
		var24.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
		var23.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		final Object var4 = this.field_104025_t;
		synchronized (this.field_104025_t) {
			if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v
					&& mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
				final GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
				var5.disableSecurityWarning();
				this.mc.displayGuiScreen(var5);
			}
		}
	}
}
