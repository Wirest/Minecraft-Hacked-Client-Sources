// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.misc;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import java.util.Random;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.Client;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class Spammer extends Module
{
    TimeHelper time;
    public static String message;
    public static String meme;
    
    static {
        Spammer.message = String.valueOf(Client.instance.ClientName) + " / Hush " + Client.instance.ClientVersion + " by " + Client.instance.ClientCoder;
        Spammer.meme = "Intave, is good!";
    }
    
    public Spammer() {
        super("Spammer", "Spammer", 14620696, 0, Category.MISC);
        this.time = new TimeHelper();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Normal");
        mode.add("Meme");
        Client.instance.settingManager.rSetting(new Setting("Mode", "SpammerMode", this, "Normal", mode));
        Client.instance.settingManager.rSetting(new Setting("@all", this, false));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("SpammerMode").getValString().equalsIgnoreCase("Normal")) {
            this.Normal();
        }
        else if (Client.instance.settingManager.getSettingByName("SpammerMode").getValString().equalsIgnoreCase("Meme")) {
            this.Meme();
        }
    }
    
    private void Normal() {
        if (TimeHelper.hasReached(5000L)) {
            if (!Client.instance.settingManager.getSettingByName("@all").getValBoolean()) {
                final String[] rdmString = { String.valueOf(Spammer.message) + " | Best Client!, Buy now!" };
                final Random rdm = new Random();
                final Minecraft mc = Spammer.mc;
                Minecraft.thePlayer.sendChatMessage((rdmString != null) ? (String.valueOf(String.valueOf(rdm.nextInt(9999))) + " | " + rdmString[rdm.nextInt(rdmString.length)] + " | " + rdm.nextInt(9999)) : "");
                TimeHelper.reset();
            }
            else if (TimeHelper.hasReached(5000L)) {
                final String[] rdmString = { String.valueOf(Spammer.message) + " | Best Client!, Buy now!" };
                final Random rdm = new Random();
                final Minecraft mc2 = Spammer.mc;
                Minecraft.thePlayer.sendChatMessage((rdmString != null) ? (String.valueOf(String.valueOf(new StringBuilder("@all ").append(rdm.nextInt(9999)).toString())) + " | " + rdmString[rdm.nextInt(rdmString.length)] + " | " + rdm.nextInt(9999)) : "");
                TimeHelper.reset();
            }
        }
    }
    
    private void Meme() {
        if (!Client.instance.settingManager.getSettingByName("@all").getValBoolean()) {
            if (TimeHelper.hasReached(5000L)) {
                final Random rdm = new Random();
                final boolean rdmString = true;
                final Minecraft mc = Spammer.mc;
                Minecraft.thePlayer.sendChatMessage(rdmString ? (String.valueOf(String.valueOf(rdm.nextInt(9999))) + " | " + Spammer.meme + " | " + rdm.nextInt(9999)) : Spammer.meme);
                TimeHelper.reset();
            }
        }
        else if (TimeHelper.hasReached(5000L)) {
            final Random rdm = new Random();
            final boolean rdmString = true;
            final Minecraft mc2 = Spammer.mc;
            Minecraft.thePlayer.sendChatMessage(rdmString ? (String.valueOf(String.valueOf(new StringBuilder("@all ").append(rdm.nextInt(9999)).toString())) + " | " + Spammer.meme + " | " + rdm.nextInt(9999)) : Spammer.meme);
            TimeHelper.reset();
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
