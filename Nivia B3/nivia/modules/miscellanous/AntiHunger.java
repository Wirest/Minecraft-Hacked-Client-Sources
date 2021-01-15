package nivia.modules.miscellanous;

import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.Pandora;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketSend;
import nivia.modules.Module;
import nivia.utils.Helper;

public class AntiHunger extends Module {
	public AntiHunger() {
		super("AntiHunger", 0, 0x005C00, Category.MISCELLANEOUS, "Makes you take no hunger.",
				new String[] { "nohunger", "hunger", "antih" }, true);
	}

	@EventTarget
	private void onPacketSend(EventPacketSend event) {
		if (!Pandora.getModManager().getModState("Jesus") && !Helper.blockUtils().isOnLiquid()) {
			if ((event.getPacket() instanceof C03PacketPlayer)) {
				C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
				double yFix = mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
				boolean ground = yFix == 0.0;
				if (ground && !mc.playerController.isHittingBlock)
					packet.field_149474_g = false;
			}
		}
	}
}
