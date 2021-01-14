package de.iotacb.client.events.render;

import com.darkmagician6.eventapi.events.Event;

import de.iotacb.client.events.states.RenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RenderEvent implements Event {

	private final RenderState state;
	
	private final  ScaledResolution sr;
	
	private final float partialTicks;
	
	public RenderEvent(RenderState state, float partialTicks) {
		this.state = state;
		this.partialTicks = partialTicks;
		sr = new ScaledResolution(Minecraft.getMinecraft());
	}
	
	public RenderState getState() {
		return state;
	}
	
	public ScaledResolution getSr() {
		return sr;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
	
}
