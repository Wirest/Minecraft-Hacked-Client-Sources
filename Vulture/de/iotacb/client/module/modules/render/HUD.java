package de.iotacb.client.module.modules.render;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.minimap.XaeroMinimap;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.render.compass.Compass;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Cubic;
import de.iotacb.client.utilities.render.animations.easings.Elastic;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name = "HUD", description = "Draws the client HUD", category = Category.RENDER)
public class HUD extends Module {

	private AnimationUtil animUtil;
	
	private Compass compass;
	
	private XaeroMinimap minimap;
	
	private double time;
	
	private Value coloring;
	
	private final ResourceLocation VULTURE_OLD = new ResourceLocation("client/textures/logo.png"),
								   VULTURE_NEW_FILL = new ResourceLocation("client/textures/logo-fill.png"),
								   VULTURE_NEW_OUTLINE = new ResourceLocation("client/textures/logo-outline.png"),
								   KOEFTE = new ResourceLocation("client/textures/koefte.png");
	
	@Override
	public void onInit() {
		minimap = new XaeroMinimap();
		minimap.load();
		addValue(new Value("HUDDynamic", false));
		addValue(new Value("HUDNotifications", true));
		addValue(new Value("HUDLogo", "New", "Old", "Simple"));
		addValue(new Value("HUDInfo", false));
		addValue(new Value("HUDBackground", true));
		addValue(new Value("HUDKöftespieß", false));
		this.coloring = addValue(new Value("HUDColoring", "Color picker", "Rainbow", "Pulse"));
		addValue(new Value("HUDCompass", true));
		addValue(new Value("HUDMinimap", true));
		addValue(new Value("HUDBlur", true));
		addValue(new Value("HUDBetterHUD", true));
		addValue(new Value("HUDCrosshair", true));
		addValue(new Value("HUDRainbow speed", 10, new ValueMinMax(1, 50, 1)));
		addValue(new Value("HUDRainbow offset", 2, new ValueMinMax(1, 20, 1)));
		addValue(new Value("HUDBlur strength", 2, new ValueMinMax(1, 20, 1)));
		addValue(new Value("HUDFonts", "Verdana", "Kenyan", "Big Noodle", "Roboto", "Comfortaa", "Jetbrains"));
		
		this.animUtil = new AnimationUtil(Cubic.class);
		this.compass = new Compass();
		animUtil.addProgression(1337);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		animUtil.getProgression(1337).setValue(0);
		if (getMc().thePlayer == null || getMc().theWorld == null) return;
		Client.INSTANCE.getModuleManager().getModules().forEach(module -> {
			if (!module.isEnabled()) {
				module.getModuleProgressionX().setValue(1);
				module.getModuleProgressionY().setValue(1);
			} else {
				module.getModuleProgressionX().setValue(0);
				module.getModuleProgressionY().setValue(0);
			}
		});
	}
	
	private double prevRotationYaw, prevRotationPitch;
	private double yawDiff, pitchDiff;
	
	private double xOffset, yOffset;
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.TWOD) {
			
			final double distanceX = Math.abs(yawDiff - xOffset);
			final double stepX = (distanceX) / 2 * (Client.DELTA_UTIL.deltaTime * .2);
			
			final double distanceY = Math.abs(pitchDiff - yOffset);
			final double stepY = (distanceY) / 2 * (Client.DELTA_UTIL.deltaTime * .2);
			
			yawDiff = getMc().thePlayer.rotationYaw - prevRotationYaw;
			yawDiff = MathHelper.clamp_double(yawDiff, -20, 20);
			pitchDiff = getMc().thePlayer.rotationPitch - prevRotationPitch;
			pitchDiff = MathHelper.clamp_double(pitchDiff, -20, 20);
			
			if (xOffset < yawDiff) {
				if (yawDiff - xOffset >= stepX) {
					xOffset += stepX;
				} else {
					xOffset = yawDiff;
				}
			} else if (xOffset > yawDiff) {
				if (xOffset - yawDiff >= stepX) {
					xOffset -= stepX;
				} else {
					xOffset = yawDiff;
				}
			}
			
			if (yOffset < pitchDiff) {
				if (pitchDiff - yOffset >= stepY) {
					yOffset += stepY;
				} else {
					yOffset = pitchDiff;
				}
			} else if (yOffset > pitchDiff) {
				if (yOffset - pitchDiff >= stepY) {
					yOffset -= stepY;
				} else {
					yOffset = pitchDiff;
				}
			}
			if (getValueByName("HUDDynamic").getBooleanValue()) {
				GL11.glPushMatrix();
				
				if (Math.abs(xOffset) < .01)
					xOffset = 0;
				if (xOffset > 0) {
					xOffset *= 1 - (Client.DELTA_UTIL.deltaTime * .01);
				} else {
					xOffset *= 1 - (Client.DELTA_UTIL.deltaTime * .01);
				}
				Client.RENDER2D.translate(xOffset * -1, yOffset * -1);
			}
			
			if (getValueByName("HUDNotifications").getBooleanValue())
				Client.INSTANCE.getNotificationManager().draw(0);
			
			final double logoOffsetX = MathHelper.clamp_double(-120 + animUtil.easeOut(1337, 0, 120, 1), -120, 0);
			final double logoOffsetY = 10;
			time += Client.DELTA_UTIL.deltaTime * .002;
			
			switch (getValueByName("HUDLogo").getComboValue()) {
			case "New":
				Client.RENDER2D.push();
				Client.RENDER2D.translate(logoOffsetX + 10 + Math.cos(time), logoOffsetY + Math.sin(-time));
				Client.RENDER2D.scale(.3, .3);
				Client.RENDER2D.rotate(0, 0, 1, Math.sin(time));
				Client.RENDER2D.color(Client.INSTANCE.getClientColor());
				Client.RENDER2D.image(VULTURE_NEW_FILL, 0, 0, 420, 210);
				Client.RENDER2D.color(Color.white);
				Client.RENDER2D.image(VULTURE_NEW_OUTLINE, 0, 0, 420, 210);
				Client.RENDER2D.pop();
				break;

			case "Old":
				Client.RENDER2D.push();
				Client.RENDER2D.translate(logoOffsetX + 10 + Math.cos(time), logoOffsetY + Math.sin(-time));
				Client.RENDER2D.scale(.3, .3);
				Client.RENDER2D.rotate(0, 0, 1, Math.sin(time));
				Client.RENDER2D.color(Color.black);
				Client.RENDER2D.image(VULTURE_OLD, 5, 5, 420, 210);
				Client.RENDER2D.color(Color.white);
				Client.RENDER2D.image(VULTURE_OLD, 0, 0, 420, 210);
				Client.RENDER2D.pop();
				break;
				
			case "Simple":
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(Client.INSTANCE.getClientName() + " version: " + Client.INSTANCE.getClientColorCode() + Client.INSTANCE.getClientVersion().split(" ")[2], 2, 2, Color.white);
				break;
			}
			double yPos = (getValueByName("HUDBackground").getBooleanValue() ? 0 : 2);
			final double screenWidth = event.getSr().getScaledWidth();
			final List<Module> modules = Client.INSTANCE.getModuleManager().getModules();
			modules.sort(Comparator.comparingDouble(module -> -Client.INSTANCE.getFontManager().getDefaultFont().getWidth(module.getName() + module.getSettingInfo())));
			for (final Module module : modules) {
				if (module.getName().equalsIgnoreCase("clickgui") || !module.getValueByName(module.getName() + "Show in List").getBooleanValue()) continue;
				final double moduleWidth = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(module.getName() + module.getSettingInfo()) + (getValueByName("HUDBackground").getBooleanValue() ? 4 : 2) + 2;
				final double moduleHeight = Client.INSTANCE.getFontManager().getDefaultFont().getHeight(module.getName() + module.getSettingInfo()) + 2;
				if (module.isEnabled()) {
					module.setAnimX(moduleWidth - animUtil.easeOut(module.getModuleProgressionX(), 0, moduleWidth, .5));
					module.setAnimY(moduleHeight - animUtil.easeOut(module.getModuleProgressionY(), 0, moduleHeight, .5));
				} else {
					module.setAnimX(animUtil.easeOut(module.getModuleProgressionX(), 0, moduleWidth, .5));
					module.setAnimY(animUtil.easeOut(module.getModuleProgressionY(), 0, moduleHeight, .5));
				}
				module.setAnimX(MathHelper.clamp_double(module.getAnimX(), 0, moduleWidth));
				module.setAnimY(MathHelper.clamp_double(module.getAnimY(), 0, moduleHeight));
				if (module.isEnabled() || (module.getAnimX() != 0 && module.getAnimX() != moduleWidth)) {
					if (getValueByName("HUDBackground").getBooleanValue()) {
						Client.RENDER2D.rect(screenWidth - moduleWidth + module.getAnimX() - 1, yPos, moduleWidth, moduleHeight, new Color(20, 20, 20));
						Client.RENDER2D.rect(screenWidth + module.getAnimX() - (screenWidth < event.getSr().getScaledWidth() / 2 ? 0 : 2), yPos, 2, moduleHeight, coloring.getComboValue().equals("Rainbow") ? BetterColor.getRainbow(yPos) : coloring.getComboValue().equals("Pulse") ? Client.COLOR_UTIL.fade(Client.INSTANCE.getClientColor(), 100, modules.indexOf(module) * 2 + 10) : Client.INSTANCE.getClientColor());
					}
					if (getValueByName("HUDKöftespieß").getBooleanValue()) {
						Client.RENDER2D.image(KOEFTE, event.getSr().getScaledWidth() - moduleWidth + module.getAnimX() - 1, yPos, moduleWidth, moduleHeight);
					}
					final double offsetX = screenWidth < event.getSr().getScaledWidth() / 2 ? 2 : 0;
					Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(module.getName(), screenWidth - moduleWidth + module.getAnimX() + offsetX, yPos + (Client.INSTANCE.getClientFont().equalsIgnoreCase("kenyan") || Client.INSTANCE.getClientFont().equalsIgnoreCase("big noodle") || Client.INSTANCE.getClientFont().equalsIgnoreCase("comfortaa") ? 1 : 0), coloring.getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(yPos) : coloring.getComboValue().equalsIgnoreCase("Pulse") ? Client.COLOR_UTIL.fade(Client.INSTANCE.getClientColor(), 100, modules.indexOf(module) * 2 + 10) : Color.white);
					Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(module.getSettingInfo(), screenWidth - moduleWidth + Client.INSTANCE.getFontManager().getDefaultFont().getWidth(module.getName()) + module.getAnimX() + offsetX, yPos + (Client.INSTANCE.getClientFont().equalsIgnoreCase("kenyan") || Client.INSTANCE.getClientFont().equalsIgnoreCase("big noodle") || Client.INSTANCE.getClientFont().equalsIgnoreCase("comfortaa") ? 1 : 0), coloring.getComboValue().equalsIgnoreCase("Rainbow") || coloring.getComboValue().equalsIgnoreCase("Pulse") ? Color.white : Client.INSTANCE.getClientColor());
					yPos += moduleHeight - module.getAnimY();
				}
			}
			
			if (getValueByName("HUDInfo").getBooleanValue()) {
				drawArmor(event.getSr());
			}
			
			if (getValueByName("HUDCompass").getBooleanValue()) {
				drawCompass(event.getSr());
			}
			
			if (getValueByName("HUDBetterHUD").getBooleanValue()) {
				drawBetterHUD(event.getSr());
			}
			
			if (getValueByName("HUDCrosshair").getBooleanValue()) {
				drawCrosshair(event.getSr());
			}
			
			if (getValueByName("HUDDynamic").getBooleanValue()) {
				GL11.glPopMatrix();
			}
			prevRotationYaw = getMc().thePlayer.rotationYaw;
			prevRotationPitch = getMc().thePlayer.rotationPitch;
		}
	}
	
	@Override
	public void updateValueStates() {
		final boolean rainbow = coloring.getComboValue().equalsIgnoreCase("Rainbow");
		getValueByName("HUDRainbow speed").setEnabled(rainbow);
		getValueByName("HUDRainbow offset").setEnabled(rainbow);
		getValueByName("HUDBlur strength").setEnabled(getValueByName("HUDBlur").getBooleanValue());
		super.updateValueStates();
	}
	
	private void drawCompass(ScaledResolution sr) {
		compass.draw(sr, getMc().thePlayer.rotationYaw);
	}
	
	private void drawArmor(ScaledResolution sr) {
		double xOffset = 0;
		for (final ItemStack armor : getMc().thePlayer.inventory.armorInventory) {
			if (armor == null) continue;
			GlStateManager.enableDepth();
			getMc().getRenderItem().renderItemIntoGUI(armor, sr.getScaledWidth() / 2 + 12 + xOffset - (getValueByName("HUDBetterHUD").getBooleanValue() ? 50 : 0), sr.getScaledHeight() - 60 - (getValueByName("HUDBetterHUD").getBooleanValue() ? 35 : 0));
			GlStateManager.disableDepth();
			xOffset += 20;
		}
	}
	
	private void drawBetterHUD(ScaledResolution sr) {
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 50, 180, 10, new Color(20, 20, 20));
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 50, 180 * (getMc().thePlayer.getHealth() / getMc().thePlayer.getMaxHealth()), 10, new Color(250, 50, 50));
		final String health = String.format("%s | %s", Client.MATH_UTIL.roundSecondPlace(getMc().thePlayer.getHealth()), Client.MATH_UTIL.roundSecondPlace(getMc().thePlayer.getMaxHealth()));
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(health, sr.getScaledWidth() / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(health) / 2, sr.getScaledHeight() - 50, Color.white);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 61, 180, 10, new Color(20, 20, 20));
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 61, 180 * (getMc().thePlayer.getFoodStats().getFoodLevel() / 20.0), 10, new Color(130, 90, 60));
		final String food = String.format("%s | %s", Client.MATH_UTIL.roundSecondPlace(getMc().thePlayer.getFoodStats().getFoodLevel()), 20.0);
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(food, sr.getScaledWidth() / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(food) / 2, sr.getScaledHeight() - 61, Color.white);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 72, 180, 10, new Color(20, 20, 20));
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - 90, sr.getScaledHeight() - 72, 180 * (getMc().thePlayer.getTotalArmorValue() / 20.0), 10, new Color(200, 200, 200));
		final String armor = String.format("%s | %s", Client.MATH_UTIL.roundSecondPlace(getMc().thePlayer.getTotalArmorValue()), 20.0);
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(armor, sr.getScaledWidth() / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(armor) / 2, sr.getScaledHeight() - 72, Color.white);
	}
	
	double spacing;
	private void drawCrosshair(ScaledResolution sr) {
		spacing = AnimationUtil.slide(spacing, 0, 2, .1, Client.MOVEMENT_UTIL.isMoving() || !getMc().thePlayer.onGround);
		double offset = 6;
		final Color color = new Color(20, 20, 20);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - offset - spacing + .5, sr.getScaledHeight() / 2 + .5, 5, 1, color);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 - offset - spacing, sr.getScaledHeight() / 2, 5, 1, Client.INSTANCE.getClientColor());
		
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 + offset + spacing - 5 + 2, sr.getScaledHeight() / 2 + .5, 5, 1, color);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 + offset + spacing - 5 + 1.5, sr.getScaledHeight() / 2, 5, 1, Client.INSTANCE.getClientColor());
		
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 + .5, sr.getScaledHeight() / 2 - offset - spacing + .5, 1, 5, color);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - offset - spacing, 1, 5, Client.INSTANCE.getClientColor());
		
		Client.RENDER2D.rect(sr.getScaledWidth() / 2 + .5, sr.getScaledHeight() / 2 + offset + spacing - 3.5, 1, 5, color);
		Client.RENDER2D.rect(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + offset + spacing - 4, 1, 5, Client.INSTANCE.getClientColor());
	}
}
