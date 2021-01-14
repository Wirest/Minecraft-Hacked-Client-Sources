package de.iotacb.client.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.DeathEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.SpawnEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "DeathSpawn", description = "Spawn where you died", category = Category.MISC)
public class DeathSpawn extends Module {

	private BlockPos spawn;
	
	private Value amount;
	
	@Override
	public void onInit() {
		amount = addValue("Tries", 5, new ValueMinMax(1, 50, 1));
	}

	@Override
	public void onEnable() {
		spawn = getMc().thePlayer.getPosition();
	}

	@Override
	public void onDisable() {
		spawn = null;
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onLiving(LivingUpdateEvent event) {
		if (getMc().thePlayer.getHealth() < 3) {
			if (spawn == null) return;
			spawn();
			getMc().thePlayer.setSpawnPoint(spawn, true);
		}
	}
	
	@EventTarget
	public void onDeath(DeathEvent event) {
		if (spawn == null) return;
		getMc().thePlayer.setSpawnPoint(spawn, true);
		spawn();
	}
	
	private void spawn() {
		for (int i = 0; i < (int)amount.getNumberValue(); i++) {
			getMc().getNetHandler().addToSendQueue2(new C03PacketPlayer.C04PacketPlayerPosition(spawn, false));
		}
	}

}
