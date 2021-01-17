// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.misc;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.Client;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class AutoConfig extends Module
{
    public static String config;
    
    static {
        AutoConfig.config = "";
    }
    
    public AutoConfig() {
        super("AutoConfig", "AutoConfig", 16777215, 0, Category.MISC);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = AutoConfig.mc;
        if (Minecraft.thePlayer != null && AutoConfig.mc.theWorld != null && AutoConfig.mc.getCurrentServerData() != null) {
            final String serverip = AutoConfig.mc.getCurrentServerData().serverIP;
            if (serverip.toLowerCase().contains("cubecraft") && AutoConfig.config != "CubeCraft") {
                Client.instance.commandManager.execute(".config CubeCraft");
                AutoConfig.config = "CubeCraft";
            }
            if (serverip.toLowerCase().contains("gommehd") && AutoConfig.config != "GommeHD") {
                final Minecraft mc2 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config GommeHD");
                AutoConfig.config = "GommeHD";
            }
            if (serverip.toLowerCase().contains("mc-central") && AutoConfig.config != "MC-Central") {
                final Minecraft mc3 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config MC-Central");
                AutoConfig.config = "MC-Central";
            }
            if (serverip.toLowerCase().contains("neruxvace") && AutoConfig.config != "NeruxVace") {
                final Minecraft mc4 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config NeruxVace");
                AutoConfig.config = "NeruxVace";
            }
            if (serverip.toLowerCase().contains("hive") && AutoConfig.config != "HiveMC") {
                final Minecraft mc5 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config HiveMC");
                AutoConfig.config = "HiveMC";
            }
            if (serverip.toLowerCase().contains("mineplex") && AutoConfig.config != "MinePlex") {
                final Minecraft mc6 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config MinePlex");
                AutoConfig.config = "MinePlex";
            }
            if (serverip.toLowerCase().contains("aac") && AutoConfig.config != "AAC") {
                final Minecraft mc7 = AutoConfig.mc;
                Minecraft.thePlayer.sendChatMessage(".config AAC");
                AutoConfig.config = "AAC";
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        AutoConfig.config = "";
    }
}
