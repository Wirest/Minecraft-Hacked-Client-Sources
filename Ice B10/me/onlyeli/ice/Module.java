package me.onlyeli.ice;

import java.io.File;
import java.util.*;
import me.onlyeli.ice.events.*;
import me.onlyeli.ice.main.Ice;
import me.onlyeli.ice.utils.FileUtils;
import me.onlyeli.ice.utils.FriendUtils;
import net.minecraft.client.*;

public class Module {
	
	protected static Minecraft mc;
	private String name;
	private int keyCode;
	private boolean toggled;
	private static List<Module> moduleList;
	
	 public static Minecraft mc() {
	        return Minecraft.getMinecraft();
	    }
	
	public Module (final String name, final int keyCode, final Category category) {
		this.keyCode = keyCode;
		this.toggled = false;
	}
	
	public void toggle() { 
		this.toggled = !this.toggled;
		if(this.toggled) {
			onEnable();
		}else {
			onDisable();
		}
	}

	public void onEnable() { }
	
	public void onDisable() { }
	
	public void onUpdate() { }
	
	public void onRender() { }
	
	public void setModName(final String name) {
		this.name = name;
	}
		
	public int keyCode() {
		return this.keyCode;
	}
	
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public int getkeyCode() {
		return this.keyCode;
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public boolean isToggled() {
		return this.toggled;
	}
	
	public Category getCategory() {
		return null;
	}
	
	public String getModName() {
		return this.name;
	}
	
	public int getBind() {
		return this.keyCode;
	}
	
	public void setBind(int keyBind) {
		this.keyCode = keyBind;
	}
			
	public void onEvent(Events event) {
		
	}
	
}