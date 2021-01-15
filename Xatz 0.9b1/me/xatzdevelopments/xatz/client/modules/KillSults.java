package me.xatzdevelopments.xatz.client.modules;


import me.xatzdevelopments.xatz.utils.*;
import net.minecraft.network.play.server.*;
import java.util.*;

import org.jetbrains.annotations.Contract;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.*;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.module.state.Category;

import me.xatzdevelopments.xatz.module.Module;

public class KillSults extends Module {

    int counter;
    Stopwatch timer;
    

    public KillSults() {
    	super("KillSults", Keyboard.KEY_NONE, Category.PLAYER, "Insult Sigma Virgins.");
      
        this.timer = new Stopwatch();
		
	}

	public void onEnable() {
        this.counter = 0;
    }

    @Override
    public void onUpdate(UpdateEvent e) {
        if (e.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat) e.getPacket();
            final String msg = packet.getChatComponent().getUnformattedText();
            try {
                if (msg.contains("was slain by" + mc.session.getUsername().toString())) { 
                    for (final Object o : KillSults.mc.theWorld.getLoadedEntityList()) {
                        if (o instanceof EntityLivingBase && o != KillSults.mc.thePlayer) {
                            final EntityLivingBase ent = (EntityLivingBase) o;
                            if (!msg.contains(ent.getName())) {
                                continue;
                            }
                            if (this.currentMode.equals("AutoL")) {
                                mc.thePlayer.sendChatMessage("L, ");
                            } else if (this.currentMode.equals("Insult")) {
                                final String[] messages = {"Did ur parents ask you to run away, " + ent.getName(), "I don't cheat, " + ent.getName() + " I just use Helium.", "You do be lookin' kinda bad at the game, " + ent.getName(), "Did someone leave your cage open " + ent.getName() + "?", "rage at me on discord Kansio#6759" + ent.getName(), "Is being in the spectator mode fun, " + ent.getName() + "?", "I understand why your parents abused you, " + ent.getName(), "Do you practice being this bad, " + ent.getName(), "hi my name is " + ent.getName() + " and my iq is -420!", ent.getName() + "'s aim is sponsored by Parkinson's", ent.getName() + " go take a long walk on a short bridge", ent.getName() + " probably plays fortnite lmao.", "plz no repotr i no want ban " + ent.getName() + "!", ent.getName() + ", you probably have the coronavirus.", ent.getName() + ", you really like taking L's.", ent.getName() + " drown in your own salt", ent.getName() + ", I'm not saying you're worthless, but i'd unplug ur lifesupport to charge my phone.", ent.getName() + ", could you please commit not alive?", ent.getName() + " I don't cheat, you just need to click faster", ent.getName() + " I speak english not your gibberish", "Your mom do be lookin' kinda black doe, " + ent.getName(), "Hey look! It's a fortnite player " + ent.getName(), "Need some pvp advice? " + ent.getName() + ".", ent.getName() + ", do you really like dying this much?", ent.getName() + " probably reported me.", ent.getName() + " you're the type to get 3rd place in a 1v1.", ent.getName() + " how does it feel to get stomped on?", ent.getName() + ", the type of guy to use sigma.", ent.getName() + " that's a #VictoryRoyale! better luck next time!", "lol " + ent.getName() + " probably speaks dog eater", ent.getName() + " is a fricking monkey (black person)", ent.getName() + " be like: ''I'm black and this a robbery''", ent.getName() + ", even your mom is better than you in this game.", ent.getName() + " go back to fortnite you degenerate.", ent.getName() + " your iq is that of a steve.", ent.getName() + " go commit stop breathing plz", ent.getName() + ", your parents abandoned you, then the orphanage did the same", ent.getName() + " probably bought sigma premium", ent.getName() + " probably got an error on his hello world program lmao", ent.getName() + " how'd you hit the download button with that aim", "Someone in 1940 forgot to gas you, " + ent.getName() + " :)", ent.getName() + ", did your dad go get milk and never return?", ent.getName() + " you died in a block game.", ent.getName() + " thinks that his ping is equal to his iq.", ent.getName() + " stop eating dogs", "if the body is 70% water then how is " + ent.getName() + "'s body 100% salt?", "yo stop spreading corona, " + ent.getName() + ". I know you're asian, but stop spreading it.", ent.getName() + "'s got dropped him on his head by his parents.", "yo " + ent.getName() + " come rage at me on discord Kansio#6759", ent.getName() + " doesn't have parents L", "how are you so bad? im losing brain cells while watching you play", ent.getName() + " even lolitsalex has more wins than you", "some kids were dropped at birth, but " + ent.getName() + " got thrown at the wall.", ent.getName() + " black"};
                                if (this.counter >= messages.length) {
                                    this.counter = 0;
                                }
                                KillSults.mc.thePlayer.sendChatMessage(String.valueOf(messages[this.counter]));
                            }
                            ++this.counter;
                        }
                    }
                }
            } catch (Exception geh) {
            	geh.printStackTrace();
            }
        }
	}

	public String[] getModes() {
		return new String[] { "AutoL", "Insult" };
		
    }
	
	public String getModeName() {
		return "Mode: ";
	}
}
