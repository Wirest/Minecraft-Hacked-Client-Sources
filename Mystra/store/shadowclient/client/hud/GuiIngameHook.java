package store.shadowclient.client.hud;

import java.awt.Color;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.animate.AnimationUtil;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiIngameHook extends GuiIngame {
	public GuiIngameHook(Minecraft mcIn) {
		super(mcIn);
	}

	private final static ResourceLocation MYSTRA = new ResourceLocation("client/Mystra32x32.png");
	AtomicInteger nameoftheoffset = new AtomicInteger(12);
	private static AnimationUtil animUtil;
	private static double time;

	public static FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
	//fr = font on a wrapper

	public static void HUDArrayList() {
		TTFFontRenderer normal = Shadow.instance.fontManager.getFont("SFB 8");
		String mode = Shadow.instance.settingsManager.getSettingByName("ArrayLists").getValString();
		
			if(mode.equalsIgnoreCase("New")) {
				final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
				final int[] yDist = {2};
				final int[] counter = {1};
				int count = 0;
				
				double offset = count*(font.FONT_HEIGHT + 6);
				AtomicInteger arrayoffset = new AtomicInteger(3);
				
				for(Module m: Shadow.instance.moduleManager.getModules()) {
					m.isToggled();
					if(m.isToggled()) {
						drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 8, offset, sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, 4 + font.FONT_HEIGHT + offset, rainbow(counter[0] * 300));
						drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, offset, sr.getScaledWidth(), 4 + font.FONT_HEIGHT + offset, 0xff121212);
						count++;
					}
				}
				Shadow.instance.moduleManager.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingInt(module -> -font.getStringWidth(module.getDisplayName()))).forEach(module -> {
					Gui.drawRect(sr.getScaledWidth() + font.getStringWidth(module.getDisplayName()), arrayoffset.get() - 3, sr.getScaledWidth() -font.getStringWidth(module.getDisplayName()) - 7, arrayoffset.get() + 10, rainbow(counter[0] * 300));
					Gui.drawRect(sr.getScaledWidth() - font.getStringWidth(module.getDisplayName()) - 5, arrayoffset.get() - 3, sr.getScaledWidth() - 1, arrayoffset.get() + 8, 0xff121212);
					Gui.drawRect(sr.getScaledWidth() - 1, arrayoffset.get() - 3, sr.getScaledWidth(), arrayoffset.get() + 8, rainbow(counter[0] * 300));
					//drawRect(sr.getScaledWidth() - font.getStringWidth(module.getName()) - 8, offset, sr.getScaledWidth() - font.getStringWidth(module.getName()) - /*Linea De Alado*/ 6,  /*Linea De Alado*/ 4 + font.FONT_HEIGHT + offset, rainbow(counter[0] * 300));
					//drawRect(sr.getScaledWidth() - font.getStringWidth(module.getName()) - 6, offset, sr.getScaledWidth(), /*Linea De Abajo*/ 4 + font.FONT_HEIGHT + offset, 0xff121212);
					font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3 - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
					font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3.2F - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
					//font.drawString(module.getDisplayName(), sr.getScaledWidth() - 2 - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300), true);
					yDist[0] += font.FONT_HEIGHT + 2;
					arrayoffset.addAndGet(font.FONT_HEIGHT + 2);
					counter[0]++;
				});
			} 
			
		if(mode.equalsIgnoreCase("Old")) {
			final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
			final int[] yDist = {2};
			final int[] counter = {1};
			int count = 0;
			double offset = count*(font.FONT_HEIGHT + 6);
			AtomicInteger arrayoffset = new AtomicInteger(3);
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				m.isToggled();
				if(m.isToggled()) {
					/*drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 8, offset, sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, 4 + font.FONT_HEIGHT + offset, rainbow(counter[0] * 300));
					drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, offset, sr.getScaledWidth(), 4 + font.FONT_HEIGHT + offset, 0xff121212);*/
					count++;
				}
			}
			Shadow.instance.moduleManager.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingInt(module -> -font.getStringWidth(module.getDisplayName()))).forEach(module -> {
			Gui.drawRect(sr.getScaledWidth() - font.getStringWidth(module.getDisplayName()) - 5, arrayoffset.get() - 3, sr.getScaledWidth() - 1, arrayoffset.get() + 8, 0xff121212);
			Gui.drawRect(sr.getScaledWidth() - 1, arrayoffset.get() - 3, sr.getScaledWidth(), arrayoffset.get() + 8, rainbow(counter[0] * 300));
			//drawRect(sr.getScaledWidth() - Wrapper.fr.getStringWidth(module.getName()) - 8, offset, sr.getScaledWidth() - Wrapper.fr.getStringWidth(module.getName()) - /*Linea De Alado*/ 6,  /*Linea De Alado*/ 4 + Wrapper.fr.FONT_HEIGHT + offset, Rainbow(counter[0] * 300));
			//drawRecyt(sr.getScaledWidth() - Wrapper.fr.getStringWidth(module.getName()) - 6, offset, sr.getScaledWidth(), /*Linea De Abajo*/ 4 + Wrapper.fr.FONT_HEIGHT + offset, 0xff121212);
			font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3 - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
			font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3.2F - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
			//font.drawString(module.getDisplayName(), sr.getScaledWidth() - 2 - font.getStringWidth(module.getDisplayName()), yDist[0], Rainbow(counter[0] * 300), true);
			yDist[0] += font.FONT_HEIGHT + 2;
			arrayoffset.addAndGet(font.FONT_HEIGHT + 2);
			counter[0]++;
			});
		}
		
		if(mode.equalsIgnoreCase("Novoline")) {
			final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
			final int[] yDist = {2};
			final int[] counter = {1};
			int count = 0;
			double offset = count*(font.FONT_HEIGHT + 6);
			AtomicInteger arrayoffset = new AtomicInteger(3);
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				m.isToggled();
				if(m.isToggled()) {
					/*drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 8, offset, sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, 4 + font.FONT_HEIGHT + offset, rainbow(counter[0] * 300));
					drawRect(sr.getScaledWidth() - font.getStringWidth(m.getDisplayName()) - 6, offset, sr.getScaledWidth(), 4 + font.FONT_HEIGHT + offset, 0xff121212);*/
					count++;
				}
			}
			Shadow.instance.moduleManager.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingInt(module -> -font.getStringWidth(module.getDisplayName()))).forEach(module -> {
				Gui.drawRect(sr.getScaledWidth() - font.getStringWidth(module.getDisplayName()) - 6, arrayoffset.get() - 3, sr.getScaledWidth() - 1, arrayoffset.get() + 8, 0x55232323);
				Gui.drawRect(sr.getScaledWidth() - 1, arrayoffset.get() - 3, sr.getScaledWidth(), arrayoffset.get() + 8, novoline(counter[0] * 300));
				Gui.drawRect(sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getDisplayName()) - 6, arrayoffset.get() - 3, sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getDisplayName()) - 7, arrayoffset.get() + 8, novoline(counter[0] * 300));
				if(Shadow.instance.settingsManager.getSettingByName("ArrayListFont").getValBoolean()) {
					Shadow.fontManager.getFont("SFB 12").drawString(module.getDisplayName(), sr.getScaledWidth() - 3 - font.getStringWidth(module.getDisplayName()), yDist[0] - 1, novoline(counter[0] * 300));
					Shadow.fontManager.getFont("SFB 12").drawString(module.getDisplayName(), sr.getScaledWidth() - 3.2F - font.getStringWidth(module.getDisplayName()), yDist[0] - 1, novoline(counter[0] * 300));
				} else {
					font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3 - font.getStringWidth(module.getDisplayName()), yDist[0] - 1, novoline(counter[0] * 300));
					font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3.2F - font.getStringWidth(module.getDisplayName()), yDist[0] - 1, novoline(counter[0] * 300));
				}
				yDist[0] += font.FONT_HEIGHT + 2;
				arrayoffset.addAndGet(font.FONT_HEIGHT + 2);
				counter[0]++;
			});
		}
		
		if(mode.equalsIgnoreCase("Old2")) {
			final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
			final int[] yDist = {2};
			final int[] counter = {1};
			int count = 0;
			
			double offset = count*(font.FONT_HEIGHT + 6);
			AtomicInteger arrayoffset = new AtomicInteger(3);
			
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				m.isToggled();
				if(m.isToggled()) {
					count++;
				}
			}
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				m.isToggled();
				if(m.isToggled()) {
					count++;
				}
			}
			Shadow.instance.moduleManager.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingInt(module -> -font.getStringWidth(module.getDisplayName()))).forEach(module -> {
				Gui.drawRect(sr.getScaledWidth() - font.getStringWidth(module.getDisplayName()) - 5, arrayoffset.get() - 3, sr.getScaledWidth() - 1, arrayoffset.get() + 8, 0x99232323);
				Gui.drawRect(sr.getScaledWidth() - 1, arrayoffset.get() - 3, sr.getScaledWidth(), arrayoffset.get() + 8, rainbow(counter[0] * 300));
				Gui.drawRect(sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getDisplayName()) - 5, arrayoffset.get() - 3, sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getDisplayName()) - 7, arrayoffset.get() + 8, rainbow(counter[0] * 300));
				font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3 - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
				font.drawString(module.getDisplayName(), sr.getScaledWidth() - 3.2F - font.getStringWidth(module.getDisplayName()), yDist[0], rainbow(counter[0] * 300));
				yDist[0] += font.FONT_HEIGHT + 2;
				arrayoffset.addAndGet(font.FONT_HEIGHT + 2);
				counter[0]++;
			});
		}
	}
	
	

	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 50.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}
	
	public static void name() {
		String themeadv = Shadow.instance.settingsManager.getSettingByName("Theme").getValString();
		int counter = 0;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if(Shadow.instance.settingsManager.getSettingByName("Watermark").getValBoolean()) {
        	if(themeadv.equalsIgnoreCase("Shadow")) {
        		//Shadow.instance.fontManager.getFont("SF-UI-Display-Bold").drawStringWithMystra("ï¿½lMystra ï¿½rï¿½7ï¿½lBï¿½oï¿½l1", 10.0f, 3.0f, rainbow(counter*300));
        		Shadow.instance.fontManager.getFont("SFB 10").drawStringWithShadow(Shadow.instance.uiname.substring(0), 12.0f, 3.0f, rainbow(counter*300));
        	}
        	
        	if(themeadv.equalsIgnoreCase("Cheese")) {
        		Shadow.instance.fontManager.getFont("SFB 10").drawStringWithShadow("§eCheezy Boi", 7, 12 - 10, rainbow(counter*300));
        	}
        //("ï¿½rï¿½lï¿½oMï¿½rï¿½lystra ï¿½rï¿½7ï¿½lBï¿½oï¿½l1", 10, 12 - 10, rainbow(counter));
        
        counter++;
        }
    }
}