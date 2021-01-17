package me.ihaq.iClient.modules;

import java.io.IOException;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.file.FileManager;
import me.ihaq.iClient.file.files.Binds;
import me.ihaq.iClient.file.files.Modules;
import net.minecraft.client.Minecraft;

public class Module {

	protected static Minecraft mc = Minecraft.getMinecraft();

	public String name;
	public int keyCode;
	public Category c;
	private boolean toggled;
	public String mode;

	public Module(String name, int keyCode, Category c, String m) {
		this.name = name;
		this.keyCode = keyCode;
		this.c = c;
		this.toggled = false;
		this.mode = m;
	}

	public void toggle() {
		if (this.toggled) {
			this.onDisable();
			this.toggled = false;
		} else {
			this.onEnable();
			this.toggled = true;
			try {
				FileManager.getFile(Modules.class).saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onToggle() {
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public void onRender() {
	}

	public enum Category {
		COMBAT, MOVEMENT, RENDER, PLAYER
	}
	
	public Category getCategory(){
		return c;		
	}

	public boolean getModCategory(Category cat) {
		return c == cat;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	public void setKeyCode(int keycode) {
		this.keyCode = keycode;
		try {
			FileManager.getFile(Binds.class).saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		try {
			FileManager.getFile(Modules.class).saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disable() {
		this.toggled = false;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setMode(String m) {
		this.mode = m;
	}

	public String getMode() {
		return this.mode;
	}

	public static Module getModuleByString(String modName) {
		for (Module m : Envy.MODULE_MANAGER.getModules()) {
			if (m.name.equalsIgnoreCase(modName)) {
				return m;
			}
		}
		return null;
	}

}