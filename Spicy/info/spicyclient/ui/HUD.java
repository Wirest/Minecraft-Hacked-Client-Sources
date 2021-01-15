package info.spicyclient.ui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.EventType;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.fonts.FontManager;
import info.spicyclient.fonts.FontUtils;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.render.SkyColor;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.util.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class HUD {
	public boolean rainbowEnabled = false;
	public double rainbowTimer = 4;
	
	public String separator = " | ";
	
	public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean RainbowGUI = false;
	
	public static boolean ClickGUI = false;
	
	public static int primaryColor = -1, secondaryColor = 0x80ffffff;
	
	public void draw() {
		
		NotificationManager.getNotificationManager().onRender();
		
		ScaledResolution sr = new ScaledResolution(mc);
		FontRenderer fr = mc.fontRendererObj;
		
		float hue = System.currentTimeMillis() % (int)(rainbowTimer * 1000) / (float)(rainbowTimer * 1000);
		int primColor = Color.HSBtoRGB(hue, 0.45f, 1);
		int secColor = Color.HSBtoRGB(hue, 0.45f, 0.6f);
		
		if (rainbowEnabled) {
			primaryColor = primColor;
			secondaryColor = secColor;
		}
		
		// This is here so the modules don't move when you toggle them
		CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>(SpicyClient.modules);
		
		info.spicyclient.SpicyClient.modules.sort(Comparator.comparingInt(m -> mc.fontRendererObj.getStringWidth(((Module) m).name + (((Module)m).additionalInformation != "" ? ((Module)m).additionalInformation + separator : ""))).reversed());
		
		// HUD
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(4, 4, 0);
		GlStateManager.scale(2, 2, 1);
		GlStateManager.translate(-4, -4, 0);
		
		if (SpicyClient.config.clientVersion != SpicyClient.config.version) {
			
			//fr.drawStringWithShadow(SpicyClient.config.clientName + SpicyClient.config.clientVersion, 4, 4, primaryColor);
			//fr.drawStringWithQuadShadow(SpicyClient.config.clientName + SpicyClient.config.clientVersion, 4, 4, primaryColor, 0.3f);
			fr.drawStringWithQuadShadow(SpicyClient.config.clientName, 4, 4, primaryColor, 0.3f);
			
		}else {
			// We enable blending so there is a transparent background on the logo
			GlStateManager.enableBlend();
			GlStateManager.color(1, 1, 1);
			SkyColor skyColor = SpicyClient.config.skyColor;
			
			int maxBrightness = 200;
			if (skyColor.red.getValue() >= maxBrightness && skyColor.green.getValue() >= maxBrightness && skyColor.blue.getValue() >= maxBrightness && skyColor.isEnabled() && skyColor.RgbBrightness.getValue() >= 0.5) {
				mc.getTextureManager().bindTexture(new ResourceLocation("spicy/SpicyClientBlack.png"));
			}else {
				mc.getTextureManager().bindTexture(new ResourceLocation("spicy/SpicyClientWhite.png"));
			}
			int imageWidth = 500, imageHeight = 122;
			imageWidth /= 6;
			imageHeight /= 6;
			Gui.drawModalRectWithCustomSizedTexture(4, 2, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
			GlStateManager.pushMatrix();
			GlStateManager.translate(4, 4, 0);
			GlStateManager.scale(0.7, 0.7, 1);
			GlStateManager.translate(-4, -4, 0);
			if (SpicyClient.account.loggedIn) {
				fr.drawString(" - [ " + SpicyClient.account.username + " ]", 103f, 4f, -1, false);
			}
			GlStateManager.popMatrix();
		}
		
		if (!SpicyClient.config.hud.isEnabled()) {
			GlStateManager.popMatrix();
		}else {
			
			if (mc.currentScreen instanceof GuiChat) {
				
			}else {
				GlStateManager.translate(4, 4, 0);
				GlStateManager.scale(1.3, 1.3, 1);
				GlStateManager.translate(-4, -4, 0);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
				LocalDateTime now = LocalDateTime.now();
				
				String message = dtf.format(now);
				
				String[] times = message.split(":");
				
				if (Integer.valueOf(times[0]) >= 12 && Integer.valueOf(times[0]) < 24) {
					message = message.replaceAll("13:", "01:").replaceAll("14:", "02:").replaceAll("15:", "03:").replaceAll("16:", "04:").replaceAll("17:", "05:").replaceAll("18:", "06:").replaceAll("19:", "07:").replaceAll("20:", "08:").replaceAll("21:", "09:").replaceAll("22:", "10:").replaceAll("23:", "11:").replaceAll("24:", "12:");
					message += " PM";
				}
				else if (Integer.valueOf(times[0]) <= 0) {
					message = message.replaceAll("00:", "12:");
					message += " AM";
				}
				else if (Integer.valueOf(times[0]) <= 12) {
					message += " AM";
				}
				
				fr.drawString(message, 4,  (int) ((sr.getScaledHeight() - fr.FONT_HEIGHT) / 2.6 - 3), primaryColor);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				try {
					GlStateManager.scale(0.5, 0.5, 1);
					fr.drawString("Ping: " + mc.getNetHandler().getPlayerInfo((mc.thePlayer).getUniqueID()).responseTime + (SpicyClient.config.pingSpoof.isEnabled() ? " - [Spoofed]" : ""), 8,  (int) ((sr.getScaledHeight() - fr.FONT_HEIGHT) / 1.3 - 16), primaryColor);
					fr.drawString(decimalFormat.format(MovementUtils.getBlocksPerSecond()) + " blocks/sec", 8f,  (float) ((sr.getScaledHeight() - (fr.FONT_HEIGHT * 2)) / 1.3 - 18.75), primaryColor, false);
					GlStateManager.scale(2, 2, 1);
				} catch (NullPointerException e) {
					
				}
				
			}
			
			GlStateManager.popMatrix();
			
			int count = 0;
			
			for (Module m : info.spicyclient.SpicyClient.modules) {
				if (m.toggled) {
					
					double offset = count*(fr.FONT_HEIGHT + 2);
					
					
					
					if (m.additionalInformation != "") {
						Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation + separator) - 8, 0 + offset, sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation + separator) - 6, fr.FONT_HEIGHT + 2 + offset, primaryColor);
						Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation + separator) - 6, 0 + offset, sr.getScaledWidth(), fr.FONT_HEIGHT + 2 + offset, rainbowEnabled ? 0x0f000000 : 0x50000000);
						fr.drawString(m.name, sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation + separator) - 4, (int) (2 + offset), primaryColor);
						fr.drawString(separator, sr.getScaledWidth() - fr.getStringWidth(m.additionalInformation + separator) - 4, (int) (2 + offset), primaryColor);
						// fr.drawStringWithShadow("   " + m.additionalInformation, sr.getScaledWidth() - fr.getStringWidth(m.additionalInformation + separator) - 4, (float) (2 + offset), 0xff9c9c9c);
						fr.drawString(m.additionalInformation, sr.getScaledWidth() - fr.getStringWidth(m.additionalInformation) - 4, (int) (2 + offset), secondaryColor);
					}else {
						Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation) - 8, 0 + offset, sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation) - 6, fr.FONT_HEIGHT + 2 + offset, primaryColor);
						Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation) - 6, 0 + offset, sr.getScaledWidth(), fr.FONT_HEIGHT + 2 + offset, rainbowEnabled ? 0x0f000000 : 0x50000000);
						fr.drawString(m.name, sr.getScaledWidth() - fr.getStringWidth(m.name + m.additionalInformation) - 4, (int) (2 + offset), primaryColor);
					}
					
					double bottemLines = 0;
					
					ArrayList<Module> enabledModules = new ArrayList<Module>();
					for (Module e : SpicyClient.modules) {
						if (e.isEnabled()) {
							enabledModules.add(e);
						}
					}
					
					try {
						
						if (enabledModules.indexOf(m) != enabledModules.size() - 1) {
							bottemLines = (fr.getStringWidth(enabledModules.get(enabledModules.indexOf(m)).name)) - (fr.getStringWidth(enabledModules.get(enabledModules.indexOf(m) + 1).name + (enabledModules.get(enabledModules.indexOf(m) + 1).additionalInformation != "" ? enabledModules.get(enabledModules.indexOf(m) + 1).additionalInformation + separator : "")));
						}else {
							bottemLines = (fr.getStringWidth(enabledModules.get(enabledModules.indexOf(m)).name + (enabledModules.get(enabledModules.indexOf(m)).additionalInformation == "" ? enabledModules.get(enabledModules.indexOf(m)).additionalInformation + separator : ""))) + 10;
						}
						
					} catch (IndexOutOfBoundsException e) {
						// TODO: handle exception
					}
					
					Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name + (m.additionalInformation != "" ? m.additionalInformation + separator : "")) - 8, (0 + offset) + (fr.FONT_HEIGHT + 2), (sr.getScaledWidth() - fr.getStringWidth(m.name) - 6) + (bottemLines), (2 + offset) + (fr.FONT_HEIGHT + 2), primaryColor);
					//Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.name) - 8, 0 + offset, sr.getScaledWidth() - fr.getStringWidth(m.name) - 6, fr.FONT_HEIGHT + 2 + offset, primaryColor);
					
					count++;
					
					
				}
			}
			
		}
		
		// This is here so the modules don't move when you toggle them
		SpicyClient.modules = modules;
		
		EventRenderGUI event = new EventRenderGUI();
		event.setType(EventType.PRE);
		
		info.spicyclient.SpicyClient.onEvent(event);
		
	}
	
}
