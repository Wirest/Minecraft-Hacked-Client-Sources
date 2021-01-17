// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.nico.hush.utils.PlayerUtils;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Bhop extends Module
{
    public Bhop() {
        super("Bhop", "Bhop", 16777215, 0, Category.MOVEMENT);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Legit");
        mode.add("Motion");
        Client.instance.settingManager.rSetting(new Setting("Mode", "BhopMode", this, "Legit", mode));
        Client.instance.settingManager.rSetting(new Setting("Motion §l\u2192", "BhopMotion", this, 1.3, 0.5, 2.0, false));
        Client.instance.settingManager.rSetting(new Setting("MotionY §l\u2192", "MotionY", this, 1.0, 1.0, 2.0, false));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("BhopMode").getValString().equalsIgnoreCase("Legit")) {
            this.Legit();
        }
        else if (Client.instance.settingManager.getSettingByName("BhopMode").getValString().equalsIgnoreCase("Motion")) {
            this.Motion();
        }
    }
    
    private void Motion() {
        Bhop.mc.gameSettings.keyBindJump.pressed = false;
        if (PlayerUtils.playeriswalking()) {
            final Minecraft mc = Bhop.mc;
            if (!Minecraft.thePlayer.isOnLadder()) {
                final Minecraft mc2 = Bhop.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc3 = Bhop.mc;
                    Minecraft.thePlayer.jump();
                    final Minecraft mc4 = Bhop.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    thePlayer.motionX *= Client.instance.settingManager.getSettingByName("BhopMotion").getValDouble();
                    final Minecraft mc5 = Bhop.mc;
                    final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                    thePlayer2.motionZ *= Client.instance.settingManager.getSettingByName("BhopMotion").getValDouble();
                    final Minecraft mc6 = Bhop.mc;
                    final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                    thePlayer3.motionY *= Client.instance.settingManager.getSettingByName("MotionY").getValDouble();
                }
            }
        }
    }
    
    private void Legit() {
        if (PlayerUtils.playeriswalking()) {
            final Minecraft mc = Bhop.mc;
            if (!Minecraft.thePlayer.isOnLadder()) {
                final Minecraft mc2 = Bhop.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc3 = Bhop.mc;
                    Minecraft.thePlayer.setSprinting(true);
                    final Minecraft mc4 = Bhop.mc;
                    Minecraft.thePlayer.jump();
                }
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
    }
}
