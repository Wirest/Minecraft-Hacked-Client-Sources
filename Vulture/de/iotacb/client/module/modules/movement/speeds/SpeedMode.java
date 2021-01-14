package de.iotacb.client.module.modules.movement.speeds;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import net.minecraft.client.Minecraft;

public abstract class SpeedMode {
	
	private final String modeName;
	
	private final static Minecraft MC = Minecraft.getMinecraft();;
	
	public SpeedMode(String modeName) {
		this.modeName = modeName;
	}
	
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract void onToggle();
	
	public abstract void onRender(RenderEvent event);
	public abstract void onUpdate(UpdateEvent event);
	public abstract void onLivingUpdate(LivingUpdateEvent event);
	public abstract void onMove(MoveEvent event);
	public abstract void onTick(TickEvent event);
	public abstract void onSafe(SafewalkEvent event);
	public abstract void onPacket(PacketEvent event);
	
	public String getModeName() {
		return modeName;
	}
	
	public Minecraft getMc() {
		return MC;
	}

}
