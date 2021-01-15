package info.spicyclient.modules.combat;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventGetBlockReach;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {
	
	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.BETA);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public Timer timer = new Timer();
	
	private static transient int stage = 1;
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket) {
			Packet packet = ((EventSendPacket)e).packet;
			
			if (packet instanceof C02PacketUseEntity) {
				
				if (MovementUtils.isOnGround(0.0000001)) {
					return;
				}
				
				C02PacketUseEntity attack = (C02PacketUseEntity) packet;
				
				if (attack.getAction() == Action.ATTACK) {
					
					//Crit(new Double[] { 0.0, 0.419999986886978, 0.3331999936342235, 0.2481359985909455, 0.164773281826067, 0.083077817806467, 0.0, -0.078400001525879, -0.155232004516602, -0.230527368912964, -0.304316827457544, -0.376630498238655, -0.104080378093037 });
					//Crit(new Double[] {0.0, 0.3199989889898899898989898989, 0.2199989889898899898989898989, 0.0, -0.3199989889898899898989898989, -0.2199989889898899898989898989});
					//Crit2(new Double[] {0.11, 0.1100013579, 0.0000013579});
					
					double[] var5;
		            int var4 = (var5 = new double[]{0.051D, 0.011511D, 0.001D, 0.001D}).length;

		            for(int var3 = 0; var3 < var4; ++var3) {
		                double offset = var5[var3];
		                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
		            }
					
					if (e.isPre()) {
						
						
						
					}
					else if (e.isPost()) {
						
					}
					
				}
				
			}
			
		}
		
	}
	
	// Found these methods on github
	public static void Crit(final Double[] value) {
        final Minecraft mc = Criticals.mc;
        final NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
        final Minecraft mc2 = Criticals.mc;
        final Double curX = mc.thePlayer.posX;
        final Minecraft mc3 = Criticals.mc;
        Double curY = mc.thePlayer.posY;
        final Minecraft mc4 = Criticals.mc;
        final Double curZ = mc.thePlayer.posZ;
        final Double RandomY = 0.0;
        for (final Double offset : value) {
            curY += offset;
            var1.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)curX, curY + RandomY, (double)curZ, false));
        }
    }
	
    public static void Crit2(final Double[] value) {
        final Minecraft mc = Criticals.mc;
        final NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
        final Minecraft mc2 = Criticals.mc;
        final Double curX = mc.thePlayer.posX;
        final Minecraft mc3 = Criticals.mc;
        final Double curY = mc.thePlayer.posY;
        final Minecraft mc4 = Criticals.mc;
        final Double curZ = mc.thePlayer.posZ;
        for (final Double offset : value) {
            var1.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)curX, curY + offset, (double)curZ, false));
        }
    }
	
}
