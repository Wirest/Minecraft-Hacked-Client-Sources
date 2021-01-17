package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;
import java.util.*;

public class Godmode extends Module implements WorldHelper
{
    public int delay;
    private int[] nums;
    private String[] names;
    public static Value mode;
    
    static {
        Godmode.mode = new Value("Godmode", String.class, "Mode", "Slimes", new String[] { "Slimes", "Factions", "Sethome", "Teleport" });
    }
    
    public Godmode() {
        this.nums = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        this.names = new String[] { "MemeZ", "DankZ", "HakeZ", "GayZ", "LMAOZ" };
        this.setName("Godmode");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Godmode.mode);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (Godmode.mode.getSelectedOption().equalsIgnoreCase("Factions")) {
                ++this.delay;
                if (this.delay == 10) {
                    Godmode.mc.thePlayer.sendChatMessage("/f create " + this.getRandom(this.names) + this.getRandom(this.nums));
                    Godmode.mc.thePlayer.sendChatMessage("/f claim");
                }
                if (this.delay >= 80) {
                    Godmode.mc.thePlayer.sendChatMessage("/f disband");
                    this.delay = 0;
                }
            }
            if (Godmode.mode.getSelectedOption().equalsIgnoreCase("Slimes")) {
                Godmode.mc.thePlayer.isDead = true;
            }
            if (Godmode.mode.getSelectedOption().equalsIgnoreCase("Sethome")) {
                ++this.delay;
                if (this.delay == 50) {
                    Godmode.mc.thePlayer.sendChatMessage("/sethome");
                }
                if (this.delay == 70) {
                    Godmode.mc.thePlayer.sendChatMessage("/home");
                    this.delay = 0;
                }
            }
            if (Godmode.mode.getSelectedOption().equalsIgnoreCase("Teleport")) {
                ++this.delay;
                if (this.delay == 70) {
                    Godmode.mc.thePlayer.sendChatMessage("/tp " + Godmode.mc.thePlayer.getName());
                    this.delay = 0;
                }
            }
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Godmode.mc.thePlayer.isDead = false;
        this.delay = 0;
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    public String getRandom(final String[] array) {
        final int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    
    public int getRandom(final int[] array) {
        final int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
}
