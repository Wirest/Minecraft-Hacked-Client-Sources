package net.minecraft.client.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.common.collect.Lists;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.main.ReturnType;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.LoadTools;
import me.robbanrobbin.jigsaw.cracker.gui.GuiJigsawAccHacker;
import me.robbanrobbin.jigsaw.gui.GuiJigsaw;
import me.robbanrobbin.jigsaw.gui.GuiJigsawAltLogin;
import me.robbanrobbin.jigsaw.gui.GuiJigsawAltManager;
import me.robbanrobbin.jigsaw.gui.GuiJigsawChangelog;
import me.robbanrobbin.jigsaw.gui.GuiJigsawCredits;
import me.robbanrobbin.jigsaw.gui.GuiMCLeaksRedeemToken;
import me.robbanrobbin.jigsaw.gui.animations.Animation;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.DisplayClickGui;
import me.robbanrobbin.jigsaw.gui.main.MenuParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session.Type;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private static final AtomicInteger field_175373_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();
	// TODO Jigsaw vars
	private static boolean flashScreen = false;
	public boolean doCheckVersion = false;

	/** Counts the number of screen updates. */
	private float updateCounter;

	/** The splash message. */
	private String splashText;
	private GuiButton buttonResetDemo;

	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;

	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;
	private boolean field_175375_v = true;

	/**
	 * The Object object utilized as a thread lock when performing non
	 * thread-safe operations
	 */
	private final Object threadLock = new Object();

	/** OpenGL graphics card warning. */
	private String openGLWarning1;

	/** OpenGL graphics card warning. */
	private String openGLWarning2;

	/** Link to the Mojang Support about minimum requirements */
	private String openGLWarningLink;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");

	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here"
			+ EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation backgroundTexture;

	private int guiScaleBefore = 2;

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		guiScaleBefore = mc.gameSettings.guiScale;
		width = mc.displayWidth / 2;
		height = mc.displayHeight / 2;
		mc.gameSettings.guiScale = 2;
		super.setWorldAndResolution(mc, width, height);
	}

	// TODO Jigsaw texture n stuff

	/** Minecraft Realms button. */
	private GuiButton realmsButton;

	public ArrayList<MenuParticle> particles = new ArrayList<MenuParticle>();

	public int particleCount = 20000;
	
	private boolean accountInfo;
	private boolean accountInfoOverride;

	@Override
	public void onGuiClosed() {
		mc.gameSettings.guiScale = guiScaleBefore;
		super.onGuiClosed();
	}

	public GuiMainMenu() {
		// TODO perticles

		particles.clear();
		this.openGLWarning2 = field_96138_a;
		this.splashText = "missingno";
		BufferedReader bufferedreader = null;

		try {
			List<String> list = Lists.<String>newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
					Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					list.add(s);
				}
			}

			if (!list.isEmpty()) {
				while (true) {
					// TODO Atlas code
					if (Jigsaw.ghostMode) {
						this.splashText = (String) list.get(RANDOM.nextInt(list.size()));
					} else {
						this.splashText = (String) Jigsaw.getModules().get(RANDOM.nextInt(Jigsaw.getModules().size()))
								.getName() + "!";
					}
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

	public GuiMainMenu(boolean doUpdateCheck) {
		this();
		int img = RANDOM.nextInt(5);
		doCheckVersion = doUpdateCheck;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		// TODO Jigsaw alert thing
		if (Jigsaw.triedConnectToUpdate && !Jigsaw.triedConnectToAlert) {
			new Thread(LoadTools.alertInfoFetcher).start();
			Jigsaw.triedConnectToAlert = true;
		}
		++this.panoramaTimer;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called
	 * when the GUI is displayed and when the window resizes, the buttonList is
	 * cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();
		

		particles.clear();
		anim.off();
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",
				this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		}

		int i = 24;
		int j = this.height / 4 + 48;

		if (this.mc.isDemo()) {
			this.addDemoButtons(j, 24);
		} else {
			this.addSingleplayerMultiplayerButtons(j, 24);
		}
		// TODO Jigsaw alt button and settings button main menu
		if (!Jigsaw.ghostMode) {
			//this.buttonList.add(new GuiButton(69, 5, this.height - 25, 98, 20, "Alt Login"));
			this.buttonList
					.add(new GuiButton(1337, width - 100, 132 + 11 - 66, 100, 20, Jigsaw.getClientName().split(" ")[0]));
			this.buttonList.add(new GuiButton(426, width - 170, 0, 170, 20, "How to Use Infinite Reach"));
			//this.buttonList.add(new GuiButton(420, width - 100, 0 + 22, 100, 20, "Changelog"));
			//this.buttonList.add(new GuiButton(421, width - 100, 22 + 22, 100, 20, "Bugs"));
			//this.buttonList.add(new GuiButton(422, width - 100, 44 + 22, 100, 20, "Suggestions"));
			//this.buttonList.add(new GuiButton(423, width - 100, 44 + 22, 100, 20, "Credits"));
			//this.buttonList.add(new GuiButton(424, width - 100, 22 + 22, 100, 20, "YouTube"));
			this.buttonList.add(new GuiButton(427, width - 100, 66 + -44, 100, 20, "MCLeaks"));
			this.buttonList.add(new GuiButton(429, width - 100, 198 - 88, 100, 20, "Particles"));
			this.buttonList.add(new GuiButton(428, width - 100, 132, 100, 20, "Modules"));
			// this.buttonList.add(new GuiButton(425, width - 100, 110 + 22,
			// 100, 20, "Acc Hacker"));
		}
		if (Jigsaw.ghostMode) {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20,
					I18n.format("menu.options", new Object[0])));
			this.buttonList.add(
					new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
			this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));
		} else {
			this.buttonList.add(
					new GuiButton(0, -105 + 320, this.height - 25, 98, 20, I18n.format("menu.options", new Object[0])));
			this.buttonList.add(new GuiButton(4, this.width - 69, this.height - 25, 65, 20,
					I18n.format("menu.quit", new Object[0])));
			this.buttonList.add(new GuiButton(5, width - 100, 154 - 110, 100, 20, "Language"));
			this.mc.func_181537_a(false);
			if (ClientSettings.mainMenuParticles) {
				anim.on();
				for (int iii = 0; iii < particleCount; iii++) {
					double randomX = -2 + (2 - -2) * RANDOM.nextDouble();
					double randomY = -2 + (2 - -2) * RANDOM.nextDouble();
					double randomXm = -0 + (width - -0) * RANDOM.nextDouble();
					double randomYm = -0 + (height - -0) * RANDOM.nextDouble();
					double randomDepthm = RANDOM.nextDouble() + 0.1;
					int mX = 0;
					int mY = 0;
					MenuParticle part = new MenuParticle(randomXm + 0, randomYm + 0, randomDepthm + 0, true)
							.addMotion(randomX + mX / 4, randomY + mY / 4);
					part.alpha = 0.15f;
					particles.add(part);
				}
			} else {
				for (MenuParticle part : particles) {
					part.alphaDecay = true;
				}
			}
		}

		synchronized (this.threadLock) {
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
			int k = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - k) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - 24;
			this.field_92020_v = this.field_92022_t + k;
			this.field_92019_w = this.field_92021_u + 24;
		}

		// TODO Server info stuff
		if (!doCheckVersion) {
			return;
		}
		new Thread(LoadTools.serverInfoFetcher).start();
		doCheckVersion = false;

	}

	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who
	 * have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		if (Jigsaw.ghostMode) {
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_,
					I18n.format("menu.singleplayer", new Object[0])));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1,
					I18n.format("menu.multiplayer", new Object[0])));
			this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2,
					I18n.format("menu.online", new Object[0])));
		} else {
			this.buttonList.add(
					new GuiButton(1, 110 - 105, this.height - 25, 98, 20, I18n.format("menu.singleplayer", new Object[0])));
			this.buttonList.add(
					new GuiButton(2, 215 - 105, this.height - 25, 98, 20, I18n.format("menu.multiplayer", new Object[0])));
			// this.buttonList.add(this.realmsButton = new GuiButton(14,
			// this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2,
			// I18n.format("menu.online", new Object[0])));
		}
	}

	/**
	 * Adds Demo buttons on Main Menu for players who are playing Demo.
	 */
	private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
		this.buttonList
				.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
		this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1,
				I18n.format("menu.resetdemo", new Object[0])));
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

		if (worldinfo == null) {
			this.buttonResetDemo.enabled = false;
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
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
		if (button.id == 69) {
			this.mc.displayGuiScreen(new GuiJigsawAltLogin(this));
		}
		if (button.id == 1337) {
			this.mc.displayGuiScreen(new GuiJigsaw(this));
		}
		if (button.id == 420) {
			this.mc.displayGuiScreen(new GuiJigsawChangelog(this));
		}
		if (button.id == 421) {
			try {
				this.openWebLink(new URI("http://jigsawclient.weebly.com/submit-a-bug.html"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 422) {
			try {
				this.openWebLink(new URI("http://jigsawclient.weebly.com/suggestions.html"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 423) {
			this.mc.displayGuiScreen(new GuiJigsawCredits(this));
		}
		if (button.id == 424) {
			try {
				this.openWebLink(new URI("https://www.youtube.com/channel/UCtYz3npCasN6oMAWSX1_etQ"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 425) {
			this.mc.displayGuiScreen(new GuiJigsawAccHacker(this));
		}
		if (button.id == 426) {
			try {
				this.openWebLink(new URI("http://jigsawclient.weebly.com/infinite-reach.html"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 427) {
			mc.displayGuiScreen(new GuiMCLeaksRedeemToken(false));
		}
		if (button.id == 428) {
			mc.displayGuiScreen(new DisplayClickGui(true));
		}
		if (button.id == 429) {
			ClientSettings.mainMenuParticles = !ClientSettings.mainMenuParticles;
			anim.toggle(ClientSettings.mainMenuParticles);
			if (ClientSettings.mainMenuParticles) {
				for (int iii = 0; iii < particleCount; iii++) {
					double randomX = -2 + (2 - -2) * RANDOM.nextDouble();
					double randomY = -2 + (2 - -2) * RANDOM.nextDouble();
					double randomXm = -0 + (width - -0) * RANDOM.nextDouble();
					double randomYm = -0 + (height - -0) * RANDOM.nextDouble();
					double randomDepthm = RANDOM.nextDouble() + 0.1;
					int mX = 0;
					int mY = 0;
					MenuParticle part = new MenuParticle(randomXm + 0, randomYm + 0, randomDepthm + 0, true)
							.addMotion(randomX + mX / 4, randomY + mY / 4);
					part.alpha = 0.15f;
					particles.add(part);
				}
			} else {
				for (MenuParticle part : particles) {
					part.alphaDecay = true;
				}
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
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							new Object[] { new URI(this.openGLWarningLink) });
				} catch (Throwable throwable) {
					logger.error("Couldn\'t open link", throwable);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
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

		for (int j = 0; j < i * i; ++j) {
			GlStateManager.pushMatrix();
			float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
			float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F,
					1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k) {
				GlStateManager.pushMatrix();

				if (k == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				float f3 = 0.0F;
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
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
	private void rotateAndBlurSkybox(float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for (int j = 0; j < i; ++j) {
			float f = 1.0F / (float) (j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (float) (j - i / 2) / 256.0F;
			worldrenderer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
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
		float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	ArrayList<Object> modules = Jigsaw.filterModulesByCategory(Jigsaw.getModules(),
			Jigsaw.defaultCategories,
			ReturnType.MODULE);
	float wait = 0;
	int rand1 = 0;
	int rand2 = 0;

	Animation anim = new Animation() {
		public int getMaxTime() {
			return 9;
		};

		@Override
		public void render() {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1f, 1f, 1f, (float) time / 10.0f);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) 0, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) 0, 0.0D).endVertex();
			worldrenderer.pos((double) 0, (double) 0, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}

	};
	
	Animation anim2 = new Animation() {
		public int getMaxTime() {
			return 5;
		};

		@Override
		public void render() {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(0f, 0f, 0f, (float) time / 7);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) width - 10, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) 0, 0.0D).endVertex();
			worldrenderer.pos((double) width - 10, (double) 0, 0.0D).endVertex();
			tessellator.draw();
			
//			GlStateManager.color(1f, 1f, 1f, (float) time / 7);
//			worldrenderer.begin(4, DefaultVertexFormats.POSITION);
//			worldrenderer.pos((double) width - 9, (double) height / 2 + 4, 0.0D).endVertex();
//			worldrenderer.pos((double) width - 1, (double) height / 2, 0.0D).endVertex();
//			worldrenderer.pos((double) width - 9, (double) height / 2 - 4, 0.0D).endVertex();
//			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			if(this.time >= 4) {
				GlStateManager.pushMatrix();
				GlStateManager.rotate(-90, 0, 0, 1);
				drawCenteredString(fontRendererObj, "Next Image", -(height / 2), width - 9, 0xffffffff);
				GlStateManager.popMatrix();
			}
			
		}

	};
	
	int gifIndex = 0;
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		gifIndex += 1;
		if(gifIndex >= Jigsaw.gifLocations.size()) {
			gifIndex = 0;
		}
		
		anim.update();
		anim2.update();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		if (Jigsaw.ghostMode) {
			this.renderSkybox(mouseX, mouseY, partialTicks);
		}
		GlStateManager.enableAlpha();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = 30;
		// TODO Jigsaw flash sreen thing
		if (!(Jigsaw.ghostMode) && flashScreen) {
			if (wait <= 0) {
				rand2 = RANDOM.nextInt(Integer.MAX_VALUE) - Integer.MAX_VALUE;
				rand1 = RANDOM.nextInt(Integer.MAX_VALUE) - Integer.MAX_VALUE / rand2;
				this.drawGradientRect(0, 0, this.width, this.height, rand2, rand1);
				wait = 10f;

			} else {
				this.drawGradientRect(0, 0, this.width, this.height, rand2, rand1);
				wait -= partialTicks;
			}
			do3dEffects(mouseX, mouseY, partialTicks);
			drawRect(0, 0, width, height, 0x30000000);
		}
		if (Jigsaw.ghostMode) {
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		}
		if (!Jigsaw.ghostMode) {
			float xPlus = (((float)mouseX) - this.width / 2f) / 100f;
			float yPlus = (((float)mouseY) - this.height / 2f) / 100f;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.mc.getTextureManager().bindTexture(Jigsaw.images[ClientSettings.bgImage]);
			if(Jigsaw.gifMenu) {
				this.mc.getTextureManager().bindTexture(Jigsaw.gifLocations.get(gifIndex));
			}
			GlStateManager.translate(-xPlus / 2f, -yPlus / 2f, 0);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.scale(width * 0.0041, height * 0.0041, 0);
			this.drawTexturedModalRect(-10, -10, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.popMatrix();
			anim.render();
			if (!particles.isEmpty()) {
				GlStateManager.pushMatrix();
				// TODO Menu Particles
				for (MenuParticle particle : particles) {
					particle.update(preMouseX, preMouseY, particles);
					if (particle.alpha < 0.1f) {
						particle.remove = true;
					}
				}
				Iterator<MenuParticle> iter = particles.iterator();
				while (iter.hasNext()) {
					MenuParticle part = iter.next();
					if (part.remove) {
						iter.remove();
					}
				}
				GlStateManager.enableBlend();
				GlStateManager.disableTexture2D();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				boolean c = true;
				for (MenuParticle particle : particles) {
					GlStateManager.color(c ? 0f : 1f, c ? 0f : 1f, 
							c ? 0f : 1f, particle.alpha);
					double x = particle.x;
					double y = particle.y;
					
					worldrenderer.begin(7, DefaultVertexFormats.POSITION);
					worldrenderer.pos((double) x, (double) y + 1, 0.0D).endVertex();
					worldrenderer.pos((double) x + 0.5, (double) y + 1, 0.0D).endVertex();
					worldrenderer.pos((double) x + 0.5, (double) y, 0.0D).endVertex();
					worldrenderer.pos((double) x, (double) y, 0.0D).endVertex();
					tessellator.draw();
				}
				
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0.8f, 0.3f, 0.3f, 1f);
			this.mc.getTextureManager().bindTexture(Jigsaw.jigsawTexture512);

			this.drawTexturedModalRect(j + 12 + xPlus, this.height / 2 - 140 + yPlus, 0, 0, 512 / 2, 512 / 2);
			
			GL11.glColor4f(1f, 1f, 1f, 1f);
			
			this.drawTexturedModalRect(j + 10 + xPlus, this.height / 2 - 142 + yPlus, 0, 0, 512 / 2, 512 / 2);

			GlStateManager.popMatrix();

			this.drawGradientRect(0, 0, this.width, this.height, 0x1fcc7070, 0x1f7070cc);
			
			if(mouseX >= width - 10) {
				anim2.on();
				if(Mouse.isButtonDown(0)
						|| Mouse.isButtonDown(1)
						|| Mouse.isButtonDown(2)) {
					anim2.reset();
				}
			}
			else {
				anim2.off();
			}
		} else {
			this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			if ((double) this.updateCounter < 1.0E-4D) {
				this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
				this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
				this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
				this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
				this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
			} else {
				this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
				this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
			}
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
//		float f = 1.8F - MathHelper.abs(
//				MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
//		f = f * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
//		GlStateManager.scale(f, f, f);
//		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();
		String s;
		if (Jigsaw.ghostMode) {
			s = "Minecraft 1.8.8";
		} else {
			s = Jigsaw.getClientName() + " v" + Jigsaw.getClientVersion() + " / by: " + Jigsaw.getClientAuthor();
		}

		if (this.mc.isDemo()) {
			s = s + " Demo";
		}
		int offset = Jigsaw.ghostMode ? 10 : 37;
		this.drawString(this.fontRendererObj, s, 2, this.height - offset, -1);
		if (!Jigsaw.ghostMode && !Jigsaw.firstStart && Jigsaw.motd != null) {
			this.drawString(this.fontRendererObj, Jigsaw.motd,
					(this.width / 2) - this.fontRendererObj.getStringWidth(Jigsaw.motd) / 2, this.height - 15 - offset,
					-1);
		}
		if(Jigsaw.firstStart) {
			{
				String str = "Only download Jigsaw from our official websites (jigsawclient.ml or jigsawclient.weebly.com)";
				this.drawString(this.fontRendererObj, str,
						(this.width / 2) - this.fontRendererObj.getStringWidth(str) / 2, this.height - 25 - offset,
						-1);
			}
			{
				String str = "If you didn't, your computer might be infected with malware!";
				this.drawString(this.fontRendererObj, str,
						(this.width / 2) - this.fontRendererObj.getStringWidth(str) / 2, this.height - 15 - offset,
						-1);
			}
		}

		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,
				this.height - offset, -1);

		// TODO JIGSAW MODULE COUNT
		if (!Jigsaw.ghostMode) {
//			this.drawString(this.fontRendererObj,
//					Jigsaw.getClientName() + " currently has " + modules.size() + " usable hacks!", 5, 5, -1);
			drawString(fontRendererObj, "§6" + mc.session.getUsername(), 5, 59, 0xffffffff);
			
			try {
				String name = mc.session.getUsername();
				AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name)
						.loadTexture(Minecraft.getMinecraft().getResourceManager());
				Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
				GL11.glPushMatrix();
				glEnable(GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				// Face
				double x = 0;
				double y = 0;
				double h = 32;
				double w = 32;
				double fw = 20;
				double fh = 20;
				double u = 32;
				double v = 32;
				GlStateManager.translate(5, 5, 0);
				GlStateManager.scale(32 / fw, 32 / fw, 0);
				drawTexturedModalRect(x, y, u, v, w, h);
				// Hat
				u = 160;
				v = 32;
				drawTexturedModalRect(x, y, u, v, w, h);
				GL11.glPopMatrix();
				if(mouseX > 4 && mouseX < 56
						&& mouseY > 5 && mouseY < 57) {
					drawRect(5, 5, 56, 56, Mouse.isButtonDown(0) ? 0x50000000 : 0x40000000);
					accountInfo = true;
					if(Mouse.isButtonDown(0)) {
						mc.displayGuiScreen(new GuiJigsawAltManager(this));
						Mouse.destroy();
						Mouse.create();
					}
				}
				else {
					accountInfo = false;
				}
				glDisable(GL_BLEND);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(accountInfo || accountInfoOverride) {
				if(mc.session.getSessionType() == Type.LEGACY || mc.session.getUsername().isEmpty()) {
					drawString(fontRendererObj, "§7" + "Not Migrated", 5, 59 + 11, 0xffffffff);
				}
				else {
					drawString(fontRendererObj, "§7" + "Migrated (Mojang Account)", 5, 59 + 11, 0xffffffff);
				}
			}
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.5, 1.5, 0);
			drawString(fontRendererObj, "§b" + "Alt manager", (int)(58 / 1.5), (int)(5 / 1.5), 0xffffffff);
			GlStateManager.popMatrix();
			drawString(fontRendererObj, "§7" + "(Click the head)", 58, 19, 0xffffffff);
			
		}
		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1,
					1428160512);
			this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2,
					((GuiButton) this.buttonList.get(0)).yPosition - 12, -1);
		}
		if (!Jigsaw.ghostMode) {
			for (int ii = 0; ii < this.buttonList.size(); ++ii) {
				((GuiButton) this.buttonList.get(ii)).drawJigsawButton(this.mc, mouseX, mouseY);
			}

			for (int jj = 0; jj < this.labelList.size(); ++jj) {
				((GuiLabel) this.labelList.get(jj)).drawLabel(this.mc, mouseX, mouseY);
			}
		} else {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		this.preMouseX = mouseX;
		this.preMouseY = mouseY;
		anim2.render();
	}

	private void drawTexturedModalRect(double x, double y, double u, double v, double w, double h) {
		drawTexturedModalRect((int) x, (int) y, (int) u, (int) v, (int) w, (int) h);
	}

	private void do3dEffects(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glEnable(GL11.GL_DEPTH_TEST);
		glDisable(GL_TEXTURE_2D);
		GL11.glColor4d(1, 1, 1, 1);
		int randBig = mc.displayWidth * 2;
		int divider = randBig / 2;
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(RANDOM.nextInt(randBig) - divider, RANDOM.nextInt(randBig) - divider);
			glVertex2d(RANDOM.nextInt(randBig) - divider, RANDOM.nextInt(randBig) - divider);
		}
		glEnd();
		float f1 = 0.1f;
		float f2 = 0.5f;
		float f3 = 0.1f;

		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glDisable(GL11.GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		GlStateManager.popMatrix();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseX > 4 && mouseX < 56
				&& mouseY > 16 && mouseY < 68) {
			//accountInfoOverride = !accountInfoOverride;
		}
		if(mouseX >= width - 10) {
			ClientSettings.bgImage++;
			if(ClientSettings.bgImage >= Jigsaw.images.length) {
				ClientSettings.bgImage = 0;
			}
		}
		if (ClientSettings.mainMenuParticles) {
			for (MenuParticle part : particles) {
				float angle = (float) Math.toDegrees(Math.atan2(mouseY - part.y, mouseX - part.x));
				if (angle < 0) {
					angle += 360;
				}
				double xDist = mouseX - part.x;
				double yDist = mouseY - part.y;
				double dist = Math.sqrt(xDist * xDist + yDist * yDist);
				double mX = Math.cos(Math.toRadians(angle));
				double mY = Math.sin(Math.toRadians(angle));
				if (dist < 20) {
					dist = 20;
				}
				part.motionX -= mX * 200 / (dist / 2);
				part.motionY -= mY * 200 / (dist / 2);
			}
		}
		synchronized (this.threadLock) {
			if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v
					&& mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				this.mc.displayGuiScreen(guiconfirmopenlink);
			}
		}
	}

	public int preMouseX;
	public int preMouseY;
}
