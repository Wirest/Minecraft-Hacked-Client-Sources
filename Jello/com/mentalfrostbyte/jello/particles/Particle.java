package com.mentalfrostbyte.jello.particles;

import java.util.Random;

import net.minecraft.client.gui.ScaledResolution;

public class Particle {

	public float x,y,radius,speed,ticks, opacity;
	
	public Particle(ScaledResolution sr, float r, float s){
		x = new Random().nextFloat()*sr.getScaledWidth();
		y = new Random().nextFloat()*sr.getScaledHeight();
		ticks = new Random().nextFloat()*sr.getScaledHeight()/2;
		radius = r;
		speed = s;
	}
}
