package nivia.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import nivia.Pandora;
import nivia.events.Event;
import nivia.files.modulefiles.Modules;
import nivia.managers.PropertyManager.Property;
import nivia.modules.combat.KillAura;
import nivia.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Module {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private String name, tag, suffix = "";
    private int keybind;
    public boolean State;
    private boolean visible;
    public int color;
    private Category category;
    private String description;
    private String[] alias;
    private ArrayList<ModuleMode> modes = new ArrayList<>();

    public Module(String name, int keybind, int color, Category category, String description, String[] alias,
	    boolean visible) {
	this.name = tag = name;
	this.keybind = keybind;
	this.color = color;
	this.category = category;
	this.description = description;
	this.alias = alias;
	this.visible = visible;
        this.consumerMap = new HashMap<>();
	addCommand();
    }

    public Module(String name, int keybind, int color, Category category, String description, String[] alias) {
	this.name = tag = name;
	this.keybind = keybind;
	this.color = color;
	this.category = category;
	this.description = description;
	this.alias = alias;
        this.consumerMap = new HashMap<>();
	addCommand();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getTag() {
	return tag;
    }

    public void setTag(String name) {
	this.tag = name;
    }

    public void setSuffix(String suffix) {
	this.suffix = suffix;
    }

    public String getSuffix() {
    	if (this.suffix.isEmpty())
            return "";
        String s = StringUtils.upperCase(suffix.substring(0, 1)) + StringUtils.lowerCase(suffix.substring(1));
        return EnumChatFormatting.GRAY + s;
    }

    public int getKeybind() {
	return keybind;
    }

    public void setKeybind(int keybind) {
	this.keybind = keybind;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean vis) {
	this.visible = vis;
    }

    public int getColor() {
	return color;
    }

    public void setColor(int color) {
	this.color = color;
    }

    public Category getCategory() {
	return category;
    }

    public String getDescription() {
	return description;
    }

    public String[] getAlias() {
	return alias;
    }

    public boolean getState() {
	return State;
    }

    public enum Category {
	COMBAT, MISCELLANEOUS, EXPLOITS, MOVEMENT, PLAYER, RENDER, GHOST, NONE;
    	public String getName() {
    		return StringUtils.capitalize(name().toLowerCase());
		}
    }

    public final boolean isCategory(Category s) {
	if (s.equals(category))
	    return true;
	else
	    return false;
    }
	public ArrayList<ModuleMode> getModes(){
    	return modes;
	}
	public ModuleMode getMode(String name) {
		for(ModuleMode m : modes) {
			if(m.getName().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}
	public void addMode(ModuleMode... m) {
		Collections.addAll(modes, m);
	}
    public void setState(boolean state) {
	State = state;
	if (State)
	    onEnable();
	else
	    onDisable();
    }

    protected void onEnable() {
    	Pandora.getEventManager().register(this);
    }

    protected void onDisable() {
    	Pandora.getEventManager().unregister(this);
    }

    // TODO: Add Mode shit
    public void Toggle() {
	State = !State;
	if (State)
	    onEnable();
	else
	    onDisable();
		try {
			Pandora.getFileManager().getFile(Modules.class).saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private final Map<Class<Event>, Consumer<Event>> consumerMap;

    protected <T extends Event> void addHook(Class<T> clazz, Consumer<T> consumer) {
	consumerMap.put((Class<Event>) clazz, (Consumer<Event>) consumer);
    }

    public ArrayList<Property> getModuleProperties() {

	return Pandora.getPropertyManager().getPropertiesFromModule(this);
    }

    public void logValues() {
    	Pandora.getPropertyManager().getPropertiesFromModule(this).forEach(p -> {
    	    if (p.value instanceof Boolean)
    			Logger.logChat(EnumChatFormatting.YELLOW + StringUtils.capitalize(p.getName()) + ": "
    				+ ((boolean) p.value ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + p.value);
    		    else if (p.value instanceof KillAura.AuraMode) {
    			Logger.logChat(EnumChatFormatting.YELLOW + StringUtils.capitalize(p.getName()) + ": "
    				+ EnumChatFormatting.GRAY + ((KillAura.AuraMode) p.value).getName());
    		    } else
    			Logger.logChat(EnumChatFormatting.YELLOW + StringUtils.capitalize(p.getName()) + ": "
    				+ EnumChatFormatting.GRAY + p.value);
    	 });
    }

    public void logValue(Property p) {
	if (p.value instanceof Boolean)
	    Logger.logChat(EnumChatFormatting.YELLOW + StringUtils.capitalize(p.getName()) + ": "
		    + ((boolean) p.value ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + p.value);
	else
	    Logger.logChat(EnumChatFormatting.YELLOW + StringUtils.capitalize(p.getName()) + ": "
		    + EnumChatFormatting.GRAY + p.value);
    }

    protected void addCommand() {
    }

}
