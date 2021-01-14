package com.mentalfrostbyte.jello.jelloclickgui;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.tabgui.TabGUI.Cat;
import com.mentalfrostbyte.jello.ttf.GLUtils;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.Stencil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Panel {

	public float x;
	public float y;
	public Cat category;
	public boolean dragging;
	public float startX;
	public float startY;
	public float lastX;
	public float lastY;
	
	public float scroll;
	
	public float scrollTicks;
	public float lastScrollTicks;
	
	public Panel(float x, float y, Cat category) {
		this.x = x;
		this.y = y;
		this.category = category;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Cat getCategory() {
		return category;
	}

	public void setCategory(Cat category) {
		this.category = category;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state){
		dragging = false;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, JelloGui parent){
		if(mouseButton == 0){
		if(this.isHoveringCoords(x, y, 100, 30, mouseX, mouseY)){
			startX = mouseX;
    		startY = mouseY;
    		lastX = x;
    		lastY = y;
    		dragging = true;
		}
		
		 int count = 0;
	    for(Module m : Jello.getModulesInCategory(category)){
	    	boolean isHovering = isHoveringCoords(x, y + 25 + count*15 - scroll, 100, 15, mouseX, mouseY) && this.isMouseHoveringRect2(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, Jello.jgui.mouseX, Jello.jgui.mouseY);
	    	if(isHovering){
	    		m.toggle();
	    	}
	    	count++;
	    }
	}else if(mouseButton == 1){
		 int count = 0;
		    for(Module m : Jello.getModulesInCategory(category)){
		    	boolean isHovering = isHoveringCoords(x, y + 25 + count*15 - scroll, 100, 15, mouseX, mouseY) && this.isMouseHoveringRect2(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, Jello.jgui.mouseX, Jello.jgui.mouseY);
		    	if(isHovering){
		    		parent.selectedModule = m;
		    		parent.selectedCategory = this;
		    	}
		    	count++;
		    }
	}
	}
	
	public void onTick(){
		lastScrollTicks = scrollTicks;
		if(scrollTicks > 0){
			scrollTicks--;
		}
		int count = 0;
		for(Module m : Jello.getModulesInCategory(category)){
	    	boolean isHovering = isHoveringCoords(x, y + 25 + count*15 - scroll, 100, 15, Jello.jgui.mouseX, Jello.jgui.mouseY) && this.isMouseHoveringRect2(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, Jello.jgui.mouseX, Jello.jgui.mouseY);
	    	m.lastHoverPercent = m.hoverPercent;
	    	if(isHovering){
	    		if(m.hoverPercent > 0){
		    		m.hoverPercent -= 0.25;
		    		
		    	}
	    	}else if(m.hoverPercent < 1){
	    		m.hoverPercent += 0.25;
	    		}
	    	
	    	if(m.hoverPercent > 1){
	    		m.hoverPercent = 1;
	    	}
	    	if(m.hoverPercent < 0){
	    		m.hoverPercent = 0;
	    	}
	    	count++;
	    }
		
	}
	public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
	public void drawPanel(int mouseX, int mouseY, float opacity, boolean hideShadow, float leftIntro, float rightIntro){
		GlStateManager.disableAlpha();
		if(dragging){
    		x = lastX + mouseX - startX;
    		y = lastY + mouseY - startY;
    	}
		GlStateManager.color(1, 1, 1, opacity);
		
		if(!hideShadow){
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f, y - 19/2f, 0, 0,  238/2f, 338/2f, 238/2f, 338/2f);
		}else{
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanel.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f + 9.5f, y - 19/2f + 9.5f, 9.5f, 9.5f,  238/2f - 9.5f*2, 338/2f - 9.5f*2, 238/2f, 338/2f);
		}
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	    
	    FontUtil.jelloFontAddAlt.drawNoBSCenteredString(category.name, x + 100/2f, y + 17/2f, new Color(1f, 1f, 1f, opacity).getRGB());
	  
	    if(!hideShadow){
	    Stencil.write(false);
	    Gui.drawFloatRect(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, -1);
		Stencil.erase(true);
	    }
	    int count = 0;
	    for(Module m : Jello.getModulesInCategory(category)){
	    	float hoverTrans = smoothTrans(m.hoverPercent, m.lastHoverPercent);
	    	boolean isHovering = isHoveringCoords(x, y + 25 + count*15 - scroll, 100, 15, mouseX, mouseY) && this.isMouseHoveringRect2(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, mouseX, mouseY);
	    	
	    	Color h = Color.getHSBColor(270, 0, 0.8f + (0.16f*hoverTrans));
	    	
	    	Gui.drawFloatRect(hideShadow ? leftIntro : x, y + 25 + count*15 - scroll, hideShadow ? rightIntro :  x + 100/* + (hideShadow ? 1.5f*(1-intro) : 0)*/, y + 25 + 15 + count*15 - scroll, m.isToggled() ? new Color(42, 165, 255, (int)(opacity*(200 + (55*hoverTrans)))).getRGB() : new Color(h.getRed(), h.getGreen(), h.getBlue(), (int)(opacity*255)).getRGB());
			GlStateManager.color(1, 1, 1, 1);
	    	FontUtil.jelloFontAddAlt3.drawNoBSCenteredString(m.getName(), x + 100/2f, y + 58/2f + count*15 - scroll, m.isToggled() ?new Color(1f, 1f, 1f, opacity).getRGB() : new Color(0f, 0f, 0f, opacity).getRGB());
	    	count++;
	    }
	    if (Mouse.hasWheel() && this.isMouseHoveringRect2(x - 19/2f + 9.5f, y - 19/2f + 9.5f + 25, x - 19/2f - 9.5f + 238/2f, y - 19/2f - 9.5f + 338/2f, mouseX, mouseY) && (count-1)*15 > 100) {
	   
            final int wheel = Jello.jgui.mouseWheel;
            if (wheel < 0) {
            	if(scroll < 100){
                    this.scroll += 18.5f;
                	}
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
                if(scroll > (count-1)*15 - 110){
                	scroll = (count-1)*15 - 110;
                }
                if(scrollTicks <= 20 && scrollTicks > 0){
                	scrollTicks = 20;
                	lastScrollTicks =20;
                }else if(scrollTicks ==0){
                scrollTicks = 30;
            	lastScrollTicks = 30;
                }
            }
            else if (wheel > 0) {
                this.scroll -= 18.5f;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
                if(scroll > (count-1)*15 - 110){
                }
                if(scrollTicks <= 20 && scrollTicks > 0){
                	scrollTicks = 20;
                	lastScrollTicks = 20;
                }else if(scrollTicks ==0){
                	lastScrollTicks = 30;
                scrollTicks = 30;
                }
            }
        }
	    if(!hideShadow){
	    Stencil.dispose();
	    }
	    GlStateManager.disableAlpha();
	    GlStateManager.color(1, 1, 1, 1);
	    float smoothScroll = smoothTrans(scrollTicks, lastScrollTicks);
	    GlStateManager.enableBlend();
	    GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + 100 - 2.5f - 5.5f, y + 25 + 2.5f, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
        Gui.drawFloatRect(x + 100 - 2.5f, y + 25 + 2.5f + 5.5f/2f, x + 100 - 2.5f - 5.5f, y + 25 + 125 - 2.5f - 2.5f, new Color(0,0,0,opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
        
        GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
        GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + 100 - 2.5f - 5.5f, y + 25 + 125 - 2.5f - 2.5f, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
	   // System.out.println(count-1 + " " + category);
	    float height = (50/((Math.max(count-1, 1))))*10;
	    float percent = (scroll/((Math.max(count-1, 1))*15 - 110))*100;
	    float startY = 0 + percent - height*(percent/100);
	    float endY = -100  + percent + height - height*(percent/100);
	    
	    GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    
		Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + 100 - 2.5f - 5.5f, y + 25 + 2.5f + startY, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
        
	    Gui.drawFloatRect(x + 100 - 2.5f, y + 25 + 2.5f + 5.5f/2f + startY, x + 100 - 2.5f - 5.5f, y + 25 + 125 - 2.5f - 2.5f + endY, new Color(0,0,0,opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
        
        GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
        GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x + 100 - 2.5f - 5.5f, y + 25 + 125 - 2.5f - 2.5f + endY, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    
	    GlStateManager.color(1, 1, 1, 1);
	    
        GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.color(1, 1, 1, opacity);
	    if(!hideShadow){
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/JelloPanelOverlay.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x - 19/2f, y - 19/2f, 0, 0,  238/2f, 338/2f, 238/2f, 338/2f);
	    }
	}
	public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/"+image+".png"));
        Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x,  y, 0, 0, width, height, width, height);
    }
	
	public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
	
	public boolean isHoveringCoords(float x, float y, float width, float height, int mouseX, int mouseY){
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height-0.5f;
	}
	
}
