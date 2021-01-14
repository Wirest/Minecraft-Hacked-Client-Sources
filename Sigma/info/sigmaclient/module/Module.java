package info.sigmaclient.module;

import com.google.gson.annotations.Expose;

import info.sigmaclient.Client;
import info.sigmaclient.event.EventListener;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.management.Saveable;
import info.sigmaclient.management.SubFolder;
import info.sigmaclient.management.agora.AgoraModule;
import info.sigmaclient.management.agora.AgoraSwitchModule;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.management.keybinding.Bindable;
import info.sigmaclient.management.keybinding.KeyHandler;
import info.sigmaclient.management.keybinding.Keybind;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.data.SettingsMap;
import info.sigmaclient.module.impl.other.ClickGui;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Module extends Saveable implements EventListener, Bindable, Toggleable {
    protected final static Minecraft mc = Minecraft.getMinecraft();
    @Expose
    protected final ModuleData data;
    @Expose
    protected final SettingsMap settings = new SettingsMap();
    private Keybind keybind;
    private boolean enabled;
    private String suffix;
    private boolean isHidden;
    protected Module premiumAddon = null;
    int enable = 0;
    Translate translate;
    private Timer timer = new Timer();
    public int getColor() {
        return color;
    }

    private int color;

    public Module(ModuleData data) {
        this.data = data;
        setFolderType(SubFolder.Module);
        setKeybind(new Keybind(this, data.key, data.mask));
        loadStatus();
        color = Colors.getColor((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()), 255);
        enable = 0;
        this.translate = new Translate(0, 0);
    }
    public Translate getTranslate(){
    	return this.translate;
    }
    public void setTranslate(int x, int y, int speed){
    	this.translate.interpolate(x, y, speed);
    }
    private static final File MODULE_DIR = FileUtils.getConfigFile("Mods");
    private static final File SETTINGS_DIR = FileUtils.getConfigFile("Sets");
    public int getCategoryColor(){
    	switch (this.data.type){
    	case Combat:
    		return Colors.getColor(150, 53, 46);
    	case Visuals:
    		return Colors.getColor(65, 118, 146);
    	case Other:
    		return Colors.getColor(65, 155, 103);
    	case Movement:
    		return Colors.getColor(200, 121, 0);  		
    	case Player:
    		return Colors.getColor(65, 134, 45);
    	case MiniGames:
    		return Colors.getColor(66, 111, 214);
    	}
    	return 0;
    }
    public static void saveStatus() {
        List<String> fileContent = new ArrayList<>();
        for (Module module : Client.getModuleManager().getArray()) {
            fileContent.add(String.format("%s:%s:%s:%s", module.getName(), module.isEnabled(), module.data.getKey(), module.isHidden));
        }
        FileUtils.write(MODULE_DIR, fileContent, true);
    }

    public static void saveSettings() {
        List<String> fileContent = new ArrayList<>();
        for (Module module : Client.getModuleManager().getArray()) {
            for (Setting setting : module.getSettings().values()) {
                if (!(setting.getValue() instanceof Options)) {
                    String displayName = module.getName();
                    String settingName = setting.getName();
                    String settingValue = setting.getValue().toString();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                } else {
                    String displayName = module.getName();
                    String settingName = setting.getName();
                    String settingValue = ((Options) setting.getValue()).getSelected();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                }
            }
        }
        FileUtils.write(SETTINGS_DIR, fileContent, true);
    }

    public void checkBypass() {

    }
    
    public static void loadStatus() {
        try {
            List<String> fileContent = FileUtils.read(MODULE_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String displayName = split[0];
                for (Module module : Client.getModuleManager().getArray()) {
                    if (module.getName().equalsIgnoreCase(displayName)) {
                        String strEnabled = split[1];
                        boolean enabled = Boolean.parseBoolean(strEnabled);
                        String key = split[2];
                        module.setKeybind(new Keybind(module, Integer.parseInt(key)));
                        if (split.length == 4) {
                            module.isHidden = Boolean.parseBoolean(split[3]);
                        }
                        if (enabled && !module.isEnabled()) {
                            module.enabled = true;
                            EventSystem.register(module);
                            module.onEnable();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
        try {
            List<String> fileContent = FileUtils.read(SETTINGS_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                for (Module module : Client.getModuleManager().getArray()) {
                    if (module.getName().equalsIgnoreCase(split[0])) {
                        Setting setting = getSetting(module.getSettings(), split[1]);
                        String settingValue = split[2];
                        if (setting != null) {
                            if (setting.getValue() instanceof Number) {
                                Object newValue = (StringConversions.castNumber(settingValue, setting.getValue()));
                                if (newValue != null) {
                                    setting.setValue(newValue);
                                }
                            } // If the setting is supposed to be a string
                            else if (setting.getValue().getClass().equals(String.class)) {
                                String parsed = settingValue.toString().replaceAll("_", " ");
                                setting.setValue(parsed);
                            } // If the setting is supposed to be a boolean
                            else if (setting.getValue().getClass().equals(Boolean.class)) {
                                setting.setValue(Boolean.parseBoolean(settingValue));
                            } else if (setting.getValue().getClass().equals(Options.class)) {
                                Options dank = ((Options) setting.getValue());
                                for (String str : dank.getOptions()) {
                                    if (str.equalsIgnoreCase(settingValue)) {
                                        dank.setSelected(settingValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Setting getSetting(SettingsMap map, String settingText) {
        settingText = settingText.toUpperCase();
        if (map.containsKey(settingText)) {
            return map.get(settingText);
        } else {
            for (String key : map.keySet()) {
                if (key.startsWith(settingText)) {
                    return map.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Handles the toggling of the module
     */
    @Override
    public void toggle() {
        enabled = !enabled;
        if (Client.getModuleManager().isSetup()) {
            checkBypass();
            saveStatus();
            saveSettings();
        }
        if (enabled) {
            timer.reset();
            EventSystem.register(this);
            onEnable();
        } else {
            // Save module data
            EventSystem.unregister(this);
            onDisable();
        }
    }

    @Override
    public void onEnable() {

    }
    public int getEnableTime(){
    	return enable;
    }
    public void setEnableTime(int x){
    	this.enable = x;
    }
    @Override
    public void onDisable() {

    }

    @Override
    public void onBindPress() {
        toggle();
        if (mc.thePlayer != null && !(this instanceof ClickGui) && !(this instanceof AgoraModule))
            mc.thePlayer.playSound("random.click", 0.15F, enabled ? 0.7F : 0.6F);
    }

    @Override
    public void onBindRelease() {

    }

    /**
     * Sets the current keybind to another.
     *
     * @param newBind
     */
    @Override
    public void setKeybind(Keybind newBind) {
        if (newBind == null) {
            return;
        }
        // Client init
        if (keybind == null) {
            keybind = newBind;
            KeyHandler.register(keybind);
            return;
        }
        // Not client setup
        boolean sameKey = newBind.getKeyInt() == keybind.getKeyInt();
        boolean sameMask = newBind.getMask() == keybind.getMask();
        if (sameKey && !sameMask) {
            KeyHandler.update(this, keybind, newBind);
        } else if (!sameKey) {
            if (KeyHandler.keyHasBinds(keybind.getKeyInt())) {
                KeyHandler.unregister(this, keybind);
            }
            KeyHandler.register(newBind);
        }
        keybind.update(newBind);
        data.key = keybind.getKeyInt();
        data.mask = keybind.getMask();
        if (Client.getModuleManager().isSetup()) {
            saveStatus();
        }
        /*
         * boolean noBind = newBind.getKeyInt() == Keyboard.CHAR_NONE; boolean
         * isRegistered = KeyHandler.isRegistered(keybind); if (isRegistered) {
         * if (noBind) { // Unegister the now-unused keybind
         * KeyHandler.unregister(keybind); } else { // Update the existing
         * keybind with new information int curKey = keybind.getKeyInt(); int
         * newKey = newBind.getKeyInt(); if (curKey == newKey) {
         * KeyHandler.update(keybind, newBind); } else {
         * KeyHandler.unregister(keybind); KeyHandler.register(newBind); } }
         * }else{ KeyHandler.register(newBind); } keybind.update(newBind); // if
         * (!isRegistered && !noBind) { // Register the new keybind
         * //KeyHandler.register(keybind); // } if (keybind != null) { data.key
         * = keybind.getKeyInt(); }
         */
    }

    /**
     * TODO: UN FUCK THE GOD FUCKING KEYBINDS
     * <p>
     * What I want to happen: - Client loads up - Module init - - Checks if
     * settings exist - - - If so load them and use the information for making
     * the keybind - - - Else use the default as provided in the constructor
     * <p>
     * Then later on: .bind Module key
     * <p>
     * - Check if the new key and current key match, if so just update the bind
     * (IE: adding a mask) - - If the keys dont match, unregister the old one,
     * register the new one
     */

    public static int getColor(ModuleData.Type type) {
        int color = -1;
        switch (type) {
            case Combat:
                color = Colors.getColor(135, 39, 39);
                break;
            case Player:
                color = Colors.getColor(90, 90, 90);
                break;
            case Movement:
                color = Colors.getColor(161, 180, 196);
                break;
            case Visuals:
                color = Colors.getColor(27, 198, 190);
                break;
            case Other:
                color = Colors.getColor(90, 90, 90);
                break;
            default:
                break;
        }
        return color;
    }

    public Keybind getKeybind() {
        return keybind;
    }

    /**
     * Adds a setting to the settings map
     *
     * @param key
     * @param setting
     * @return Returns if the setting was added
     */
    public boolean addSetting(String key, Setting setting) {
        if (settings.containsKey(key)) {
            return false;
        } else {
            settings.put(key, setting);
            return true;
        }
    }
    public Setting getSetting(String key) {
        return settings.get(key);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public SettingsMap getSettings() {
        return settings;
    }

    public String getName() {
        return data.name;
    }

    public String getDescription() {
        return data.description;
    }

    public ModuleData.Type getType() {
        return data.type;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean shouldRegister() {
        return true;
    }

    public Module getPremiumAddon() {
        return premiumAddon;
    }

    public void setPremiumAddon(Module premiumAddon) {
        this.premiumAddon = premiumAddon;
    }
}
