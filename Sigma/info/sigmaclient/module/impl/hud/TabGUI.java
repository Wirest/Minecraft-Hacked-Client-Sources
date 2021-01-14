package info.sigmaclient.module.impl.hud;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventKeyPress;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.data.SettingsMap;
import info.sigmaclient.module.data.ModuleData.Type;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.other.ClickGui;
import info.sigmaclient.module.impl.other.Ranking;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.RenderUtilities;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TabGUI extends Module {
	private final String TOOLTIPS = "TOOLTIPS";
	private final String FONT = "FONT";
	ArrayList<Module>modules = new ArrayList<>();
	List<Setting> settingList = new CopyOnWriteArrayList<>();
	ArrayList<ModuleData.Type>categories = new ArrayList<>();
	Timer timer = new Timer();
	TTFFontRenderer sigma = Client.fm.getFont("SFM 8");
	float categoryW = 55, moduleW = 55, settingW = 55, insetW = 10, boxH = 12.5f, x = categoryW, descX = categoryW;
	int y1=0,y2=0,y3=0,y4=0,opa=0;
	int col = 1;
	float h;
	String fontMode ="";
	String hudMode = "";
	public TabGUI(ModuleData data) {
		super(data);
		settings.put(TOOLTIPS, new Setting<>(TOOLTIPS, true, "Shows descriptions of features."));
		settings.put(FONT, new Setting<>(FONT, new Options("Font", "Sigma", new String[] { "Sigma", "Vanilla"}), "Font used."));
		for(Type cat : ModuleData.Type.values()){
			categories.add(cat);
		}
	}
	public static Translate opacity = new Translate(0, 0);
	private Translate category = new Translate(0, 0);
	private Translate module = new Translate(0, 0);
	private Translate setting = new Translate(0, 0);
	private Translate inset = new Translate(0, 0);
	private Translate collum = new Translate(0, 0);
	private Translate desc = new Translate(0, 0);
	@Override
	public void onEnable() {
	
	}

	@RegisterEvent(events = { EventRenderGui.class, EventTick.class, EventKeyPress.class })
	public void onEvent(Event event) {
		if (mc.gameSettings.showDebugInfo) {
			return;
		}
		if (event instanceof EventKeyPress) {
			EventKeyPress ek = (EventKeyPress) event;
			int key = ek.getKey();
			if(keyCheck(key)){
				timer.reset();
			}
			if(key == Keyboard.KEY_DOWN) {
				if(col == 1){
					y1 += 1;
					if(y1 > categories.size()-1){
						y1 = 0;
					}
				}else if(col == 2){
					if(modules.size() > 1)
					desc = new Translate((moduleW), 0);
					y2 += 1;
					if(y2 > modules.size()-1){
						y2 = 0;
					}
				}else if(col == 3){
					if(settingList.size() > 1)
					desc = new Translate((moduleW+settingW), 0);
					y3 += 1;
					if(y3 > settingList.size()-1){
						y3 = 0;
					}
				}
				
				
			}else if(key == Keyboard.KEY_UP) {
				if(col == 1){
					y1 -= 1;
					if(y1<0){
						y1=categories.size()-1;
					}
				}else if(col == 2){
					if(modules.size() > 1)
					desc = new Translate((moduleW), 0);
					y2 -= 1;
					if(y2<0){
						y2=modules.size()-1;
					}
				}else if(col == 3 ){
					if(settingList.size() > 1)
					desc = new Translate((moduleW+settingW), 0);
					y3 -= 1;
					if(y3<0){
						y3=settingList.size()-1;
					}
				}
			}else if(key == Keyboard.KEY_LEFT) {
				if(col > 1){
					col --;
					
					if(col == 1){
						x-=moduleW;
					}else if(col ==2){
						
						//desc = new Translate((moduleW+settingW), 0);
						x-=settingW;
					}else if(col ==3){
						//desc = new Translate((moduleW+settingW), 0);
						x-=insetW;
					}
				}
				
			}else if(key == Keyboard.KEY_RIGHT) {
				if(col < 4){
					
					if(col == 1){
						module = new Translate(0, 3);
						y2 = 0;
						Type cat = Type.values()[y1];
						modules = Client.getModuleManager().getModuleByCategory(cat);
					}else if(col == 2){
						setting = new Translate(0, 3);
						y3 = 0;
						Type cat = Type.values()[y1];
						Module mod = Client.getModuleManager().getModuleByCategory(cat).get(y2);
						
						if(mod != null ){
							List<Setting> add = getSettings(mod);
							settingList.clear();
							if(add != null && add.size() > 0){	
								desc = new Translate(0, 0);
								settingList.addAll(add);
							}
							
						}

					}
					if(col != 3){
						updateModuleWidth();
						updateSetWidth();
					}
					
					if(col ==1){
						x+=moduleW;
						col ++;
					}else if(col ==2 && settingList.size() > 0){
						x+=settingW;
						if(hudMode.equalsIgnoreCase("Jigsaw")){
							x -= 1;
						}
						col ++;
					}else if(col == 3){
						x+=insetW;
						y4=y3;
						col ++;
					}
					
					
				}
				
			}else if(key == Keyboard.KEY_RETURN){
				if(col == 2){
					Module mod = Client.getModuleManager().getModuleByCategory(Type.values()[y1]).get(y2);
					mod.toggle();
				}
			}
			if(col == 4){

				if (ek.getKey() == Keyboard.KEY_LEFT) {
				} else if (ek.getKey() == Keyboard.KEY_UP) {
					Type cat = Type.values()[y1];
					Module mod = Client.getModuleManager().getModuleByCategory(cat).get(y2);
					Setting set = getSettings(mod).get(y3);
					String WIDTH = set.getName();
					if (set.getValue() instanceof Number) {
						double increment = (set.getInc());
						String str = MathUtils.isInteger(MathUtils.getIncremental(
								(((Number) (set.getValue())).doubleValue() + increment),
								increment)) ? (MathUtils.getIncremental(
										(((Number) (set.getValue())).doubleValue() + increment), increment) + "")
												.replace(".0", "")
										: MathUtils.getIncremental(
												(((Number) (set.getValue())).doubleValue() + increment), increment)
												+ "";
						if (Double.parseDouble(str) > set.getMax() && set.getInc() != 0) {
							return;						
						}
						Object newValue = (StringConversions.castNumber(str, increment));
						if (newValue != null) {
							set.setValue(newValue);
							Module.saveSettings();
							return;
						}
						
					} else if (set.getValue().getClass().equals(Boolean.class)) {
						boolean xd = ((Boolean) set.getValue());
						set.setValue(!xd);
						Module.saveSettings();
					} else if (set.getValue() instanceof Options) {
						List<String> options = new CopyOnWriteArrayList<>();
						Collections.addAll(options, ((Options) set.getValue()).getOptions());
						for (int i = 0; i <= options.size() - 1; i++) {
							if (options.get(i).equalsIgnoreCase(((Options) set.getValue()).getSelected())) {
								if (i + 1 > options.size() - 1) {
									((Options) set.getValue()).setSelected(options.get(0));
								} else {
									((Options) set.getValue()).setSelected(options.get(i + 1));
								}
								break;
							}
						}
					}
				
				} else if (ek.getKey() == Keyboard.KEY_DOWN) {
					Type cat = Type.values()[y1];
					Module mod = Client.getModuleManager().getModuleByCategory(cat).get(y2);
					Setting set = getSettings(mod).get(y3);
					String WIDTH = set.getName();
					if (set.getValue() instanceof Number) {
						double increment = (set.getInc());

						String str = MathUtils.isInteger(MathUtils.getIncremental(
								(((Number) (set.getValue())).doubleValue() - increment),
								increment)) ? (MathUtils.getIncremental(
										(((Number) (set.getValue())).doubleValue() - increment), increment) + "")
												.replace(".0", "")
										: MathUtils.getIncremental(
												(((Number) (set.getValue())).doubleValue() - increment), increment)
												+ "";
						if (Double.parseDouble(str) < set.getMin() && increment != 0) {
							return;
						}
						Object newValue = (StringConversions.castNumber(str, increment));
						if (newValue != null) {
							set.setValue(newValue);
							Module.saveSettings();
							return;
						}
					} else if (set.getValue().getClass().equals(Boolean.class)) {
						boolean xd = ((Boolean) set.getValue()).booleanValue();
						set.setValue(!xd);
						Module.saveSettings();
					} else if (set.getValue() instanceof Options) {
						List<String> options = new CopyOnWriteArrayList<>();
						Collections.addAll(options, ((Options) set.getValue()).getOptions());
						for (int i = options.size() - 1; i >= 0; i--) {
							if (options.get(i).equalsIgnoreCase(((Options) set.getValue()).getSelected())) {
								if (i - 1 < 0) {
									((Options) set.getValue()).setSelected(options.get(options.size() - 1));
								} else {
									((Options) set.getValue()).setSelected(options.get(i - 1));
								}
								break;
							}
						}
					}
					
				}
			}
			
		}
		if (event instanceof EventTick) {
			Module hud = Client.getModuleManager().get(Enabled.class);
			if(hudMode != ((Options) hud.getSetting(Enabled.MODE).getValue()).getSelected()){
				hudMode = ((Options) hud.getSetting(Enabled.MODE).getValue()).getSelected();
				updateModuleWidth();
				updateSetWidth();
				col = 1;
				x = categoryW;
			}
			
			if(!fontMode.equalsIgnoreCase( (((Options) settings.get(FONT).getValue()).getSelected()))){
				fontMode = (((Options) settings.get(FONT).getValue()).getSelected());
				updateModuleWidth();
				updateSetWidth();
				if(col == 1){
					x = categoryW;
				}else if(col == 2){
					x = categoryW + moduleW;
				}else if(col == 3){
					x = categoryW + moduleW + settingW;
				}
				
			}
			if(timer.delay(4500)){
				opa = 80;
			}else{
				opa = 160;
			}
		}
		if (event instanceof EventRenderGui) {
			EventRenderGui er = (EventRenderGui) event;
		    Module hud = Client.getModuleManager().get(Enabled.class);
		    String hudMode = ((Options) hud.getSetting(Enabled.MODE).getValue()).getSelected();
		    switch(hudMode){
		    case"Sigma":
		    	drawLigmaTabgui(er);
		    	break;
		    case"Jigsaw":
		    	drawJigsawTabgui(er);
		    	break;
		    }
			
		}
	}
	void drawJigsawTabgui(EventRenderGui er){
		boxH = 9;
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		category.interpolate(0, 3+y1*boxH, 5);
		module.interpolate(0, 3+ y2*boxH, 5);
		setting.interpolate(0, 3+ y3*boxH, 5);
		opacity.interpolate(opa, opa, 4);
		collum.interpolate(x*2, 0, 4);
		desc.interpolate(descX, 0, 3);
		if(col == 2){
			descX = categoryW+moduleW+getStringWidth(modules.get(y2).getDescription()) + 7;
		}else if(col == 3){
			descX = categoryW+moduleW+settingW+getStringWidth(settingList.get(y3).getDesc());
		}else if(col == 4){
			descX =  categoryW+moduleW+settingW+insetW+getStringWidth(settingList.get(y3).getDesc())+10;
		}
		
		if(timer.delay(4500) || (collum.getX() != x*2 && col != 4 && !(col ==3 && collum.getX() > x*2))){
			descX =  col*10;
		}
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		int s = res.getScaleFactor();
		glScissor(0, 0, (int)collum.getX()/2*s, 1000*s);
	    glEnable(GL_SCISSOR_TEST);
	    double translateY = 29;
	    double translateX = -1.8;
	    GL11.glTranslated(translateX, translateY, 0);
		
	    int moreDank = 150;
	    int alpha = 200;
	    int rDank = ColorManager.hudColor.getRed() - moreDank < 0? 0 : ColorManager.hudColor.getRed() - moreDank;
	    int gDank = ColorManager.hudColor.getGreen() - moreDank < 0? 0 : ColorManager.hudColor.getGreen() - moreDank;
	    int bDank = ColorManager.hudColor.getBlue() - moreDank < 0? 0 : ColorManager.hudColor.getBlue() - moreDank;
	    int dank = Colors.getColor(rDank, gDank, bDank, alpha);
	     
	    h+=0.2f;
		if (h > 255) {
			h = 0;
		}
	
		int hud = Colors.getColor(ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), alpha);
		
		
	    RenderingUtil.rectangle(3.3, 2.3, categoryW, categories.size()*boxH + 3.5, Colors.getColor(40,185));
	    RenderingUtil.rectangle(categoryW+2.8, 2.3, categoryW+moduleW-1, modules.size()*boxH + 3.5, Colors.getColor(40,185));
	    RenderingUtil.rectangle(categoryW+moduleW+2.8, 2.3, categoryW+moduleW+settingW-1.4, settingList.size()*boxH + 3.2, Colors.getColor(40,185));
	    RenderingUtil.drawFilledTriangle(categoryW+moduleW+settingW+5f, y4*boxH+2.8f+boxH/2, 8, dank, hud);
	    hud = Colors.getColor(ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), 255);
	    Module enabled = Client.getModuleManager().get(Enabled.class);
		final Color rainB = Color.getHSBColor(h / 255.0f, 0.6f, 0.9f);
		if(((Options) enabled.getSetting(Enabled.ArraylistColorMode).getValue()).getSelected().equals("Rainbow")){
			hud = rainB.getRGB();
		}
	    RenderingUtil.rectangle(4, category.getY(), categoryW-0.35, category.getY() + boxH,  hud);
	    RenderingUtil.rectangle(3.5+categoryW, module.getY(), categoryW+ moduleW-1.5, module.getY() + boxH, hud);
		RenderingUtil.rectangle(3.3+categoryW+ moduleW, setting.getY(), categoryW+ moduleW+settingW - 1.5, setting.getY() + boxH-0.4, hud);
		RenderingUtil.drawBorderRect(3.7, 2.7, categoryW, categories.size()*boxH + 3.2,Colors.getColor(10,155), 1);
		RenderingUtil.drawBorderRect(categoryW+3.3, 2.6, categoryW+moduleW-1, modules.size()*boxH + 3,Colors.getColor(10,155), 1);
		RenderingUtil.drawBorderRect(categoryW+moduleW+3.3, 2.6, categoryW+moduleW+settingW-1.4, settingList.size()*boxH + 2.8,Colors.getColor(10,155), 1);
		float Y = 1;
	
		int strBright = 255;
		if(!categories.isEmpty()){
			for(Type category : categories){		
				drawString(category.name(), 5, Y, Colors.getColor(strBright, 255));
				Y += boxH;
			}
		}
		Y=1;
		if(!modules.isEmpty()){
			for(Module mod : modules){
				drawString(mod.getName(), 5+categoryW, Y, Colors.getColor(mod.isEnabled()?strBright : strBright - 90, 255));
				Y+= boxH;
			}
		}
		Y=1;	
		if( settingList != null && !modules.isEmpty() && modules != null &&  settingList.size() > 0){
			for (Setting setting : settingList) {
				String xd = setting.getName().charAt(0) + setting.getName().toLowerCase().substring(1);
				String fagniger = setting.getValue() instanceof Options
						? ((Options) setting.getValue()).getSelected() : setting.getValue().toString();
				drawString(xd+" " + fagniger, 5+categoryW+moduleW, Y, Colors.getColor(strBright, 255));
				Y+= boxH;
			}  
		}
		
		
		
		
	
		glDisable(GL_SCISSOR_TEST);
		if(!modules.isEmpty() && (Boolean)settings.get(TOOLTIPS).getValue()){
			if(col == 2 && collum.getX() == x*2){
				String descM = modules.get(y2).getDescription();
				glScissor((int)(moduleW), 0, (int)desc.getX()*s, 1000*s);
			    glEnable(GL_SCISSOR_TEST);
				RenderingUtil.rectangle(3+categoryW+moduleW, y2*boxH+2, categoryW+moduleW+getStringWidth(descM) + 7, y2*boxH+4 + boxH, Colors.getColor(30,155));			
				RenderingUtil.drawBorderRect(3+categoryW+moduleW, y2*boxH+2, categoryW+moduleW+getStringWidth(descM) + 7, y2*boxH+4 + boxH,Colors.getColor(10,155),1);
				drawString(descM, 5+categoryW+moduleW, y2*boxH+1.6f, Colors.getColor(strBright, 255));
				glDisable(GL_SCISSOR_TEST);
			}else if(col >= 3){
				String descS = settingList.get(y3).getDesc();
				glScissor((int)(categoryW+moduleW-3), 0, (int)desc.getX()*s, 1000*s);
			    glEnable(GL_SCISSOR_TEST);
			    float test = Math.abs(categoryW+moduleW+settingW - collum.getX()/2);
				RenderingUtil.rectangle(categoryW+moduleW+settingW+insetW-8+test, y3*boxH+2, test+categoryW+moduleW+settingW+insetW+getStringWidth(descS) -3, y3*boxH+4 + boxH, Colors.getColor(30,155));
				RenderingUtil.drawBorderRect(categoryW+moduleW+settingW+insetW-8+test, y3*boxH+2, test+categoryW+moduleW+settingW+insetW+getStringWidth(descS) -3, y3*boxH+4 + boxH,Colors.getColor(10,155),1);
				drawString(descS, categoryW+moduleW+settingW+insetW+test-6, y3*boxH+1.5F, Colors.getColor(strBright, 255));
				glDisable(GL_SCISSOR_TEST);
			}
			
		}
		GL11.glTranslated(translateX, -translateY, 0);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}
	void drawLigmaTabgui(EventRenderGui er){
		boxH = 12.5f;
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		category.interpolate(0, 3+y1*boxH, 5);
		module.interpolate(0, 3+ y2*boxH, 5);
		setting.interpolate(0, 3+ y3*boxH, 5);
		opacity.interpolate(opa, opa, 4);
		collum.interpolate(x*2, 0, 4);
		desc.interpolate(descX, 0, 3);
		if(col == 2){
			descX = categoryW+moduleW+getStringWidth(modules.get(y2).getDescription()) + 7;
		}else if(col == 3){
			descX = categoryW+moduleW+settingW+getStringWidth(settingList.get(y3).getDesc());
		}else if(col == 4){
			descX =  categoryW+moduleW+settingW+insetW+getStringWidth(settingList.get(y3).getDesc())+10;
		}
		
		if(timer.delay(4500) || (collum.getX() != x*2 && col != 4 && !(col ==3 && collum.getX() > x*2))){
			descX =  col*10;
		}
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		int s = res.getScaleFactor();
		glScissor(0, 0, (int)collum.getX()/2*s, 1000*s);
	    glEnable(GL_SCISSOR_TEST);
	    int translateY = 15;

	    GL11.glTranslated(0, translateY, 0);
		
	    int moreDank = 150;
	    int alpha = 200;
	    int rDank = ColorManager.hudColor.getRed() - moreDank < 0? 0 : ColorManager.hudColor.getRed() - moreDank;
	    int gDank = ColorManager.hudColor.getGreen() - moreDank < 0? 0 : ColorManager.hudColor.getGreen() - moreDank;
	    int bDank = ColorManager.hudColor.getBlue() - moreDank < 0? 0 : ColorManager.hudColor.getBlue() - moreDank;
	    int dank = Colors.getColor(rDank, gDank, bDank, alpha);
	    int hud = Colors.getColor(ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), alpha); 
	    RenderingUtil.rectangle(2, -1, categoryW, 80, Colors.getColor(0,(int)opacity.getX()));
	    RenderingUtil.rectangle(categoryW+2, 1, categoryW+moduleW, modules.size()*boxH + 4.5, Colors.getColor(0,(int)opacity.getX()));
	    RenderingUtil.rectangle(categoryW+moduleW+2, 1, categoryW+moduleW+settingW, settingList.size()*boxH + 4.5, Colors.getColor(0,(int)opacity.getX()));
	    RenderingUtil.drawHLine(6, 0,categoryW-4,0, 1, Colors.getColor(255, 255, 255, 255));
	    RenderingUtil.drawFilledTriangle(categoryW+moduleW+settingW+5f, y4*boxH+2.8f+boxH/2, 8, dank, hud);
	    RenderingUtil.drawGradientSideways(4, category.getY(), categoryW - 2, category.getY() + boxH, dank, hud);
	    RenderingUtil.drawGradientSideways(3.5+categoryW, module.getY(), categoryW+ moduleW-1.5, module.getY() + boxH, dank, hud);
		RenderingUtil.drawGradientSideways(3.3+categoryW+ moduleW, setting.getY(), categoryW+ moduleW+settingW - 1.5, setting.getY() + boxH-0.4, dank, hud);
		float Y = 5;
	
		int strBright = ((int)opacity.getX() + 120  > 255) ? 255 : (int)opacity.getX() + 150;
		if(strBright > 255)
			strBright = 255;
		if(!categories.isEmpty()){
			for(Type category : categories){		
				drawString(category.name(), 5, Y, Colors.getColor(strBright, 255));
				Y += boxH;
			}
		}
		Y=5;
		if(!modules.isEmpty()){
			for(Module mod : modules){
				drawString(mod.getName(), 5+categoryW, Y, Colors.getColor(mod.isEnabled()?strBright : strBright - 90, 255));
				Y+= boxH;
			}
		}
		Y=5;	
		if( settingList != null && !modules.isEmpty() && modules != null &&  settingList.size() > 0){
			for (Setting setting : settingList) {
				String xd = setting.getName().charAt(0) + setting.getName().toLowerCase().substring(1);
				String fagniger = setting.getValue() instanceof Options
						? ((Options) setting.getValue()).getSelected() : setting.getValue().toString();
				drawString(xd+" " + fagniger, 5+categoryW+moduleW, Y, Colors.getColor(strBright, 255));
				Y+= boxH;
			}  
		}
		
		
		
		
	
		glDisable(GL_SCISSOR_TEST);
		if(!modules.isEmpty() && (Boolean)settings.get(TOOLTIPS).getValue()){
			if(col == 2 && collum.getX() == x*2){
				String descM = modules.get(y2).getDescription();
				glScissor((int)(moduleW), 0, (int)desc.getX()*s, 1000*s);
			    glEnable(GL_SCISSOR_TEST);
				RenderingUtil.rectangle(3+categoryW+moduleW, y2*boxH+3, categoryW+moduleW+getStringWidth(descM) + 7, y2*boxH+3 + boxH, Colors.getColor(0,(int)opacity.getX()));			
				drawString(descM, 5+categoryW+moduleW, y2*boxH+5, Colors.getColor(strBright, 255));
				glDisable(GL_SCISSOR_TEST);
			}else if(col >= 3){
				String descS = settingList.get(y3).getDesc();
				glScissor((int)(categoryW+moduleW-3), 0, (int)desc.getX()*s, 1000*s);
			    glEnable(GL_SCISSOR_TEST);
			    float test = Math.abs(categoryW+moduleW+settingW - collum.getX()/2);
				RenderingUtil.rectangle(categoryW+moduleW+settingW+insetW-8+test, y3*boxH+3, test+categoryW+moduleW+settingW+insetW+getStringWidth(descS) -3, y3*boxH+3 + boxH, Colors.getColor(0,(int)opacity.getX()));			
				drawString(descS, categoryW+moduleW+settingW+insetW+test-6, y3*boxH+5, Colors.getColor(strBright, 255));
				glDisable(GL_SCISSOR_TEST);
			}
			
		}
		GL11.glTranslated(0, -translateY, 0);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}
	private void updateModuleWidth(){
		float maxW = 0;
		if(!modules.isEmpty()){
			for(Module module : modules){
				if(getStringWidth(module.getName()) > maxW){
					maxW = getStringWidth(module.getName());
				}
			}
			moduleW = maxW+7;
		}
	}

	private void updateSetWidth(){
		float maxW = 0;
		if(settingList != null && settingList.size() > 0){
			for (Setting set : settingList) {
				if (set.getValue() instanceof Number) {
					double increment = set.getInc();
					String str = MathUtils.isInteger(MathUtils.getIncremental(
							(((Number) (set.getValue())).doubleValue() - increment),
							increment)) ? (MathUtils.getIncremental(
									(((Number) (set.getValue())).doubleValue() - increment), increment) + "")
											.replace(".0", "")
									: MathUtils.getIncremental(
											(((Number) (set.getValue())).doubleValue() - increment), increment)
											+ "";
					Object newValue = (StringConversions.castNumber(str, increment));
					if (newValue != null) {
						if(getStringWidth(set.getName() + " " + str) > maxW){
							maxW = getStringWidth(set.getName() + " " + str);
						}				
					}
				} else if (set.getValue().getClass().equals(Boolean.class)) {
					boolean xd = ((Boolean) set.getValue()).booleanValue();
					if(getStringWidth(set.getName() + " " + xd) > maxW){
						maxW = getStringWidth(set.getName() + " " + xd);
					}
				} else if (set.getValue() instanceof Options) {
					List<String> options = new CopyOnWriteArrayList<>();
					Collections.addAll(options, ((Options) set.getValue()).getOptions());
					for (int i = 0; i <= options.size() - 1; i++) {
						if(getStringWidth(set.getName() + " " + options.get(i)) > maxW){
							maxW = getStringWidth(set.getName() + " " + options.get(i));
						}
					}
				}
			}
			settingW = maxW+7;
		}
	}
	private List<Setting> getSettings(Module mod) {
		List<Setting> settings = new CopyOnWriteArrayList<>();
		settings.addAll(mod.getSettings().values());
		for (Setting setting : settings) {
			if (setting.getValue().getClass().equals(String.class)) {
				settings.remove(setting);
			}
		}
		if (settings.isEmpty()) {
			return null;
		}
		settings.sort(Comparator.comparing(Setting::getName));
		return settings;
	}
	private void drawString(String text, float x, float y, int col){

		switch(hudMode){
		case"Sigma":
			switch(fontMode){
			case"Sigma":
				
				Client.fm.getFont("SFB 8").drawString(text, x, y+3f, col);
				break;
			case"Vanilla":
				mc.fontRendererObj.drawString(text, x+1, y, col);
				break;
			}
		    break;
		case"Jigsaw":
			Client.fm.getFont("JIGR 19").drawStringWithShadow(text, x, y+3f, col);
			break;
		}
		
	}
	private int getStringWidth(String text){	
		switch(hudMode){
		case"Sigma":
			switch(fontMode){
			case"Sigma":
				return (int)Client.fm.getFont("SFB 8").getWidth(text);
			case"Vanilla":
				return mc.fontRendererObj.getStringWidth(text);
			default:
				return 0;
			}
		case"Jigsaw":
			return (int)Client.fm.getFont("JIGR 19").getWidth(text);
		default:
			return 0;
		}
		
	}
	private boolean keyCheck(int key) {
		boolean active = false;
		switch (key) {
		case Keyboard.KEY_DOWN:
			active = true;
			break;
		case Keyboard.KEY_UP:
			active = true;
			break;
		case Keyboard.KEY_RETURN:
			active = true;
			break;
		case Keyboard.KEY_LEFT:
			active = true;
			break;
		case Keyboard.KEY_RIGHT:
			active = true;
			break;
		default:
			break;
		}
		return active;
	}
}
