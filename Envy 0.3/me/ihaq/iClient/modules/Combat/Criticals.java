package me.ihaq.iClient.modules.Combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.BooleanValue;
import me.ihaq.iClient.utils.values.MultiValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {

	public static boolean active;
		
	//private ArrayList<BooleanValue> vals;
	//private MultiValue mode;

	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT, "");
		//vals.add(new BooleanValue(this, "Packets", "criticalpackets", Boolean.valueOf(true)));
		//vals.add(new BooleanValue(this, "Jump", "criticaljump", Boolean.valueOf(false)));
		//mode = new MultiValue(this,"Mode", "criticalsmode",vals);
	}

	@Override
	public void onEnable() {
		active = true;
	}

	@Override
	public void onDisable() {
		active = false;
	}
	
	@EventTarget
	public void onEvent(EventUpdate e){
		
	}

	public static void doCrits() {
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + 0.05D, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + 0.012511D, mc.thePlayer.posZ, false));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY, mc.thePlayer.posZ, false));
	}

}
