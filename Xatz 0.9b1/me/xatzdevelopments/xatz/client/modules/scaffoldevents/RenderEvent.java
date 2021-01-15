package me.xatzdevelopments.xatz.client.modules.scaffoldevents;

import me.xatzdevelopments.xatz.client.darkmagician6.*;

import me.xatzdevelopments.xatz.client.modules.scaffoldevents.*;
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
