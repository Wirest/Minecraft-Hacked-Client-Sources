package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.BooleanValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Spider extends Module {

	private static String mode;
	public BooleanValue newMode = new BooleanValue(this, "New", "NewSpider", Boolean.valueOf(true));
	public BooleanValue oldMode = new BooleanValue(this, "Old", "OldSpider", Boolean.valueOf(false));

	public Spider() {
		super("Spider", Keyboard.KEY_NONE, Category.MOVEMENT, mode);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (!this.isToggled())
			return;
		if(newMode.getValue()){
			newMode();
			oldMode.setValue(false);
		}else if(oldMode.getValue()){			
			oldMode();
			newMode.setValue(false);
		}
	}

	public void newMode() {
		setMode(": \u00A7fNew");
		if ((mc.thePlayer.isCollidedHorizontally)) {
			for (int i = 0; i < 20; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 0.049, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, false));

			}
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY, mc.thePlayer.posZ, true));
		}
	}

	public void oldMode() {
		setMode(": \u00A7fOld");
		if ((mc.thePlayer.isCollidedHorizontally)) {
			mc.thePlayer.jump();
		}
	}
}
