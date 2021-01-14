package de.iotacb.client.module.modules.movement;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;
import com.sun.jna.platform.win32.WinNT.LARGE_INTEGER.LowHigh;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.movement.speeds.AAC3313;
import de.iotacb.client.module.modules.movement.speeds.AACBhop;
import de.iotacb.client.module.modules.movement.speeds.BHop;
import de.iotacb.client.module.modules.movement.speeds.CubecraftGround;
import de.iotacb.client.module.modules.movement.speeds.CubecraftHop;
import de.iotacb.client.module.modules.movement.speeds.CubecraftHop2;
import de.iotacb.client.module.modules.movement.speeds.Hive;
import de.iotacb.client.module.modules.movement.speeds.HypixelHop;
import de.iotacb.client.module.modules.movement.speeds.JartexHop;
import de.iotacb.client.module.modules.movement.speeds.JartexHop2;
import de.iotacb.client.module.modules.movement.speeds.LowHop;
import de.iotacb.client.module.modules.movement.speeds.Redesky;
import de.iotacb.client.module.modules.movement.speeds.SpeedMode;
import de.iotacb.client.utilities.values.Value;

@ModuleInfo(name = "Speed", description = "Enables faster movement than other players", category = Category.MOVEMENT)
public class Speed extends Module {
	
	private List<SpeedMode> speedModes;

	@Override
	public void onInit() {
		addValue(new Value("SpeedModes", "HiveMC", "AAC 3.3.13", "AAC Bhop", "CubecraftGround", "CubecraftHop", "CubecraftHop2", "HypixelHop", "JartexHop", "JartexHop2", "Redesky", "LowHop", "BHop"));
		
		speedModes = new ArrayList<SpeedMode>();
		
		addModes(Hive.class,
				 AAC3313.class,
				 AACBhop.class,
				 CubecraftGround.class,
				 CubecraftHop.class,
				 CubecraftHop2.class,
				 JartexHop.class,
				 JartexHop2.class,
				 Redesky.class,
				 LowHop.class,
				 BHop.class,
				 HypixelHop.class
				 );
	}
	
	private void addModes(Class <?extends SpeedMode>...classes) {
		for (final Class <?extends SpeedMode> speedMode : classes) {
			try {
				speedModes.add(speedMode.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private SpeedMode getSpeedMode() {
		return speedModes.stream().filter(speed -> speed.getModeName().equalsIgnoreCase(getValueByName("SpeedModes").getComboValue())).findFirst().orElse(null);
	}

	@Override
	public void onEnable() {
		if (getSpeedMode() != null) getSpeedMode().onEnable();
	}

	@Override
	public void onDisable() {
		if (getSpeedMode() != null) getSpeedMode().onDisable();
	}

	@Override
	public void onToggle() {
		if (getSpeedMode() != null) getSpeedMode().onToggle();
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onRender(event);
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		setSettingInfo(getValueByName("SpeedModes").getComboValue());
		if (getSpeedMode() != null) getSpeedMode().onUpdate(event);
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onLivingUpdate(event);
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onMove(event);
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onTick(event);
	}
	
	@EventTarget
	public void onSafe(SafewalkEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onSafe(event);
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (getSpeedMode() != null) getSpeedMode().onPacket(event);
	}

}
