package de.iotacb.client.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleInfo(name = "WorldTime", description = "Set the ingame time", category = Category.WORLD)
public class WorldTime extends Module {

	@Override
	public void onInit() {
		addValue(new Value("WorldTimeTime", 0, new ValueMinMax(0, 20, 1)));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacket() instanceof S03PacketTimeUpdate) {
			event.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (getMc().thePlayer == null) return;
		
		getMc().theWorld.setWorldTime((long) (getValueByName("WorldTimeTime").getNumberValue()) * 1000);
	}

}
