package com.mentalfrostbyte.jello.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

import com.ibm.icu.util.ULocale.Category;
import com.mentalfrostbyte.jello.hud.JelloHud;
import com.mentalfrostbyte.jello.hud.Notification;
import com.mentalfrostbyte.jello.hud.NotificationManager;
import com.mentalfrostbyte.jello.tabgui.TabGUI;
import com.mentalfrostbyte.jello.util.Value;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ResourceLocation;
import optifine.Config;

public class Module {
	
	protected static Minecraft mc = Minecraft.getMinecraft();

	private String name;
	private String displayname;
	public int keyCode;
	private boolean toggled;
	 protected ArrayList<Value> values;
	 private Category category;
	 public TabGUI.Cat jelloCat;
	 private double toggleAnimation;
		public float animation;
		public float animHeight;
		
		public float hoverPercent;
		public float lastHoverPercent;
	 
	public Module(String name, int keyCode) {
		this.name = name;
		this.displayname = name;
		this.keyCode = keyCode;
		this.toggled = false;
		this.values = new ArrayList();

	}
	public void toggle() {
		
		this.toggled = !this.toggled;
		if(this.toggled) {
			onEnable();
		}else {
			onDisable();
		}

				if(mc.thePlayer != null){
		NotificationManager.notify(toggled ? "Enabled":"Disabled", this.getDisplayName(), 250);
				}
	}
	
	public String getDisplayName() {
		return displayname;
	}
	
	public void addValue(Value value)
    {
      this.values.add(value);
    }

	public void onEnable() { }
	
	public void onDisable() { }
	
	public void onUpdate() { }
	
	public void onRender() { }
	
	public String getName() {
		return this.name;
	}
	
	public double getToggleAnimation() {
		return toggleAnimation;
	}
	public void setToggleAnimation(double toggleAnimation) {
		this.toggleAnimation = toggleAnimation;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getKeyCode() {
		return this.keyCode;
	}
	
	public void setKeyCode(int keycode) {
		this.keyCode = keycode;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	
	}
	
	public boolean isToggled() {
		return this.toggled;
	}
	
	public boolean onSendChatMessage(String s){
		return true;
	}
	
	public boolean onRecieveChatMessage(S02PacketChat packet){
		return true;
	}
	
	public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category category) {
        this.category = category;
    }
   


    
    public ArrayList<Value> getValues()
    {
      return this.values;
    }
    

    public Value getValue(String label) {
        for (Value value : this.getValues()) {
            if (!value.getName().equalsIgnoreCase(label)) continue;
            return value;
        }
        return null;
    }
	public void setDisplayName(String string) {
		displayname = string;
		
	}
    
    
    

}
