package store.shadowclient.client.module.misc;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Configs extends Module
{
    public Configs() {
        super("Configs", 0, Category.MISC);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Watchdog");
        Shadow.instance.settingsManager.rSetting(new Setting("Config", this, "Watchdog", options));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
    	
        final String mode = Shadow.instance.settingsManager.getSettingByName("Config").getValString();
        
        if (mode.equalsIgnoreCase("Watchdog")) {
            for (final Module m : Shadow.instance.moduleManager.getModules()) {
                if (m.isToggled()) {
                    m.toggle();
                }
            }
            
            //AURA
            this.setting("Range").setValDouble(3.5);
            this.setting("Delay").setValDouble(12);
            this.setting("CircleESP Range").setValDouble(1.4);
            this.setting("AutoBlock").setValBoolean(true);
            this.setting("Animals").setValBoolean(false);
            this.setting("Monsters").setValBoolean(false);
            this.setting("Teams").setValBoolean(true);
            this.setting("TargetHUD").setValBoolean(true);
            
            //AUTOARMOR
            Shadow.instance.moduleManager.getModuleByName("AutoArmor").toggle();;
            this.setting("Armor Speed").setValDouble(5);
            
            //ANTIBOT
            this.setting("AntiBot Mode").setValString("Watchdog");
            Shadow.instance.moduleManager.getModuleByName("AntiBot").toggle();

            //SCAFFOLD
            this.setting("Tower").setValBoolean(false);
            this.setting("Silent").setValBoolean(false);
            
            //LONGJUMP
            this.setting("LongJump Mode").setValString("Hypixel");
            
            //DISABLER
            this.setting("Disabler Mode").setValString("Watchdog");
            
            //SPEED
            this.setting("Speed Mode").setValString("HypixelBHop");
            this.setting("Speed LagBack").setValBoolean(true);
            
            //VELOCITY
            Shadow.instance.moduleManager.getModuleByName("Velocity").toggle();
            this.setting("Velocity Mode").setValString("HypixelNew");
            
            //TARGETSTRAFE
            this.setting("CircleESP").setValBoolean(false);
            
            //INV MANAGER
            Shadow.instance.moduleManager.getModuleByName("InvManager").toggle();
            this.setting("Sword Slot").setValDouble(1);
            this.setting("InvManager Delay").setValDouble(100);
            
            //FLY
            this.setting("Fly Mode").setValString("Hypixel");
            this.setting("Timer Boost").setValBoolean(true);
            this.setting("Timer Speed").setValDouble(2.4);
            
            //INV MANAGER
            Shadow.instance.moduleManager.getModuleByName("Disabler").toggle();
            this.setting("Sword Slot").setValDouble(1);
            
            //CHESTSTEALER
            Shadow.instance.moduleManager.getModuleByName("ChestStealer").toggle();
            this.setting("CS Delay").setValDouble(100);
            
            //AMBIANCE
            Shadow.instance.moduleManager.getModuleByName("Ambiance").toggle();
            this.setting("Time").setValDouble(15000);
            
            //NOSCOREBOARD
            Shadow.instance.moduleManager.getModuleByName("NoScoreboard").toggle();
            this.setting("Position").setValDouble(200);
            
            //INVMOVE
            Shadow.instance.moduleManager.getModuleByName("InvMove").toggle();
            this.setting("Sneak").setValBoolean(false);
            
            //HUD
            Shadow.instance.moduleManager.getModuleByName("HUD").toggle();
            
            //MODULES WITHOUT VALUE
            Shadow.instance.moduleManager.getModuleByName("Fullbright").toggle();
            Shadow.instance.moduleManager.getModuleByName("NoSlowdown").toggle();
            Shadow.instance.moduleManager.getModuleByName("Sprint").toggle();
            Shadow.instance.moduleManager.getModuleByName("ESP2D").toggle();
            Shadow.instance.moduleManager.getModuleByName("SwordAnimation").toggle();
            Shadow.instance.moduleManager.getModuleByName("AutoRespawn").toggle();
            Shadow.instance.moduleManager.getModuleByName("Cosmetics").toggle();
            Shadow.instance.moduleManager.getModuleByName("FastPlace").toggle();
            Shadow.instance.moduleManager.getModuleByName("NameProtect").toggle();
            Shadow.instance.moduleManager.getModuleByName("NoWeb").toggle();
            Shadow.instance.moduleManager.getModuleByName("AutoSword").toggle();
            Shadow.instance.moduleManager.getModuleByName("AutoSoup").toggle();
            Shadow.instance.moduleManager.getModuleByName("ChestESP").toggle();
            Shadow.instance.moduleManager.getModuleByName("AimAssist").toggle();
            Shadow.instance.moduleManager.getModuleByName("Skeletons").toggle();
            Shadow.instance.moduleManager.getModuleByName("Reach").toggle();
            Shadow.instance.moduleManager.getModuleByName("AutoPlay").toggle();
            Shadow.instance.moduleManager.getModuleByName("AutoTool").toggle();
            Shadow.instance.moduleManager.getModuleByName("WallHack").toggle();
            Shadow.instance.moduleManager.getModuleByName("KillSults").toggle();
            Shadow.instance.moduleManager.getModuleByName("Detector").toggle();
            Shadow.instance.moduleManager.getModuleByName("Trajectories").toggle();
            Shadow.instance.moduleManager.getModuleByName("ItemPhysics").toggle();
            Shadow.instance.moduleManager.getModuleByName("NoHurtCam").toggle();
        }
    }
}