package Blizzard.Mod.mods.movement;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends Mod {
	public Phase() {
		super("Phase", "Phase", Keyboard.KEY_V, Category.MOVEMENT);

	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		phase();
	}

	public void phase() {
		if (mc.thePlayer.onGround) {
			double[] vals = { 0.333, 0 };
			for (double i : vals) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						this.mc.thePlayer.posX, this.mc.thePlayer.posY + i, this.mc.thePlayer.posZ, false));
			}
		}
	}
}
