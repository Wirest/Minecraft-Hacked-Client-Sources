package skyline.specc.mods.move;

import java.awt.Color;

import net.minecraft.MoveEvents.EventEntityCollision;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.util.AxisAlignedBB;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket.EventPacketType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.helper.Player;

public class Jesus extends Module {

	public Jesus() {
		super(new ModData("Jesus", 0, new Color(255, 255, 255)), ModType.MOVEMENT);

	}

	@EventListener
	public void onBoundingBox(EventEntityCollision e) {
		if (((e.getBlock() instanceof BlockLiquid)) && e.getEntity() == p && (!Player.isInLiquid())
				&& (p.fallDistance < 3.0F) && (!p.isSneaking())) {
			e.setBoundingBox(
					AxisAlignedBB.fromBounds(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(),
							e.getLocation().getX() + 1, e.getLocation().getY() + 1, e.getLocation().getZ() + 1));
		}
	}

	@EventListener
	public void onPacketSend(EventPacket event) {
		if (event.getPacket() instanceof S49PacketUpdateEntityNBT) {
			S49PacketUpdateEntityNBT packet = (S49PacketUpdateEntityNBT) event.getPacket();

		}

		if ((event.getPacket() instanceof C03PacketPlayer) && event.getType() == EventPacketType.SEND) {
			C03PacketPlayer player = (C03PacketPlayer) event.getPacket();

			if (Player.isOnLiquid()) {
				player.setY(p.posY + (p.ticksExisted % 2 == 0 ? 0.01 : -0.01));
			}
		}
	}

	@EventListener
	public void onUpdate(EventPlayerUpdate event) {
		if (event.getType() == EventType.PRE && Player.isInLiquid() && !p.isSneaking()
				&& !mc.gameSettings.keyBindJump.pressed) {
			p.motionY = 0.1;
		}
	}
}
