package info.sigmaclient.module.impl.hud;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRenderHUD;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.WinterParticle;
import info.sigmaclient.management.agora.GuiAgoraIngame;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.other.ChatCommands;
import info.sigmaclient.module.impl.other.ClickGui;
import info.sigmaclient.module.impl.player.FastPlace;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.misc.Timer;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.RenderUtilities;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

public class Enabled extends Module {
	private final String POTIONS = "POTIONS";
	private final String SUFFIX = "SUFFIX";
	public static final String MODE = "MODE";
	public final static String ArraylistColorMode = "COLORMODE";
	private final String FONTMODE = "FONTMODE";
	private final String ANIMATION = "ANIMATION";
	private final String KeyStroke = "KEYSTROKES";
	public final static String ALERTS = "ALERTS";
	public static final String CONTAINER = "CONTAINER";
	private final String BRIGHTNESS = "BRIGHTNESS";
	private final String SPEED = "SPEED";
	private final String WIDTH = "WIDTH";
	private final ResourceLocation jigsaw = new ResourceLocation("textures/jigsaw.png");
	String animMode = "";
	Timer timer = new Timer();
	ArrayList<WinterParticle> particles = new ArrayList<>();
	public Enabled(ModuleData data) {
		super(data);
		settings.put(BRIGHTNESS, new Setting<>(BRIGHTNESS, 0.6, "Brightness of the rainbow.", 0.05, 0.25, 1));
		settings.put(SPEED, new Setting<>(SPEED, 3, "Speed of the rainbow.", 0.05, 1, 15));
		settings.put(WIDTH, new Setting<>(WIDTH, 10, "Width of the rainbow.", 0.05, 1, 20));
		settings.put(POTIONS, new Setting<>(POTIONS, true, "Show active potion effects."));
		settings.put(SUFFIX, new Setting<>(SUFFIX, true, "Show modules suffix in arraylist."));
		settings.put(ArraylistColorMode, new Setting<>(ArraylistColorMode, new Options("Color", "Rainbow",
				new String[] { "Rainbow", "Random", "Default", "Category"}), "Arraylist color mode."));
		settings.put(FONTMODE, new Setting<>(FONTMODE, new Options("Font", "Sigma",
				new String[] { "Sigma", "Vanilla"}), "Arraylist font."));
		settings.put(MODE, new Setting<>(MODE, new Options("HUD Mode", "Sigma",
				new String[] { "Sigma", "Jigsaw"}), "HUD mode."));
		settings.put(ANIMATION, new Setting<>(ANIMATION, new Options("Animation", "Smooth",
				new String[] { "Slide", "Smooth"}), "Arraylist animation."));
		settings.put(KeyStroke, new Setting<>(KeyStroke, false, "Show KeyStrokes."));
		settings.put(ALERTS, new Setting<>(ALERTS, true, "Alerts you when someone calls you a hacker."));
		settings.put(CONTAINER, new Setting<>(CONTAINER, true, "Smooth animation with containers."));

	}

	private Opacity hue = new Opacity(0);

	@Override
	@RegisterEvent(events = {EventRenderHUD.class})
	public void onEvent(Event event) {
		if(event instanceof EventRenderHUD){
		DecimalFormat df = new DecimalFormat("0.0000");
		String speed = df.format(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
		//mc.fontRendererObj.drawStringWithShadow(speed, 2, 170, Colors.getColor(200));
		if (mc.gameSettings.showDebugInfo) {
			return;
		}
		EventRenderHUD e = (EventRenderHUD) event;
		if((Boolean) settings.get(POTIONS).getValue())
		drawPotionStatus(e.getResolution());
		String colorMode = (((Options) settings.get(ArraylistColorMode).getValue()).getSelected());
		String fontMode = (((Options) settings.get(FONTMODE).getValue()).getSelected());
		String HUD = (((Options) settings.get(MODE).getValue()).getSelected());
		if((((Options) settings.get(ANIMATION).getValue()).getSelected()).equalsIgnoreCase("Slide") && animMode.equalsIgnoreCase("Smooth")){
			for (Module module : Client.getModuleManager().getArray()) {
				module.setEnableTime(0);
			}
		}else 	if((((Options) settings.get(ANIMATION).getValue()).getSelected()).equalsIgnoreCase("Smooth") && animMode.equalsIgnoreCase("Slide")){
			for (Module module : Client.getModuleManager().getArray()) {
				module.setTranslate(0, 0, 100);
			}
		}
		animMode = (((Options) settings.get(ANIMATION).getValue()).getSelected());
		boolean keystroke = (Boolean) settings.get(KeyStroke).getValue();
		if (keystroke) {
			RenderUtilities.drawLeakedPvPKeyStrokes();
		}
		switch(HUD){
		case"Sigma":
			drawLigmaHUD(e, colorMode);
			break;		
		case"Jigsaw":
			drawJigsawHUD(e);
			break;
		}
		drawArraylist(e, colorMode, fontMode, HUD);
		} 
		
	}
	//TODO
	void drawLigmaHUD(EventRenderHUD e, String colorMode){
		boolean t = Client.getModuleManager().isEnabled(TabGUI.class);
		float offset = Client.fm.getFont("SFR 11").getWidth(Client.clientName);
		RenderingUtil.rectangle(2, 1, 55, 14, Colors.getColor(0, t ? (int)TabGUI.opacity.getX() : 200)); 	
		Client.fm.getFont("SFB 11").drawStringWithShadow(Client.clientName, 3, 4,
				Colors.getColor(255, t ? (int)TabGUI.opacity.getX() + 64 : 232));
		Client.fm.getFont("SFB 7").drawStringWithShadow("v4.11", 3f + offset, 3.5F,
				colorMode.equalsIgnoreCase("Rainbow") ? Color.getHSBColor(hue.getOpacity() / 255.0f, 0.6f, 1f).getRGB()
						: Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green,
								ColorManager.hudColor.blue, t ? (int)TabGUI.opacity.getX() + 64 : 255));   
		
	}
	void drawJigsawHUD(EventRenderHUD e){
		mc.fontRendererObj.drawStringWithShadow("4.11", 100, 1, Colors.getColor(200));
		GL11.glColor4d(1, 1, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		mc.getTextureManager().bindTexture(jigsaw);
		drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 100, 33.3f, 100, 33.3f);
		GlStateManager.disableBlend();
	}
	void drawArraylist(EventRenderHUD e, String colorMode, String fontMode, String hud){
		int y = 4;
		if(hud.equalsIgnoreCase("Jigsaw")){
			y = 2;
		}
		List<Module> modules = new CopyOnWriteArrayList<>();
		TTFFontRenderer sigmaFont = Client.fm.getFont("SFB 8");
		TTFFontRenderer jigsawFont = Client.fm.getFont("JIGR 19");
		FontRenderer vanillaFont = mc.fontRendererObj;
		
		float h = hue.getOpacity();
		for (Module module : Client.getModuleManager().getArray()) {
		switch (animMode){
			case "Slide":
				if(module.isEnabled()){
					if(module.getEnableTime() < 700){
						module.setEnableTime(module.getEnableTime() + 25);
					}
				}else{
					if(module.getEnableTime() > 0){
						module.setEnableTime(module.getEnableTime() - 30);
					}else if(module.getEnableTime() < 0){
						module.setEnableTime(0);				
					}
				}
				if (module.getEnableTime() > 0 && !shouldHide(module)) {
					modules.add(module);
				}
			break;
			case "Smooth":
				if(module.isEnabled()){
					
					module.setTranslate(0, 10, 10);
				}else{
					module.setTranslate(0, 0, 10);
				}
				module.setEnableTime(700);
				if(!shouldHide(module) && module.getTranslate().getY() > 0){
						
					modules.add(module);
				}
			break;
			}
		}
		switch(hud){
		case"Sigma":
			switch(fontMode){
			case"Sigma":
				modules.sort(Comparator.comparingDouble(
						m -> -sigmaFont.getWidth(m.getSuffix() != null && (Boolean)settings.get(SUFFIX).getValue() ? m.getName() + " " + m.getSuffix() : m.getName())));
				
				break;
			case"Vanilla":
				modules.sort(Comparator.comparingDouble(
						m -> -vanillaFont.getStringWidth(m.getSuffix() != null && (Boolean)settings.get(SUFFIX).getValue() ? m.getName() + " " + m.getSuffix() : m.getName())));
				
				break;
				
			default:
				break; 
			}
			break;
		case"Jigsaw":
			modules.sort(Comparator.comparingDouble(
					m -> -jigsawFont.getWidth(m.getSuffix() != null && (Boolean)settings.get(SUFFIX).getValue() ? m.getName() + " " + m.getSuffix() : m.getName())));
			
			break;
		}

		hue.interp(256, ((Number) settings.get(SPEED).getValue()).doubleValue() - 1);
		if (hue.getOpacity() > 255) {
			hue.setOpacity(0);
		}
		
		float lastWidth = e.getResolution().getScaledWidth();
		for (Module module : modules) {
			if (h > 255) {
				h = 0;
			}
			
			String suffix = module.getSuffix() != null && (Boolean)settings.get(SUFFIX).getValue() ? " " + module.getSuffix() : "";
			float x = e.getResolution().getScaledWidth()-
					(float)module.getEnableTime()/100
					* sigmaFont.getWidth(module.getName()+suffix)/7;
			float maxXValue = e.getResolution().getScaledWidth() - sigmaFont.getWidth(module.getName() + suffix);
			if(fontMode.equalsIgnoreCase("Vanilla")){
				x = e.getResolution().getScaledWidth()-
						(float)module.getEnableTime()/100
						* (float)vanillaFont.getStringWidth(module.getName()+suffix) / 7;
				maxXValue = suffix == "" ? e.getResolution().getScaledWidth() - (float)vanillaFont.getStringWidth(module.getName() + suffix) - 1
						:e.getResolution().getScaledWidth() - (float)vanillaFont.getStringWidth(module.getName() + suffix) +1;
			}
			if(hud.equalsIgnoreCase("Jigsaw")){
				x = e.getResolution().getScaledWidth()-
						(float)module.getEnableTime()/100
						* jigsawFont.getWidth(module.getName()+suffix)/7;
				maxXValue = e.getResolution().getScaledWidth() - jigsawFont.getWidth(module.getName() + suffix);
			}
			if( x < maxXValue){
				x = maxXValue;
			}
			
			float xres = e.getResolution().getScaledWidth();
	        float yres = e.getResolution().getScaledHeight();
	        int s = e.getResolution().getScaleFactor();
	        if(animMode.equalsIgnoreCase("Smooth")){
	        	glPushMatrix();
				glScissor(0,(int)(yres - (y + module.getTranslate().getY())) * s +3, (int)xres * s, (int)(module.getTranslate().getY()+4)*s);
		        glEnable(GL_SCISSOR_TEST);
	        }
			switch(hud){
			case"Sigma":
				switch (fontMode) {
				case"Sigma":
					RenderingUtil.rectangle(x - 1, y - 4.3, e.getResolution().getScaledWidth(), y + 5.7,
							Colors.getColor(0, 160));
					break;
				case"Vanilla":
					RenderingUtil.rectangle(x - 2, y - 4.3, e.getResolution().getScaledWidth(), y + 6.7,
							Colors.getColor(0, 160));
					break;
				}
				break;
			case"Jigsaw":
				RenderingUtil.rectangle(x - 1, y - 2, e.getResolution().getScaledWidth(), y + 8,
						Colors.getColor(0, 190));
				break;
			}
			
			
			int color = 0;
			switch(hud){
			case"Sigma":
				switch (colorMode) {
				case "Default":
					color = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green,
							ColorManager.hudColor.blue, 255);
					switch(fontMode){
					case"Sigma":
						RenderingUtil.rectangle(x-2, y-4.3, x-1, y+6.7, color);
						if(module != modules.get(0))
							RenderingUtil.rectangle(x-2, y-3.3, lastWidth-1, y-4.3, color);
						if(module == modules.get(modules.size() - 1))
							RenderingUtil.rectangle(x-1, y+5.5, e.getResolution().getScaledWidth(), y+6.7, color);
						sigmaFont.drawStringWithShadow(module.getName(), x, y - 1,  color);
						break;
					case"Vanilla":
						if(x <= lastWidth && modules.get(0) != module)	
							RenderingUtil.rectangle(x-3, y-3.3, x-2, y+7.7, color);
						else
							RenderingUtil.rectangle(x-3, y-4.3, x-2, y+7.7, color);
						
						if(module != modules.get(0) && x!= lastWidth)
							RenderingUtil.rectangle(lastWidth-2, y-3.3,x - 3 , y-4.3, color);
						if(module == modules.get(modules.size() - 1))
							RenderingUtil.rectangle(x - 2, y+6.5, e.getResolution().getScaledWidth(), y+7.7, color);
						vanillaFont.drawStringWithShadow(module.getName(), x-0.5f, y - 3,  color);
						break;
					}

					break;
					
				case "Rainbow":
					final Color rainB = Color.getHSBColor(h / 255.0f, ((Number) settings.get(BRIGHTNESS).getValue()).floatValue(), 1.0f);
					color = rainB.getRGB();
					switch(fontMode){
					case"Sigma":
						RenderingUtil.rectangle(x-2, y-4.3, x-1, y+6.7, color);
						if(module != modules.get(0))
							RenderingUtil.rectangle(x-2, y-3.3, lastWidth-1, y-4.3, color);
						if(module == modules.get(modules.size() - 1))
							RenderingUtil.rectangle(x-2, y+5.5, e.getResolution().getScaledWidth(), y+6.7, color);
						sigmaFont.drawStringWithShadow(module.getName(), x, y - 1, color);
						break;
					case"Vanilla":
						if(x <= lastWidth && modules.get(0) != module)	
							RenderingUtil.rectangle(x-3, y-3.3, x-2, y+7.7, color);
						else
							RenderingUtil.rectangle(x-3, y-4.3, x-2, y+7.7, color);
						
						if(module != modules.get(0) && x!= lastWidth)
							RenderingUtil.rectangle(lastWidth-2, y-3.3,x - 3 , y-4.3, color);
						if(module == modules.get(modules.size() - 1))
							RenderingUtil.rectangle(x - 2, y+6.5, e.getResolution().getScaledWidth(), y+7.7, color);
						vanillaFont.drawStringWithShadow(module.getName(), x-0.5f, y - 3,  color);
						break;
					}

					break;
					
				case "Random":
					color = module.getColor();
					switch(fontMode){
					case"Sigma":
						sigmaFont.drawStringWithShadow(module.getName(), x+0.2f, y - 1.5f, color);
						break;
					case"Vanilla":
						vanillaFont.drawStringWithShadow(module.getName(), x-0.5f, y - 3,  color);
						break;
					}

					break;
					
				case"Category":
					color = module.getCategoryColor();
					switch(fontMode){
					case"Sigma":
						sigmaFont.drawStringWithShadow(module.getName(), x+0.2f, y - 1.5f, color);
						break;
					case"Vanilla":
						vanillaFont.drawStringWithShadow(module.getName(), x-0.5f, y - 3,  color);
						break;
					}
					break;
				}
				break;
			case"Jigsaw":
				switch (colorMode) {
				case"Default":
					color = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green,
							ColorManager.hudColor.blue, 255);
				
					break;
				case"Rainbow":
					final Color rainB = Color.getHSBColor(h / 255.0f, ((Number) settings.get(BRIGHTNESS).getValue()).floatValue(), 1f);
					color = rainB.getRGB();
					break;
				case"Random":
					color = module.getColor();
					break;
				case"Category":
					color = module.getCategoryColor();
					break;
				}

				jigsawFont.drawString(module.getName(), x, y - 1,  color);
				break;
			}
			
			lastWidth = x;
			if (!Objects.equals(suffix, "")) {
				switch(hud){
				case"Sigma":
					switch(fontMode){
					case"Sigma":
						sigmaFont.drawStringWithShadow(suffix, x + sigmaFont.getWidth(module.getName()) - 2, y - 1,
								Colors.getColor(Colors.getColor(150)));
						break;
					case"Vanilla":
						vanillaFont.drawStringWithShadow(suffix, x + vanillaFont.getStringWidth(module.getName()) - 2, y - 3,
								Colors.getColor(Colors.getColor(150)));
						break;
					}
					break;
				case"Jigsaw":
					jigsawFont.drawString(suffix, x + jigsawFont.getWidth(module.getName()) - 2, y - 1,
							Colors.getColor(Colors.getColor(160)));
					break;
				}


			}
			h += 20 - ((Number) settings.get(WIDTH).getValue()).floatValue();
			if(animMode.equalsIgnoreCase("Smooth")){
				y += module.getTranslate().getY();
				glDisable(GL_SCISSOR_TEST);
				glPopMatrix();
			}else if(animMode.equalsIgnoreCase("Slide")){
				y +=10;
			}
			switch(hud){
			case"Sigma":
				if(fontMode.equalsIgnoreCase("Vanilla")){
					y+= 1;
				}
				break;
			case"Jigsaw":
				
				break;
			}
			
		}
	}

	private boolean shouldHide(Module module) {
		ModuleData.Type type = module.getType();
		return isBlacklisted(module);
	}

	private boolean isBlacklisted(Module module) {
		Class<? extends Module> clazz = module.getClass();
		return clazz.equals(ChatCommands.class) || clazz.equals(TabGUI.class) || clazz.equals(ClickGui.class)
				|| clazz.equals(Enabled.class) || module.isHidden();
	}

	private static void drawPotionStatus(ScaledResolution sr) {
		List<PotionEffect> potions = new ArrayList<>();
		for (Object o : mc.thePlayer.getActivePotionEffects())
			potions.add((PotionEffect) o);
		potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj
				.getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

		float pY = (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiAgoraIngame)) ? -15 : -2;
		for (PotionEffect effect : potions) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName());
			String PType = "";
			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			}
			if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
				PType = PType + "\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477 " + Potion.getDurationString(effect);
			}
			mc.fontRendererObj.drawStringWithShadow(name,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType),
					sr.getScaledHeight() - 9 + pY, potion.getLiquidColor());
			mc.fontRendererObj.drawStringWithShadow(PType,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType), sr.getScaledHeight() - 9 + pY, -1);
			pY -= 9;
		}
	}

	public void drawWaveString(String str, TTFFontRenderer font, float x, float y){
		float posX = x;
		for(int i = 0; i < str.length(); i++){
			String ch = str.charAt(i) + "";
			font.drawStringWithShadow(ch, posX, y,effect(i*3500000L, 1f,150).getRGB());
			posX += font.getWidth(ch)/1.3F;
			if( ch.equalsIgnoreCase("r") || ch.equalsIgnoreCase("i")){
				posX -= 0.6f;
			}else if(ch.equalsIgnoreCase("a") || ch.equalsIgnoreCase("t") || ch.equalsIgnoreCase("l") || ch.equalsIgnoreCase("1")){
				posX -= 0.3f;
			}
		}
	}
	public static Color effect(long offset, float brightness, int speed) {
		float hue = (float) (System.nanoTime() + (offset * speed)) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, brightness, 1F)).intValue()), 16);
		Color c = new Color((int) color);
		return new Color(c.getGreen()/255.0f,c.getBlue()/255.0F,c.getRed()/255.0F, c.getAlpha()/255.0F);
	}
	public void drawPub(String pub, EventRenderHUD e){
		boolean rainbow = (((Options) settings.get(ArraylistColorMode).getValue()).getSelected()).equalsIgnoreCase("Rainbow");
		boolean t = Client.getModuleManager().isEnabled(TabGUI.class);
		TTFFontRenderer sigmaFont = Client.fm.getFont("SFB 11");
		RenderingUtil.rectangle(e.getResolution().getScaledWidth_double()/2-sigmaFont.getWidth(pub)/2 - 1, 1, e.getResolution().getScaledWidth_double()/2+sigmaFont.getWidth(pub)/2, sigmaFont.getHeight(pub)*1.5, Colors.getColor(0, t ? (int)TabGUI.opacity.getX() : 200)); 

		sigmaFont.drawStringWithShadow(pub, (float)e.getResolution().getScaledWidth_double()/2-sigmaFont.getWidth(pub)/2, 3, rainbow ? Color.getHSBColor(hue.getOpacity() / 255.0f, 0.6f, 1f).getRGB()
				: Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green,
						ColorManager.hudColor.blue, t ? (int)TabGUI.opacity.getX() + 64 : 255));
	}
    private void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        GlStateManager.pushMatrix();
        float var8 = 1.0F / textureWidth;
        float var9 = 1.0F / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        boolean var1 = false;
        var11.startDrawingQuads();
        var11.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * var8), (double) ((v + height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + width) * var8), (double) ((v + height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + width) * var8), (double) (v * var9));
        var11.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * var8), (double) (v * var9));
        var10.draw();
        GlStateManager.popMatrix();
    }
}
