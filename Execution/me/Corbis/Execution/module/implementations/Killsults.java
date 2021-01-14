package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Killsults extends Module {
    public Setting mode;
    public Killsults(){
        super("KillSults", Keyboard.KEY_NONE, Category.COMBAT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Mc-Central");
        options.add("Mineplex");
        Execution.instance.getSettingsManager().rSetting(mode = new Setting("Killsult Mode", this, "Hypixel", options));
    }

    @EventTarget
    public void onReceive(EventReceivePacket event){
        if(event.getPacket() instanceof S02PacketChat){
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            String message = packet.getChatComponent().getUnformattedText();

            if(message.contains("Azerreall joined the game")){
                mc.thePlayer.sendChatMessage("/sk disable NekoAC");
            }
            if(mode.getValString().equalsIgnoreCase("Mc-Central")){
                if(message.contains(mc.thePlayer.getName()) && message.contains("Has Killed")){
                    sendSult();
                }

            }else if(mode.getValString().equalsIgnoreCase("Hypixel")){
                if(message.contains(mc.thePlayer.getName()) && message.contains("was Killed by")){
                    sendSult();
                }
            }else if(mode.getValString().equalsIgnoreCase("Mineplex")){
                if(message.contains(mc.thePlayer.getName()) && (message.contains("killed by") || message.contains("thrown into the void"))){
                    sendSult();
                }
            }
        }
    }

    public void sendSult(){
        String[] exhiKillsults = {"Is this the best you niggers can PvP?","Go drown in your own salt.","Kids like you are the inspiration of birth control.","Your brain is a fucking fax machine","Wait you guys cant fly?", "I dont care if im cheating in a block game, All i care is that you died in a fucking block game.","Toxic nigga","You just got killed by Corbis Swastikation Client a.k.a Execution","Go back to roblox where you belong, you degenerate 6 year old kid","Corbis Swastikation Nermalius","Why the fuck are u using skidma","This is 2020, Corbis Swastikation Nermalius > skidma!","You rush because youre bad, you want the satisfaction of one kill before dying to a noob.","Check out Corbis?#2410 on youtube!","Yo ass jealous of how much shit is getting out ur mouth man","Nermi.us Nigger","dont ever underestimate the power of Corbis Swastikation Nermalius","Wow, you died to a fucking free client (Corbis Swastikation Nermalius) a.k.a Execution"};
        int randomIndex = ThreadLocalRandom.current().nextInt(0, exhiKillsults.length);
        mc.thePlayer.sendChatMessage(exhiKillsults[randomIndex]);
    }
}
