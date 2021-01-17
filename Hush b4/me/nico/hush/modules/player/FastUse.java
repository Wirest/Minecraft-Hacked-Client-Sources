// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class FastUse extends Module
{
    public static String mode;
    
    static {
        FastUse.mode = "CC";
    }
    
    public FastUse() {
        super("FastUse", "FastUse", 38653, 0, Category.PLAYER);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("CC");
        mode.add("AAC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "FastUseMode", this, "AAC", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setName("FastUse");
        if (!this.isEnabled()) {
            return;
        }
        if (Client.instance.settingManager.getSettingByName("FastUseMode").getValString().equalsIgnoreCase("CC")) {
            this.setDisplayname("FastUse");
            this.CC();
        }
        else if (Client.instance.settingManager.getSettingByName("FastUseMode").getValString().equalsIgnoreCase("AAC")) {
            this.setDisplayname("FastUse");
            this.AAC();
        }
    }
    
    public void AAC() {
        if (!this.isEnabled()) {
            return;
        }
        final Minecraft mc = FastUse.mc;
        if (Minecraft.thePlayer.isEating()) {
            final Minecraft mc2 = FastUse.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = FastUse.mc;
                if (!Minecraft.thePlayer.isOnLadder()) {
                    final Minecraft mc4 = FastUse.mc;
                    if (!Minecraft.thePlayer.isInWater()) {
                        final Minecraft mc5 = FastUse.mc;
                        if (!Minecraft.thePlayer.isInLava()) {
                            FastUse.mc.timer.timerSpeed = 1.2f;
                            return;
                        }
                    }
                }
            }
        }
        FastUse.mc.timer.timerSpeed = 1.0f;
    }
    
    public void CC() {
        final Minecraft mc = FastUse.mc;
        if (Minecraft.thePlayer.isEating()) {
            final Minecraft mc2 = FastUse.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = FastUse.mc;
                if (!Minecraft.thePlayer.isOnLadder()) {
                    final Minecraft mc4 = FastUse.mc;
                    if (!Minecraft.thePlayer.isInWater()) {
                        final Minecraft mc5 = FastUse.mc;
                        if (!Minecraft.thePlayer.isInLava()) {
                            FastUse.mc.timer.timerSpeed = 2.0f;
                            return;
                        }
                    }
                }
            }
        }
        FastUse.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        FastUse.mc.timer.timerSpeed = 1.0f;
    }
}
