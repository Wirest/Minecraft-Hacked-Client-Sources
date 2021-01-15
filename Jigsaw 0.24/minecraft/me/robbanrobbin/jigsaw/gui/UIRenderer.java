package me.robbanrobbin.jigsaw.gui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.main.ReturnType;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class UIRenderer extends GuiScreen {
	ArrayList<String> objectqueue = new ArrayList<String>();
	ArrayList<ScreenPos> enumqueue = new ArrayList<ScreenPos>();
	public FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 19));
	boolean enabled = true;
	private Minecraft mc = Minecraft.getMinecraft();
	String toDraw = Jigsaw.getClientName() + " v" + Jigsaw.getClientVersion();

	public UIRenderer() {
		this.mc = Minecraft.getMinecraft();
	}

	public void addToQueue(String s, ScreenPos pos) {
		objectqueue.add(s);
		enumqueue.add(pos);
	}

	public void tickQueues() {
		int i = 0;
		int offset = Jigsaw.getTabGui().rootContainer.y + Jigsaw.getTabGui().rootContainer.height;
		if(!ClientSettings.tabGui) {
			offset = ClientSettings.bigWaterMark ? 28 : 15;
		}
		for (ScreenPos p : enumqueue) {
			addToScreen(objectqueue.get(i), p, offset);
			offset += 10;
			i++;
		}
	}
	
	public int getWidth() {
		return mc.displayWidth / mc.gameSettings.guiScale;
	}
	
	public int getHeight() {
		return mc.displayHeight / mc.gameSettings.guiScale;
	}

	public void render(boolean renderModules) {
		if (!enabled) {
			return;
		}
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiManagerDisplayScreen && ClientSettings.clickGuiTint) {
			drawRect(0, 0, width, height, 0x6a000000);
		}
		if(Jigsaw.getTabGui() != null) {
			Jigsaw.getTabGui().render();
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(Jigsaw.jigsawTexture512);
			GlStateManager.popMatrix();
		}
		GlStateManager.enableBlend();
		if(ClientSettings.bigWaterMark) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.scale(0.425, 0.425, 1);
			GlStateManager.translate(4, -97, 0);
			mc.getTextureManager().bindTexture(Jigsaw.jigsawTexture512);
			GlStateManager.color(0.8f, 0.3f, 0.3f);
			drawTexturedModalRect(2, 2, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.color(1f, 1f, 1f);
			drawTexturedModalRect(0, 0, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.popMatrix();
		}
		else {
			GlStateManager.scale(1.5, 1.5, 1);
			mc.fontRendererObj.drawString("Jigsaw", 4.5 / 2, 5.5 / 2, 0xff000000);
			mc.fontRendererObj.drawString("§cJig§8saw", 4 / 2, 5 / 2, 0xffffffff);
			GlStateManager.scale(1 / 1.5, 1 / 1.5, 1);
			mc.fontRendererObj.drawString("1.8", 52.5, 2.5, 0xff000000);
			mc.fontRendererObj.drawString("§71.8", 52, 2, 0xffffffff);
		}
		
		//mc.fontRendererObj.drawString(Jigsaw.getClientVersion(), 113, -14 + 20, 0xffdddddd, true);
		
//		mc.fontRendererObj.drawString("by: " + Jigsaw.getClientAuthor(), 4.5f, 33.5f, 0xffdd0f0f);
//		mc.fontRendererObj.drawString("by: " + Jigsaw.getClientAuthor(), 4, 33, 0xffffffff);
		if(mc.currentScreen == null) {
			if(Jigsaw.devVersion) {
				GlStateManager.translate(0, 0, 1000);
				mc.fontRendererObj.drawString("You are using a dev version of Jigsaw " + Jigsaw.getClientVersion(), 5, height - 22, 0xffdddddd, true);
				mc.fontRendererObj.drawString("Please report bugs and crashes to @ReachSaw on Twitter!", 5, height - 11, 0xffdddddd, true);
				GlStateManager.translate(0, 0, -1000);
			}
			else {
				GlStateManager.translate(0, 0, 1000);
				mc.fontRendererObj.drawString(Jigsaw.getClientVersion(), 2, height - 10, 0xffdddddd, true);
				GlStateManager.translate(0, 0, -1000);
			}
		}
		
		GlStateManager.translate(0, 5, 0);
		tickQueues();
		GlStateManager.translate(0, -5, 0);
		int y = 1;
		ArrayList<Object> modules = Jigsaw.filterModulesByCategory(Jigsaw.getToggledModules(),
				Jigsaw.defaultCategories,
				ReturnType.NAME);
		try {
			if (Jigsaw.java8) {
				modules.sort(new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						FontRenderer fr = mc.fontRendererObj;
						Module o1m = Jigsaw.getModuleByName((String) o1);
						Module o2m = Jigsaw.getModuleByName((String) o2);
						int o1w = 0;
						int o2w = 0;
						if (o1m.getAddonText() == null) {
							o1w = fr.getStringWidth(o1m.getName());
						} else {
							o1w = fr.getStringWidth(o1m.getName() + o1m.getAddonText() + " - []");
						}
						if (o2m.getAddonText() == null) {
							o2w = fr.getStringWidth(o2m.getName());
						} else {
							o2w = fr.getStringWidth(o2m.getName() + o2m.getAddonText() + " - []");
						}
						if (o1w > o2w) {
							return -1;
						}
						if (o1w < o2w) {
							return 1;
						}
						return 0;
					}
				});
			}
		} catch (NoSuchMethodError e) {
			Jigsaw.java8 = false;
		}
		
		if (renderModules) {
			for (Object m : modules) {
				String name = (String) m;
				Module module = Jigsaw.getModuleByName(name);
				String toRender;
				String addonText = " - [" + module.getAddonText() + "]";
				int nameWidth = mc.fontRendererObj.getStringWidth(name);
				int toRenderWidth;
				if (!(module.getAddonText() == null)) {
					toRender = name + addonText;
				} else {
					toRender = name;
				}
				toRenderWidth = mc.fontRendererObj.getStringWidth(toRender);
				drawRect(width - 2 - toRenderWidth - 2, y - 1, width, y + 8, 0xb0202020);
				mc.fontRendererObj.drawStringWithShadow(name,
						width - 2 - toRenderWidth, y, 0xfffababa);
				if (module.getAddonText() != null) {
					mc.fontRendererObj.drawStringWithShadow(addonText,
							width - 2 - toRenderWidth + nameWidth, y, 0xffcababa);
				}
				
				y += 9;
			}
		}
		objectqueue.clear();
		enumqueue.clear();
		if (!Jigsaw.java8 && mc.currentScreen != null) {
			GlStateManager.pushMatrix();
			String s = "You may have an old version of java: " + System.getProperty("java.version")
					+ ", please update to " + "Java 8!";
			mc.fontRendererObj.drawStringWithShadow(s,
					(width) / 2 - mc.fontRendererObj.getStringWidth(s) / 2, 2,
					0xffbbbbbb);
			GlStateManager.popMatrix();
		}
	}

	public void addToScreen(String text, ScreenPos pos, int offset) {

		if (pos == ScreenPos.LEFTUP) {
			mc.fontRendererObj.drawString(text, 4, offset, 0xffffffff);
		}
		if (pos == ScreenPos.RIGHTUP) {
			mc.fontRendererObj.drawString(text, 4, offset, 0xffffffff);
		}
	}

	public void setEnabled(boolean b) {

		enabled = b;

	}

	public boolean getUsePanicButton() {
		return false;
	}
}
