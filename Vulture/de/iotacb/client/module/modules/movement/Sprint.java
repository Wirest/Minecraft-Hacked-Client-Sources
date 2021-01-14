package de.iotacb.client.module.modules.movement;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.SprintEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.world.Scaffold;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.cu.core.mc.entity.EntityUtil;
import de.iotacb.cu.core.mc.entity.player.PlayerUtil;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

@ModuleInfo(name = "Sprint", description = "Auto sprints", category = Category.MOVEMENT)
public class Sprint extends Module {
	
	@Override
	public void onInit() {
		addValue(new Value("SprintAll directions", false));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer != null)
			getMc().thePlayer.setSprinting(false);
	}

	@Override
	public void onToggle() {
	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!Client.MOVEMENT_UTIL.isMoving()
				|| getMc().thePlayer.isSneaking()
				|| getMc().thePlayer.isCollidedHorizontally
				|| !(getMc().thePlayer.getFoodStats().getFoodLevel() > 6)) {
			getMc().thePlayer.setSprinting(false);
			return;
		}
		
		if (getMc().thePlayer.moveForward >= .5F || getValueByName("SprintAll directions").getBooleanValue()) {
			getMc().thePlayer.setSprinting(true);
		}
	}

}
