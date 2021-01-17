// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class FastLadder extends Module
{
    public FastLadder() {
        super("FastLadder", "FastLadder", 2419938, 0, Category.MOVEMENT);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("AAC");
        mode.add("CC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "FastLadderMode", this, "AAC", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("FastLadderMode").getValString().equalsIgnoreCase("CC")) {
            this.setDisplayname("FastLadder");
            this.CC();
        }
        else if (Client.instance.settingManager.getSettingByName("FastLadderMode").getValString().equalsIgnoreCase("AAC")) {
            this.setDisplayname("FastLadder");
            this.AAC();
        }
    }
    
    public void AAC() {
        final Minecraft mc = FastLadder.mc;
        if (Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = FastLadder.mc;
            final EntityPlayerSP thePlayer = Minecraft.thePlayer;
            thePlayer.motionY *= 1.025;
        }
        else {
            final Minecraft mc3 = FastLadder.mc;
            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
            thePlayer2.motionY *= 1.0;
        }
    }
    
    public void CC() {
        final Minecraft mc = FastLadder.mc;
        if (Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = FastLadder.mc;
            final EntityPlayerSP thePlayer = Minecraft.thePlayer;
            thePlayer.motionY *= 1.5;
        }
        else {
            final Minecraft mc3 = FastLadder.mc;
            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
            thePlayer2.motionY *= 1.0;
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
