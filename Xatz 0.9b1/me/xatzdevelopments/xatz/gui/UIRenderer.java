package me.xatzdevelopments.xatz.gui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.opengl.GL11;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.ColorCreator;
import me.xatzdevelopments.xatz.client.main.ReturnType;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class UIRenderer extends GuiScreen {
	ArrayList<String> objectqueue = new ArrayList<String>();
	ArrayList<ScreenPos> enumqueue = new ArrayList<ScreenPos>();
	public FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 19));
	boolean enabled = true;
	private Minecraft mc = Minecraft.getMinecraft();
	String toDraw = Xatz.getClientName() + " v" + Xatz.getClientmultiVersion();

	public UIRenderer() {
		this.mc = Minecraft.getMinecraft();
	}

	public void addToQueue(String s, ScreenPos pos) {
		objectqueue.add(s);
		enumqueue.add(pos);
	}

	public void tickQueues() {
		int i = 0;
		int offset = Xatz.getTabGui().rootContainer.y + Xatz.getTabGui().rootContainer.height;
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
	final ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	private void drawArmorStatus(final ScaledResolution scaledRes) {
		if (mc.playerController.isNotCreative()) {
            int x = 15;
            GL11.glPushMatrix();
            
            for (int index = 3; index >= 0; --index) {
                final ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
                if (stack != null) {
                    this.itemRender.renderItemAndEffectIntoGUI(stack, getWidth() / 2 + x, getHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55));
                    Minecraft.getMinecraft().getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, getWidth() / 2 + x, getHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55));
                    x += 18;
                }
            }
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.clear(256);
            GL11.glPopMatrix();
        }
	}
	
	public void render(boolean renderModules) {
		if(Xatz.getModuleByName("ArmorHUD").isToggled()) {
			this.drawArmorStatus(scaledRes);
		}
		
		if (!enabled) {
			return;
		}
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiManagerDisplayScreen && ClientSettings.clickGuiTint) {
			drawRect(0, 0, width, height, 0x6a000000);
		}
		if(Xatz.getTabGui() != null) {
			Xatz.getTabGui().render();
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(Xatz.xatzTextureTabGui);
			GlStateManager.popMatrix();
		}
		GlStateManager.enableBlend();
		if(ClientSettings.bigWaterMark) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.scale(0.425, 0.425, 1);
			GlStateManager.translate(4, -97, 0);
			mc.getTextureManager().bindTexture(Xatz.xatzTextureTabGui);
			GlStateManager.color(0.8f, 0.3f, 0.3f);
			drawTexturedModalRect(2, 2, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.color(1f, 1f, 1f);
			drawTexturedModalRect(0, 0, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.popMatrix();
		}
		else {
			GlStateManager.scale(1.5, 1.5, 1);
			int y = 1;
			mc.fontRendererObj.drawStringWithShadow("Xatz", 4 / 2, 5 / 2, ColorCreator.createRainbowFromOffset(3200, y * -15));
			GlStateManager.scale(1 / 1.5, 1 / 1.5, 1);
			mc.fontRendererObj.drawString(Xatz.getClientVersion(), 37.5, 2.5, ColorCreator.createRainbowFromOffset(3200, y * -15));
			
			
		}
		
		//mc.fontRendererObj.drawString(Xatz.getClientVersion(), 113, -14 + 20, 0xffdddddd, true);
		
//		mc.fontRendererObj.drawString("by: " + Xatz.getClientAuthor(), 4.5f, 33.5f, 0xffdd0f0f);
//		mc.fontRendererObj.drawString("by: " + Xatz.getClientAuthor(), 4, 33, 0xffffffff);
		if(mc.currentScreen == null) {
			if(Xatz.devVersion) {
				GlStateManager.translate(0, 0, 1000);
				mc.fontRendererObj.drawString("You are using a dev version of Xatz " + Xatz.getClientmultiVersion(), 5, height - 22, 0xffdddddd, true);
				mc.fontRendererObj.drawString("Please report bugs and crashes to Xatz discord admins!", 5, height - 11, 0xffdddddd, true);
				GlStateManager.translate(0, 0, -1000);
			}
			//else {
			//	GlStateManager.translate(0, 0, 1000);
			//	mc.fontRendererObj.drawString(Xatz.getClientmultiVersion(), 2, height - 10, 0xffdddddd, true);
			//	GlStateManager.translate(0, 0, -1000);
			//}
		}
		
		GlStateManager.translate(0, 5, 0);
		tickQueues();
		GlStateManager.translate(0, -5, 0);
		int y = 1;
		ArrayList<Object> modules = Xatz.filterModulesByCategory(Xatz.getToggledModules(),
				Xatz.defaultCategories,
				ReturnType.NAME);
		try {
			if (Xatz.java8) {
				modules.sort(new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						FontRenderer fr = mc.fontRendererObj;
						Module o1m = Xatz.getModuleByName((String) o1);
						Module o2m = Xatz.getModuleByName((String) o2);
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
			Xatz.java8 = false;
		}
		
		if (renderModules) {
			for (Object m : modules) {
				final int[] counter = {1};
				String name = (String) m;
				Module module = Xatz.getModuleByName(name);
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
				drawRect(width - 2 - toRenderWidth - 2, y - 1, width, y + 8, 0x90000000);
				drawRect(width - 2, y - 2, width, y + 8, ColorCreator.createRainbowFromOffset(3200, y * -15));
				mc.fontRendererObj.drawString(name,
						width - 2 - toRenderWidth, y, ColorCreator.createRainbowFromOffset(3200, y * -15));
				if (module.getAddonText() != null) {
					mc.fontRendererObj.drawStringWithShadow(addonText,
							width - 2 - toRenderWidth + nameWidth, y, 0x1e2099ba);
				}
				
				y += 9;
				counter[0]++;
			}
		}
		objectqueue.clear();
		enumqueue.clear();
		if (!Xatz.java8 && mc.currentScreen != null) {
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
