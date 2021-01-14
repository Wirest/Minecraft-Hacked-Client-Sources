package de.iotacb.client.module;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventManager;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.animations.easings.utilities.Progression;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.Minecraft;

public abstract class Module {
	
	private final String name;
	private final  String description;
	private String settingInfo;
	private final Category category;
	private int key, multiBindKey;
	
	private boolean enabled, isMultiBinded;
	
	private double animX, animY;
	
	private List<Value> values;
	
	private final Progression moduleProgressionX;
	private final Progression moduleProgressionY;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public Module() {
		ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);
		
		this.name = info.name();
		this.description = info.description();
		this.settingInfo = "";
		this.category = info.category();
		this.key = info.key();
		
		this.values = new ArrayList<Value>();
		this.moduleProgressionX = new Progression();
		this.moduleProgressionY = new Progression();
		
		onInit();
		addValue(new Value(name + "Show in List", true));
	}
	
	public abstract void onInit();
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public abstract void onToggle();
	
	public void updateValueStates() {
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSettingInfo() {
		return settingInfo;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public int getKey() {
		return key;
	}
	
	public int getMultiBindKey() {
		return multiBindKey;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isMultiBinded() {
		return isMultiBinded;
	}
	
	public Minecraft getMc() {
		return MC;
	}
	
	public Module setSettingInfo(String settingInfo) {
		this.settingInfo = settingInfo;
		return this;
	}
	
	public Module setKey(int key) {
		this.key = key;
		return this;
	}
	
	public Module setMultiBindKey(int multiBindKey) {
		this.multiBindKey = multiBindKey;
		return this;
	}
	
	public Module setEnabled(boolean enabled) {
		this.enabled = enabled;
		onToggle();
		this.moduleProgressionX.setValue(0);
		this.moduleProgressionY.setValue(0);
		if (enabled) {
			onEnable();
			if (this != Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class) && MC.thePlayer != null)
				Client.INSTANCE.getNotificationManager().addNotification(getName(), "Has been enabled!");
			EventManager.register(this);
		} else {
			onDisable();
			if (this != Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class) && MC.thePlayer != null)
				Client.INSTANCE.getNotificationManager().addNotification(getName(), "Has been disabled!");
			EventManager.unregister(this);
		}
		return this;
	}
	
	public Module setMultiBinded(boolean isMultiBinded) {
		this.isMultiBinded = isMultiBinded;
		return this;
	}
	
	public void setAnimX(double animX) {
		this.animX = animX;
	}
	
	public void setAnimY(double animY) {
		this.animY = animY;
	}
	
	public double getAnimX() {
		return animX;
	}
	
	public double getAnimY() {
		return animY;
	}
	
	public Module toggle() {
		setEnabled(!isEnabled());
		return this;
	}
	
	public Value addValue(Value value) {
		this.values.add(value);
		return value;
	}
	
	public Value addValue(String valueName, boolean state) {
		final Value value = new Value(getName() + valueName, state);
		this.values.add(value);
		return value;
	}
	
	public Value addValue(String valueName, double initValue, ValueMinMax minMax) {
		final Value value = new Value(getName() + valueName, initValue, minMax);
		this.values.add(value);
		return value;
	}
	
	public Value addValue(String valueName, String...modes) {
		final Value value = new Value(getName() + valueName, modes);
		this.values.add(value);
		return value;
	}
	
	public Value getValueByName(String valueName) {
		for (final Value value : this.values) {
			if (value.getValueName().equalsIgnoreCase(valueName)) {
				return value;
			}
		}
		return null;
	}
	
	public List<Value> getValues() {
		return values;
	}
	
	public Progression getModuleProgressionX() {
		return moduleProgressionX;
	}
	
	public Progression getModuleProgressionY() {
		return moduleProgressionY;
	}

}
