// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class NoFall extends Module
{
    public static String mode;
    TimeHelper time;
    
    static {
        NoFall.mode = "Vanilla";
    }
    
    public NoFall() {
        super("NoFall", "NoFall", 16777215, 49, Category.PLAYER);
        this.time = new TimeHelper();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Vanilla");
        mode.add("Intave");
        mode.add("New");
        mode.add("CC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "NoFallMode", this, "Vanilla", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setName("NoFall");
        if (!this.isEnabled()) {
            return;
        }
        if (Client.instance.settingManager.getSettingByName("NoFallMode").getValString().equalsIgnoreCase("Vanilla")) {
            this.setDisplayname("NoFall");
            this.Vanilla();
        }
        else if (Client.instance.settingManager.getSettingByName("NoFallMode").getValString().equalsIgnoreCase("New")) {
            this.setDisplayname("NoFall");
            this.New();
        }
        else if (Client.instance.settingManager.getSettingByName("NoFallMode").getValString().equalsIgnoreCase("CC")) {
            this.setDisplayname("NoFall");
            this.CC();
        }
        else if (Client.instance.settingManager.getSettingByName("NoFallMode").getValString().equalsIgnoreCase("Intave")) {
            this.setDisplayname("NoFall");
            this.Intave();
        }
    }
    
    private void CC() {
        final Minecraft mc = NoFall.mc;
        if (Minecraft.thePlayer.fallDistance > 3.5f) {
            final Minecraft mc2 = NoFall.mc;
            Minecraft.thePlayer.capabilities.isFlying = true;
            final Minecraft mc3 = NoFall.mc;
            Minecraft.thePlayer.onGround = true;
        }
    }
    
    private void Intave() {
        final Minecraft mc = NoFall.mc;
        if (Minecraft.thePlayer.fallDistance > 3.8f) {
            final Minecraft mc2 = NoFall.mc;
            Minecraft.thePlayer.capabilities.isFlying = true;
            final Minecraft mc3 = NoFall.mc;
            Minecraft.thePlayer.onGround = true;
            NoFall.mc.timer.timerSpeed = 1.05f;
            if (TimeHelper.hasReached(60L)) {
                final Minecraft mc4 = NoFall.mc;
                Minecraft.thePlayer.onGround = true;
            }
            else {
                final Minecraft mc5 = NoFall.mc;
                Minecraft.thePlayer.onGround = false;
            }
        }
        else {
            final Minecraft mc6 = NoFall.mc;
            Minecraft.thePlayer.capabilities.isFlying = false;
        }
    }
    
    public void Vanilla() {
        if (!this.isEnabled()) {
            return;
        }
        final Minecraft mc = NoFall.mc;
        if (Minecraft.thePlayer.fallDistance > 2.0f) {
            final Minecraft mc2 = NoFall.mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        else {
            final Minecraft mc3 = NoFall.mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
        }
    }
    
    public void New() {
        final Minecraft mc = NoFall.mc;
        if (Minecraft.thePlayer.fallDistance > 2.0f) {
            final Minecraft mc2 = NoFall.mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        else {
            final Minecraft mc3 = NoFall.mc;
            if (Minecraft.thePlayer.fallDistance > 12.0f) {
                final Minecraft mc4 = NoFall.mc;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
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
        final Minecraft mc = NoFall.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
        final Minecraft mc2 = NoFall.mc;
        Minecraft.thePlayer.capabilities.isFlying = false;
        NoFall.mc.timer.timerSpeed = 1.0f;
    }
}
