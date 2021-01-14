package splash.client.modules.visual;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.theme.type.FontType;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.font.FontChangeEvent;
import splash.client.events.key.EventKey;
import splash.client.events.render.EventRender2D;
import splash.client.events.scoreboard.EventScoreboard;
import splash.client.themes.astolfo.AstolfoTheme;
import splash.client.themes.category.CategoryTheme;
import splash.client.themes.clean.CleanTheme;
import splash.client.themes.helium.HeliumTheme;
import splash.client.themes.phoon.PhoonTheme;
import splash.client.themes.splash.SplashTheme;
import splash.client.themes.virtue.VirtueTheme;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Ice Created: 15:19, 31-May-20 Project: Client
 */
public class UI extends Module {
	int y = 0;

	public ModeValue<LogoMode> logo = new ModeValue<>("Logo", LogoMode.SPLASH, this);
	public ModeValue<ArraylistMode> arraylist = new ModeValue<>("Module List", ArraylistMode.SPLASH, this);
	public ModeValue<ArrayColor> arrayColor = new ModeValue<>("Array Color", ArrayColor.BLUE, this);
	public NumberValue<Integer> yValue = new NumberValue<>("Array Y Offset", 1, 1, 60, this);
	public ModeValue<TabGUIMode> tabgui = new ModeValue<>("TabGUI", TabGUIMode.OFF, this);
	public ModeValue<TabColor> tabguiColorValue = new ModeValue<>("TabGUI Color", TabColor.PULSE, this);
	// public ModeValue<Theme> clientThemeValue = new ModeValue<>("Theme",
	// Theme.SPLASH, this);
	public ModeValue<FontType> fontValue = new ModeValue<>("Font", FontType.CUSTOM, this);
	public ModeValue<FontMode> fontMode = new ModeValue<>("Font Mode", FontMode.ARIAL, this);
	public NumberValue<Integer> backgroundDarkness = new NumberValue<>("Background Opacity", 80, 0, 255, this);
	public NumberValue<Integer> redColorValue = new NumberValue<>("Red", 0, 1, 255, this);
	public NumberValue<Integer> greenColorValue = new NumberValue<>("Green", 255, 1, 255, this);
	public NumberValue<Integer> blueColorValue = new NumberValue<>("Blue", 226, 1, 255, this);
	public ModeValue<Case> arraylistCaseValue = new ModeValue<>("Case", Case.LOWERCASE, this);
	public BooleanValue<Boolean> notificationsValue = new BooleanValue<>("Notifications", true, this);
	public BooleanValue<Boolean> bandiCam = new BooleanValue<>("Bandicam Logo", false, this);
	public BooleanValue<Boolean> scoreboardValue = new BooleanValue<>("Scoreboard", true, this);
	public BooleanValue<Boolean> border = new BooleanValue<>("Border", false, this);
	public BooleanValue<Boolean> coords = new BooleanValue<>("Coords", true, this);
	public BooleanValue<Boolean> sideBar = new BooleanValue<>("Array Sidebar", true, this);
	public BooleanValue<Boolean> arrayBorder = new BooleanValue<>("Array Border", true, this);



	public static int color;
	
	
	public UI() {
		super("UI", "Shows user interface.", ModuleCategory.VISUALS);
		color = 3;
	}

	ScaledResolution scalRes;
	
	public enum Case {
		UPPERCASE, LOWERCASE;
	}

	public enum FontMode {
		ARIAL, SEGOEUI
	}
	
	public enum TabColor {
		PULSE, SOLID;
	}

	public enum LogoMode {
		SPLASH, VIRTUE, HELIUM, SPLASHBOX, SIGHT
	}
	
	public enum ArrayColor {
		SOLID, HELIUM, EXHIBITION, BLUE, RED, GREEN, PURPLE, CATEGORY
	}

	public enum ArraylistMode {
		SPLASH, VIRTUE, HELIUM, EXHIBITION, SOLID, CATEGORY, ASTOLFO
	}

	public enum TabGUIMode {
		NORMAL, OFF
	}

	public String getTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		Splash.getInstance().loadFontRenderer(fontMode.getValue());
	}
	
	private String lastFont = "";

	@Collect
	public void on2D(EventRender2D eventRender2D) {

		ScaledResolution scaledResolution = new ScaledResolution(mc);
		if (!this.fontMode.getValue().name().equalsIgnoreCase(lastFont)) {
			Splash.getInstance().loadFontRenderer(fontMode.getValue());
			this.lastFont = this.fontMode.getValue().name();
		}
		Splash.getInstance().getFontRenderer().updateFontType();
		Splash.getInstance().setClientColor(
				new Color(redColorValue.getValue(), greenColorValue.getValue(), blueColorValue.getValue()));
		Splash.getInstance().getNotificationManager().render();
		if (bandiCam.getValue()) {
			scalRes = new ScaledResolution(mc);
			RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/bandicam.png"),
					(scalRes.getScaledWidth() / 2) - 160, -20, 320, 60);
		}
		SplashTheme splashTheme = new SplashTheme();
		VirtueTheme virtueTheme = new VirtueTheme();
		HeliumTheme heliumTheme = new HeliumTheme();
		CleanTheme cleanTheme = new CleanTheme();
		CategoryTheme categoryTheme = new CategoryTheme();
		PhoonTheme phoonTheme = new PhoonTheme();
		AstolfoTheme astolfoTheme = new AstolfoTheme();

		switch (logo.getValue()) {
		case SPLASH:
			this.drawSplashLogo();
			break;

		case VIRTUE:
			this.drawVirtueWatermark();
			break;
		
		case SIGHT:
			this.drawSight();
			break;

		case HELIUM:
			this.drawHeliumLogo();
			break;

		case SPLASHBOX:
			this.drawSplashBoxLogo();
			break;

		}

		switch (arraylist.getValue()) {
		case SPLASH:
			splashTheme.drawArraylist();
			break;

		case VIRTUE:
			virtueTheme.drawArraylist();
			break;

		case HELIUM:
			heliumTheme.drawArraylist();
			break;
		case SOLID:
			cleanTheme.drawArraylist();
			break;

		case EXHIBITION:
			phoonTheme.drawArraylist();
			break;

		case CATEGORY:
			categoryTheme.drawArraylist();
			break;
			
		case ASTOLFO:
			astolfoTheme.drawArraylist();
			break;
			

		}

		switch (tabgui.getValue()) {
		case NORMAL:
			virtueTheme.drawTabGUI();
			break;
		case OFF:
			break;
		}

		if (border.getValue()) {
			drawBorder();
		}
		
		if (coords.getValue()) {
			if (mc.ingameGUI.getChatGUI().getChatOpen()) {
				Splash.getInstance().getFontRenderer().drawStringWithShadow(
						"XYZ: " + EnumChatFormatting.GRAY + Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", "
								+ Math.round(mc.thePlayer.posZ),
						2, scaledResolution.getScaledHeight() - Splash.getInstance().getFontRenderer().getFontHeightCustom() - 16,
						Splash.getInstance().getClientColor());

			} else {
				Splash.getInstance().getFontRenderer().drawStringWithShadow(
						"XYZ: " + EnumChatFormatting.GRAY + Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", "
								+ Math.round(mc.thePlayer.posZ),
								2, scaledResolution.getScaledHeight() - Splash.getInstance().getFontRenderer().getFontHeightCustom() - 5,
						Splash.getInstance().getClientColor());


			}
		}

	}

	@Collect
	public void onKey(EventKey eventKey) {
		VirtueTheme virtueTheme = new VirtueTheme();
		virtueTheme.onKey(eventKey.getPressedKey());
	}

	@Collect
	public void onFont(FontChangeEvent fontChangeEvent) {
		Splash.getInstance().loadFontRenderer(fontMode.getValue());
	}

	@Collect
	public void onScoreboard(EventScoreboard eventScoreboard) {
		if (!scoreboardValue.getValue()) {
			eventScoreboard.setCancelled(true);
		}
	}

	// LOGOS AND WATERMARKS
	public void drawSplashLogo() {
		ScaledResolution scaledRes = new ScaledResolution(mc);
		RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/splashPNGLogo.png"),
				(scaledRes.getScaledWidth() / 2000 - 5), -12, 960, 540);

	}

	public void drawVirtueWatermark() {
		Splash.getInstance().getFontRenderer().drawStringWithShadow(Splash.getInstance().getClientName() + " " + EnumChatFormatting.GRAY + getTime(), 4,
				2, -1);
	}
	
	public void drawSight() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("splash/sight.png"));
		GlStateManager.enableAlpha();
		Gui.drawModalRectWithCustomSizedTexture(-32, -22, 0, 0, 1280 / 8, 720 / 8, 1280 / 8, 720 / 8);
		GlStateManager.disableAlpha();
	}

	public void drawHeliumLogo() {
		Splash.getInstance().getFontRenderer().drawStringWithShadow(
				Splash.getInstance().getClientName().substring(0, 1) + EnumChatFormatting.GRAY + Splash.getInstance().getClientName().replace(Splash.getInstance().getClientName().substring(0, 1), ""), 6, 6,
				ColorUtilities.getRainbow(-6000, y * 35).getRGB());
	}

	public void drawSplashBoxLogo() {
		Gui.drawRect(5, 2, 75, 35, new Color(0, 0, 0, 120).getRGB());
		Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(Splash.getInstance().getClientName(), (float) 37.5, 10, -1);
		Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(getTime(), (float) 38, 20, -1);
	}

	public void drawBorder() {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		Gui.drawRect(scaledResolution.getScaledWidth() / 500, scaledResolution.getScaledHeight(), 0, 0,Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth() / 1.001, 0,
				Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight() / 500, 0, 0, Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 0, scaledResolution.getScaledHeight() / 1.003,
				Splash.INSTANCE.getClientColor());

	}

	public enum Theme {
		SPLASH, VIRTUE, HELIUM, PHOON, SIMPLE, CLEAN;
	}
}
