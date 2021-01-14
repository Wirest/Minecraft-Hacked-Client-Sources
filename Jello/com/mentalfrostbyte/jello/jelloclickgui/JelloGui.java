package com.mentalfrostbyte.jello.jelloclickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventTick;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.tabgui.TabGUI.Cat;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.NumberValue;
import com.mentalfrostbyte.jello.util.Stencil;
import com.mentalfrostbyte.jello.util.Value;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class JelloGui extends GuiScreen {

	
	
	public List<Panel> panels = Lists.newArrayList();
	public int mouseX;
	public int mouseY;
	
	public Module selectedModule;
	public Panel selectedCategory;
	public float lastSettingsTrans;
	public float settingsTrans;
	
	public NumberValue selectedValue;
	public boolean exit;
	
	public float lastPercent;
	public float percent;
	public float percent2;
	public float lastPercent2;
	
	public float outro;
	public float lastOutro;
	
	public int mouseWheel;
	
	public Music music;
	
	public JelloGui(){
		EventManager.register(this);
		int x = 50;
		int y = 50;
		for(Cat c : Jello.tabgui.cats){
		panels.add(new Panel(x, y, c));
			x += 119 + 5;
		}
		music = new Music(x, y);
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
    	mc.entityRenderer.theShaderGroup = null;
    	
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	selectedValue = null;
       for(Panel p : panels){
    	   p.mouseReleased(mouseX, mouseY, state);
       }
       music.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
    	if(keyCode == 1){
    		if(selectedModule != null){
    			exit = true;
    			//selectedModule = null;
    			//selectedCategory = null;
    			//selectedValue = null;
    		}else{
    		mc.displayGuiScreen(null);
    		}
    	}
    	music.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	selectedValue = null;
    	if(selectedCategory != null){
    		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    		float popupTop = selectedCategory.y;
    		float intro = smoothTrans(settingsTrans, lastSettingsTrans);
    		float leftOffset = 60*intro - 60;
    		float popupLeft = Math.max(0, selectedCategory.x - (30*intro)) - Math.max(0, (selectedCategory.x - (30*intro)) - (sr.getScaledWidth() - (100 + (60)*intro)));
    		
    		int y = 0;
    	for(Value v : selectedModule.getValues()){
    		if(v instanceof BooleanValue){
    			BooleanValue b = (BooleanValue)v;
    			if(this.isHoveringCoords(popupLeft + 141, popupTop + 25 + 10 + (34/2f)*y,  12, 12, mouseX, mouseY)){
    			b.set(!b.getValue());
    			}
    				
    		}
    		if(v instanceof NumberValue){
    			NumberValue n = (NumberValue)v;
    			float popupTop2 = popupTop - 0.5f + 5.5f;
    			float popupLeft2 = popupLeft + 11;
    			float sliderPercent = Math.min(1, Math.max(0, (mouseX-(popupLeft2 + 95 + 2 + 26) + 28)/(55-11)));
    		    float actualPercent = (float) ((n.getValue()-n.getMin())/(n.getMax()-n.getMin()));
    		    
    		    if(this.isHoveringCoords((float) (popupLeft2 + 95 + 2 + (52 - 11)*(actualPercent) - 4.5f- 1.5f), popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
    		    	selectedValue = n;
    		    }
    		}
    		y++;
    		
    	}
    	}
    	if(selectedModule != null)
    		return;
    	
    	for(Panel p : panels){
    		p.mouseClicked(mouseX, mouseY, mouseButton, this);
    	}
    	music.mouseClicked(mouseX, mouseY, mouseButton, this);
    }
    
    @EventTarget
    public void onTick(EventTick e){
    	if(!(mc.currentScreen instanceof JelloGui)){
    		lastOutro = outro;
    		if(outro < 1.7){
    			outro += 0.1f;

    			outro += ((1.7-outro)/(3f))-0.001;
    		}
    		if(outro > 1.7){
    			outro = 1.7f;
    		}
    		if(outro < 1){
    			outro = 1;
    		}
    	}
    	if(!(mc.currentScreen instanceof JelloGui))
    		return;
    	
    	lastSettingsTrans = settingsTrans;
    	lastPercent = percent;
    	lastPercent2 = percent2;
        if(percent > .98){
        	percent += ((.98-percent)/(1.45f))-0.001;
        }
        if(percent <= .98){
        	if(percent2 < 1){
        		percent2 += ((1-percent2)/(2.8f))+0.002;
        	}
        }
        
        	
    		
    	if(selectedModule != null){
    		if(selectedCategory != null){
        		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        		
        		
        		float popupTop = selectedCategory.y;
        		float intro = smoothTrans(settingsTrans, lastSettingsTrans);
        		float leftOffset = 60*intro - 60;
        		float popupLeft = Math.max(0, selectedCategory.x - (30*intro)) - Math.max(0, (selectedCategory.x - (30*intro)) - (sr.getScaledWidth() - (100 + (60)*intro)));
        		
        		    
        	int y = 0;
        	for(Value v : selectedModule.getValues()){
        		if(v instanceof BooleanValue){
        			BooleanValue b = (BooleanValue)v;
        			
        		}
        		if(v instanceof NumberValue){
        			NumberValue n = (NumberValue)v;
        			float popupTop2 = popupTop - 0.5f + 5.5f;
        			float popupLeft2 = popupLeft + 11;
        		    float sliderPercent = Math.min(1, Math.max(0, (mouseX-(popupLeft2 + 95 + 2 + 26) + 28)/(55-11)));
        		    float actualPercent = (float) ((n.getValue()-n.getMin())/(n.getMax()-n.getMin()));
        		    
        		    
        		    float sliderLeft = popupLeft2 + 95 + 2;
        		    if((selectedValue != null && selectedValue == n) || this.isHoveringCoords((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
            			n.lastHover = n.hover;
        		    	if(n.hover < 1.18){
            				n.hover += 0.1;
            			}
            			if(n.hover > 1.18){
            				n.hover = 1.18f;
            			}
            			if(n.hover < 0){
            				n.hover = 0;
            			}
        		    	//GlStateManager.scale(1.18, 1.18, 1);
            			}else{
            				n.lastHover = n.hover;
            		    	if(n.hover > 1){
                				n.hover -= 0.1;
                			}
                			if(n.hover > 1.18){
                				n.hover = 1.18f;
                			}
                			if(n.hover < 0){
                				n.hover = 0;
                			}
            			}
        		    
        		    if((selectedValue != null && selectedValue == n) || this.isHoveringCoords((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
        		    	   
        		    	//GlStateManager.scale(1/1.18, 1/1.18, 1);
        		    }
        		    
        		}
        		y++;
        		
        	}
        	
        	}
    		if(this.settingsTrans < 1 && !exit){
    			settingsTrans += ((1-settingsTrans)/(1.8f))+0.01;
    			//settingsTrans += 0.1;
    		}
    		if(exit && settingsTrans > 0){
    			settingsTrans += ((0-settingsTrans)/(1.8f))-0.01;
    		}
    	}
    	if(settingsTrans <= 0.00 && exit){
    		exit = false;
    		selectedModule = null;
			selectedCategory = null;
			selectedValue = null;
    	}
    	if(settingsTrans > 1){
    		settingsTrans = 1;
    	}
    	if(settingsTrans < 0){
    		settingsTrans = 0;
    	}
    	for(Panel p : panels){
    		p.onTick();
    	}
    	music.onTick();
    }
    
public void drawShadow(float x, float y, float x2, float y2, float width, float height){

	ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
	drawTexturedRect(x - 9, y - 9, 9, 9, "paneltopleft", sr);
	
	drawTexturedRect(x - 9, y +  height, 9, 9, "panelbottomleft", sr);
	
	drawTexturedRect(x + width, y +  height, 9, 9, "panelbottomright", sr);
	
	drawTexturedRect(x + width, y - 9, 9, 9, "paneltopright", sr);
	
	drawTexturedRect(x - 9, y, 9, height, "panelleft", sr);
	
	drawTexturedRect(x + width, y, 9, height, "panelright", sr);

	drawTexturedRect(x, y - 9, width, 9, "paneltop", sr);

	drawTexturedRect(x, y + height, width, 9, "panelbottom", sr);
    	//TOP
  		/*Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
  	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f + 9.5f, y - 19/2f, 9.5f+ 5.5f, 0,  width + 19- 9.5f- 9.5f, 9.5f, width + 19+ 5.5f+ 5.5f, 338/2f );
  	    
  	    //BOTTOM
  	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
  	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f + 9.5f, y + height, 9.5f+5.5f, height - 9.5f,  width+19- 9.5f- 9.5f, 9.5f, width+19 + 5.5f + 5.5f, height-0.5f);
  	     
	
  	    //LEFT
  	   Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
  	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f, y - 19/2f + 9.5f, 0, 9.5f,  9.5f, height - 9.5f*2 + 19, 238/2f, height+19);
  	  
  	    //RIGHT
  	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
  	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + width, y - 19/2f + 9.5f, width-9.5f, 9.5f + 5.5f,  9.5f, height+19 - 9.5f*2, width, height+19+5.5f+5.5f);
  	    
    
    //TOP
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f, y - 19/2f, 0, 0,  9.5f, 9.5f, 238/2f, 338/2f);
    
    //BOTTOM
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f, y + height, 0, 338/2f - 9.5f,  9.5f, 9.5f, 238/2f, 338/2f);
     
    //LEFT
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + width, y + height, 238/2f-9.5f, height-9.5f + 4.5f,  9.5f, 9.5, 238/2f, 338/2f);
  
    //RIGHT
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x  + width, y - 9.5f , 238/2f-9.5f, 0,  9.5f, 9.5, 238/2f, 338/2f);*/
    
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	this.mouseX = mouseX;
    	this.mouseY = mouseY;
    	
    	if (Mouse.hasWheel()) {
            mouseWheel = Mouse.getDWheel();
    	}

		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    		float outro = smoothTrans(this.outro, lastOutro);
		if(mc.currentScreen == null){
			
	    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
	    		GlStateManager.scale(outro, outro, 0);
	    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
		}
		
    		float percent = smoothTrans(this.percent, lastPercent);
    		float percent2 = smoothTrans(this.percent2, lastPercent2);
    		
    		
    		
    		if(percent > 0.98){
    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
    		GlStateManager.scale(percent, percent, 0);
    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    		}else{
    			if(percent2 <= 1){
    			GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
        		GlStateManager.scale(percent2, percent2, 0);
        		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    			}
    		}
    		GlStateManager.enableBlend();
    	for(Panel p : panels){
    		GlStateManager.color(1, 1, 1, 1);
    		if(selectedCategory == null || p != selectedCategory)
    		p.drawPanel(mouseX, mouseY, (float) (mc.currentScreen == null ? Math.min(1, Math.max(((1-(outro-1)-.4)*1.66667), 0)) : (float)Math.max(Math.min(-(((percent-1.23)*4)), 1), 0)), false, 0, 0);
    	}
    	music.drawPanel(mouseX, mouseY, (float) (mc.currentScreen == null ? Math.min(1, Math.max(((1-(outro-1)-.4)*1.66667), 0)) : (float)Math.max(Math.min(-(((percent-1.23)*4)), 1), 0)), false, 0, 0);
    	
    	if(percent > 0.98){
    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
    		GlStateManager.scale(1/percent, 1/percent, 0);
    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    		}else{
    			if(percent2 <= 1){
    			GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
        		GlStateManager.scale(1/percent2, 1/percent2, 0);
        		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    			}
    		}
    	if(mc.currentScreen == null){
			
    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
    		GlStateManager.scale(1/outro, 1/outro, 0);
    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
	}
    	GlStateManager.color(1, 1, 1, 1);
    	GlStateManager.color(1, 1, 1, (float) (mc.currentScreen == null ? Math.min(1, Math.max(((1-(outro-1)-.4)*1.66667), 0)) : (float)Math.max(Math.min(-(((percent-1.23)*4)), 1), 0)));
    	//if(music.selectedSong != null)
    	this.drawTexturedRect(sr.getScaledWidth() - 31/2f - 9, sr.getScaledHeight() - 34/2f - 9, 31/2f, 34/2f, "ambient play", sr);
    	
    	if(mc.currentScreen == null){
			
    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
    		GlStateManager.scale(outro, outro, 0);
    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
	}
    	if(percent > 0.98){
    		GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
    		GlStateManager.scale(percent, percent, 0);
    		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    		}else{
    			if(percent2 <= 1){
    			GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
        		GlStateManager.scale(percent2, percent2, 0);
        		GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
    			}
    		}
    	if(selectedCategory != null){
    		
    		
    		float popupTop = selectedCategory.y;
    		float intro = smoothTrans(settingsTrans, lastSettingsTrans);
    		float leftOffset = 60*intro - 60;
    		float popupLeft = Math.max(0, selectedCategory.x - (30*intro)) - Math.max(0, (selectedCategory.x - (30*intro)) - (sr.getScaledWidth() - (100 + (60)*intro)));
    		
    		
    		Gui.drawFloatRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, (int)(intro*96)).getRGB());
    		 GlStateManager.disableAlpha();
 	    	GlStateManager.enableBlend();
 	    	GL11.glEnable(3042);
 	    	GL11.glColor4f(1, 1, 1, 1);
 	    	/*//TOP
 			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
 		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft - 19/2f + 9.5f, popupTop - 19/2f, 9.5f, 0,  238/2f- 9.5f- 9.5f, 9.5f, 238/2f, 338/2f);
 		    
 		    //BOTTOM
 		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
 		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft - 19/2f + 9.5f, popupTop + 330/2f, 9.5f, 338/2f - 9.5f,  238/2f- 9.5f- 9.5f, 9.5f, 238/2f, 338/2f);
 		     
 		    //LEFT
 		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
 		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft - 19/2f, popupTop - 19/2f, 0, 0,  9.5f, 338/2f, 238/2f, 338/2f);
 		  
 		    //RIGHT
 		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
 		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft + 320/2f, popupTop - 19/2f, 238/2f-9.5f, 0,  9.5f, 360/2f, 238/2f, 360/2f);
 		    */
 	    	//System.out.println(238/2f - 19);
 	    	Stencil.write(false);
 	    	Gui.drawFloatRect(popupLeft, popupTop, popupLeft + 100 + (60)*intro/* + 1.5f*(1-intro)*/, popupTop + 150 + (14.5f)*intro/* + 1f*(1-intro)*/, -1);
 	    	Stencil.erase(false);
 	    	drawShadow(popupLeft, popupTop, 0, 0, 100 + (60)*intro, 150 + (14.5f)*intro);
 	    	Stencil.dispose();
 		    
    		Gui.drawFloatRect(popupLeft, popupTop + 25, popupLeft + 100 + (60)*intro/*+ 1.5f*(1-intro)*/, popupTop + 150 + (14.5f)*intro/* + 1f*(1-intro)*/, -1);
    		GlStateManager.disableAlpha();
 	    	GlStateManager.enableBlend();
 	    	GL11.glEnable(3042);
 	    	GL11.glColor4f(1, 1, 1, 1);
 	    	
 	    	Stencil.write(false);
 	    	Gui.drawFloatRect(popupLeft, popupTop, popupLeft + 100 + (60)*intro/* + 1.5f*(1-intro)*/, popupTop + 150 + (14.5f)*intro/* + 1f*(1-intro)*/, -1);
 	    	Stencil.erase(true);
    		selectedCategory.drawPanel(mouseX, mouseY, 1-intro, true, popupLeft, popupLeft + 100 + (60)*intro);
    		Stencil.dispose();
    		
    		Gui.drawFloatRect(popupLeft, popupTop, popupLeft + 100 + (60)*intro/*+ 1.5f*(1-intro)*/, popupTop + 25, 0xff99989a);
    		FontUtil.jelloFontAddAlt.drawCenteredString(selectedCategory.category.name, popupLeft + 320/4f - 30f*(1-intro), popupTop + 17/2f, new Color(1f, 1f, 1f, 1).getRGB());
    	    
    	int y = 0;
    	for(Value v : selectedModule.getValues()){
    		GlStateManager.color(1, 1, 1, 1);
    		GlStateManager.enableBlend();
    		if(v instanceof BooleanValue){
    			BooleanValue b = (BooleanValue)v;
    			GlStateManager.color(1, 1, 1, intro);
    			if(this.isHoveringCoords(popupLeft + 141, popupTop + 25 + 10 + (34/2f)*y,  12, 12, mouseX, mouseY)){
    			GlStateManager.color(0.9f, 0.9f, 0.9f, 1f);
    			}
    			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(b.getValue() ? "Jello/checked.png" : "Jello/unchecked.png"));
    		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft + 141 + leftOffset, popupTop + 25 + 10 + (34/2f)*y, 0, 0,  12, 12,12,12);
    			FontUtil.jelloFontGui.drawNoBSString(b.getName(), popupLeft + 7, popupTop + 25 + 21/2f + (34/2f)*y, new Color(0, 0, 0, intro).getRGB());
    			
    		}
    		if(v instanceof NumberValue){
    			NumberValue n = (NumberValue)v;
    			float popupTop2 = popupTop - 0.5f + 5.5f;
    			float popupLeft2 = popupLeft + 11;
        		float hoverEffect = smoothTrans(n.hover, n.lastHover);
    			GlStateManager.color(1, 1, 1, intro);
    			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/leftslider.png"));
    		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft2 + 95 + leftOffset, popupTop2 + 35 + 17*y, 0, 0,  2, 3,2,3);
    		   
    		    float sliderPercent = Math.min(1, Math.max(0, (mouseX-(popupLeft2 + 95 + 2 + 26) + 28)/(55-11)));
    		    float actualPercent = (float) ((n.getValue()-n.getMin())/(n.getMax()-n.getMin()));
    		    
    		    
    		    float sliderLeft = popupLeft2 + 95 + 2;
    		    Color c = new Color(0xff3b99fc);
    		    Color c1 = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(intro*255));
    		    Gui.drawFloatRect(sliderLeft + leftOffset, popupTop2 + 35 + 17*y, sliderLeft + leftOffset + (52 - 11)*(actualPercent) , popupTop2 + 35 + 17*y + 3, c1.getRGB());
    		    
    		    Gui.drawFloatRect(sliderLeft + leftOffset, popupTop2 + 35 + 17*y, sliderLeft + leftOffset + (52 - 11)*(actualPercent), popupTop2 + 35 + 17*y + 3, c1.getRGB());
    		    Gui.drawFloatRect(sliderLeft + leftOffset + (52 - 11)*(actualPercent), popupTop2 + 35 + 17*y, popupLeft2 + leftOffset + 95 + (54 - 11), popupTop2 + 35 + 17*y + 3, new Color(0, 0, 0, (int)(intro*25)).getRGB());
    		    GlStateManager.disableAlpha();
    	    	GlStateManager.enableBlend();
    	    	GL11.glEnable(3042);
    	    	GL11.glColor4f(1, 1, 1, 1);
    	    	GlStateManager.color(1, 1, 1, intro);
    			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/sliderright.png"));
    		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft2 + 143 - 2 + 6 - 9 + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y, 0, 0,  2, 3,2,3);

    	    	GL11.glColor4f(1, 1, 1, 1);if(selectedValue != null && selectedValue == n){
    		    	n.setVal((n.getMin() + (sliderPercent*(n.getMax()-n.getMin()))));
    		    //	FontUtil.jelloFontAddAlt3.drawString(((n.getValue()-n.getMin())/(n.getMax()-n.getMin()))+"", 1, 1, -1);
    		    }
    		    if((hoverEffect > 1) || this.isHoveringCoords((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
        			
    		    	//GlStateManager.color(0.9f, 0.9f, 0.9f, 1f);
    		    	//GlStateManager.color(1,1,1, 0.5f);
        			GlStateManager.translate((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f, 0);
        			GlStateManager.scale(hoverEffect, hoverEffect, 1);
        			GlStateManager.translate(-((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f), -(popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f), 0);
        			}
    		    if(intro > 0.5f){
    		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/sliderhead.png"));
    		    if((selectedValue != null && selectedValue == n) || this.isHoveringCoords((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f- 1.5f), popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
            		GL11.glTexParameteri(3553, 10240, 9729);
                GL11.glTexParameteri(3553, 10241, 9729);
    		    }else{
    		    	GL11.glTexParameteri(3553, 10240, 9728);
                    GL11.glTexParameteri(3553, 10241, 9728);
    		    }
    		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f) - 1.5f + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 0, 0,  23/2f, 25/2f,23/2f,25/2f);
    		    }
    		    if((hoverEffect > 1) || this.isHoveringCoords((float) (sliderLeft + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 23/2f, 25/2f, mouseX, mouseY)){
    		    	   
    		    	//GlStateManager.color(0.9f, 0.9f, 0.9f, 1f);
        			GlStateManager.translate((float) (sliderLeft + leftOffset + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f, 0);
        			GlStateManager.scale(1/hoverEffect, 1/hoverEffect, 1);
        			GlStateManager.translate(-((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f), -(popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f), 0);
        			GlStateManager.translate((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f, 0);
        			GlStateManager.scale(hoverEffect-0.18, hoverEffect-0.18, 1);
        			GlStateManager.translate(-((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f), -(popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f), 0);
        			
        			if(intro > 0.5f){
        				GlStateManager.color(1, 1, 1, (float) (5.5555555555555555555555555555556*(hoverEffect-1)));
        			
        				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/slidershadow.png"));
    		    	 GL11.glTexParameteri(3553, 10240, 9728);
    	                GL11.glTexParameteri(3553, 10241, 9728);
    		    	 Gui.INSTANCE.drawModalRectWithCustomSizedTexture((float) (popupLeft2 + 95 + 1.5f + (52 - 11)*(actualPercent) - 4.5f) - 1.5f + leftOffset, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f, 0, 0,  25/2f, 40/2f, 25/2f,40/2f);
        			}
        			GlStateManager.translate((float) (sliderLeft + leftOffset + (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f, popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f, 0);
        			GlStateManager.scale(1/(hoverEffect-0.18), 1/(hoverEffect-0.18), 1);
        			GlStateManager.translate(-((float) (sliderLeft  + leftOffset+ (52 - 11)*(actualPercent) - 4.5f- 1.5f) + 23/4f), -(popupTop2 + 25 + 10 + (34/2f)*y - 4.5f + 25/4f), 0);
        			
    		    }
    		    
    		    
    			FontUtil.jelloFontGui.drawNoBSString(n.getName(), popupLeft + 7, popupTop + 25 + 21/2f + (34/2f)*y, new Color(0,0,0,intro).getRGB());
    		}
    		y++;
    		
    	}
    	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanelOverlay.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(popupLeft - 9.5f - 5.5f*intro, popupTop - 9.5f, 0, 0,  119 + 71*intro/* + 1.5f*(1-intro)*/, 338/2f, 119 + 71*intro/*+ 1.5f*(1-intro)*/, 338/2f);
	
    	}
    }
    public boolean isHoveringCoords(float x, float y, float width, float height, int mouseX, int mouseY){
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height-0.5f;
	}
    
    public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
    public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/"+image+".png"));
        Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x,  y, 0, 0, width, height, width, height);
    }
    @Override
    public void initGui() {
    	percent = 1.23f;
		lastPercent = 1.23f;
		percent2 = 0.98f;
		lastPercent2 = 0.98f;
		outro = 1;
		lastOutro = 1;
    	for(Panel p : panels){
    		p.dragging = false;
    		p.startX = 0;
    		p.startY = 0;
    		p.lastX = 0;
    		p.lastY = 0;
    	}
    	music.initGui();
    	music.dragging = false;
    	music.startX = 0;
    	music.startY = 0;
    	music.lastX = 0;
    	music.lastY = 0;
    }
	
}
