package de.iotacb.client.module.modules.player;

import java.time.Year;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.values.Value;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "NoFall", description = "Prevents or reduces fall damage", category = Category.PLAYER)
public class NoFall extends Module {

	@Override
	public void onInit() {
		addValue(new Value("NoFallModes", "Vanilla", "HiveMC", "Cubecraft"));
	}

	@Override
	public void onEnable() {
		damage = true;
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onToggle() {
	}
	
	private boolean damage;
	private BlockPos startPos;

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		setSettingInfo(getValueByName("NoFallModes").getComboValue());
	}
	
	@EventTarget
	public void LivingUpdate(LivingUpdateEvent event) {
		switch (getValueByName("NoFallModes").getComboValue()) {
		case "Cubecraft":
			if (getMc().thePlayer.onGround) {
				startPos = null;
				damage = true;
				return;
			}
			if (getMc().thePlayer.fallDistance < 3) return;
			if (!getMc().thePlayer.onGround && startPos == null) {
				startPos = getMc().thePlayer.getPositionVector().getBlockPos();
			}
			if (startPos == null) {
				damage = true;
				return;
			}
			final BlockPos groundPos = getGroundPos(startPos);
			if (groundPos == null) {
				damage = true;
				return;
			}
			if (getMc().thePlayer.ticksExisted % 8 == 0) {
				getMc().thePlayer.motionY = 0;
			}
			if (getMc().thePlayer.getDistanceToBlockPos(groundPos) < 3) {
				if (damage || getMc().thePlayer.hurtTime >= 1 && getMc().thePlayer.hurtTime <= 4) {
					getMc().getNetHandler().addToSendQueue(new C03PacketPlayer(true));
					damage = false;
					startPos = null;
				}
			}
			break;
		case "HiveMC":
			if (!getMc().thePlayer.onGround) {
				getMc().thePlayer.motionY = -200;
			}
			break;
		case "Vanilla":
			if (getMc().thePlayer.fallDistance > 2) {
				getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			}
		break;
		}
	}
	
	private BlockPos getGroundPos(BlockPos startPos) {
		for (int i = 0; i < startPos.getY(); i++) {
			final BlockPos newPos = startPos.down(i);
			if (newPos.getBlock() != Blocks.air) {
				return newPos;
			}
		}
		return null;
	}
	
//	public void ccdmg() {
//        // standard offset: 0.0385
//		final double offset = 0.001 * 0.0385;
//		final int times = 50;
//		final double falldist = 3;
//        for (int i = 0; i <= times; i++) {
//            getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + (falldist/(double)times) + offset, getMc().thePlayer.posZ, false));
//            getMc().thePlayer.fallDistance += (float) (offset / i);
//            getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, false));
//        }
//        getMc().getNetHandler().addToSendQueue(new C03PacketPlayer(true));
//    }
}
