package de.iotacb.client.module.modules.movement;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.DisconnectEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SpawnEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.movement.flies.CubecraftBoost;
import de.iotacb.client.module.modules.movement.flies.CubecraftFast;
import de.iotacb.client.module.modules.movement.flies.CubecraftFast2;
import de.iotacb.client.module.modules.movement.flies.CubecraftSmooth;
import de.iotacb.client.module.modules.movement.flies.FlyMode;
import de.iotacb.client.module.modules.movement.flies.HiveMC;
import de.iotacb.client.module.modules.movement.flies.Hypixel;
import de.iotacb.client.module.modules.movement.flies.Kektave;
import de.iotacb.client.module.modules.movement.flies.McCentral;
import de.iotacb.client.module.modules.movement.flies.Redesky;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "Fly", description = "Enables you to fly around", category = Category.MOVEMENT)
public class Fly extends Module {

	private List<FlyMode> flyModes;
	
	@Override
	public void onInit() {
		addValue(new Value("FlyModes", "CubecraftFast", "CubecraftFast2", "CubecraftSmooth", "CubecraftBoost", "HiveMC", "McCentral", "Redesky", "Hypixel", "Kektave"));
		addValue(new Value("FlyAir bobbing", true));
		addValue(new Value("FlyMotion y", 4, new ValueMinMax(0, 10, .1)));
		addValue(new Value("FlyBoost", 1, new ValueMinMax(0, 4, .1)));
		
		flyModes = new ArrayList<FlyMode>();
		
		addModes(HiveMC.class,
				CubecraftFast.class,
				CubecraftFast2.class,
				CubecraftSmooth.class,
				CubecraftBoost.class,
				McCentral.class,
				Redesky.class,
				Hypixel.class,
				Kektave.class
				);
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("FlyMotion y").setEnabled(getValueByName("FlyModes"), "McCentral");
		getValueByName("FlyBoost").setEnabled(getValueByName("FlyModes"), "McCentral");
		super.updateValueStates();
	}
	
	void addModes(Class <?extends FlyMode>...classes) {
		for (final Class <?extends FlyMode> flyMode : classes) {
			try {
				flyModes.add(flyMode.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	FlyMode getFlyMode() {
		return flyModes.stream().filter(fly -> fly.getModeName().equalsIgnoreCase(getValueByName("FlyModes").getComboValue())).findFirst().orElse(null);
	}

	@Override
	public void onEnable() {
		if (getMc().thePlayer == null) return;
		if (getFlyMode() != null) getFlyMode().onEnable();
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null) return;
    	if (getFlyMode() != null) getFlyMode().onDisable();
	}

	@Override
	public void onToggle() {
		getMc().timer.timerSpeed = 1F;
	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getFlyMode() != null) getFlyMode().onLivingUpdate(event);
	}

	@EventTarget
	public void onMove(MoveEvent event) {
		if (getFlyMode() != null) getFlyMode().onMove(event);
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		setSettingInfo(getValueByName("FlyModes").getComboValue());
		if (getFlyMode() != null) getFlyMode().onUpdate(event);
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (getValueByName("FlyAir bobbing").getBooleanValue()) {
			getMc().thePlayer.cameraYaw = .1F;
		}
		if (getFlyMode() != null) getFlyMode().onRender(event);
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (getFlyMode() != null) getFlyMode().onPacket(event);
	}
	
	@EventTarget
	public void onDisconnect(DisconnectEvent event) {
		getMc().timer.timerSpeed = 1F;
	}
	
	@EventTarget
	public void onSpawn(SpawnEvent event) {
		if (getFlyMode() != null) getFlyMode().onSpawn(event);
	}
}
