package com.etb.client.event.events.render;

import com.etb.client.event.Event;

import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent extends Event {
	private ScaledResolution sr;
	private float pt;

	public Render2DEvent(ScaledResolution sr, float pt) {
		this.sr = sr;
		this.pt = pt;
	}

	public ScaledResolution getSR() {
		return this.sr;
	}

	public float getPT() {
		return this.pt;
	}
}
